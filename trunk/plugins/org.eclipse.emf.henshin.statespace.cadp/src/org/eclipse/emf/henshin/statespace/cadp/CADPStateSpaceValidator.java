package org.eclipse.emf.henshin.statespace.cadp;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.henshin.statespace.StateSpace;
import org.eclipse.emf.henshin.statespace.StateSpaceIndex;
import org.eclipse.emf.henshin.statespace.StateSpaceValidator;
import org.eclipse.emf.henshin.statespace.ValidationResult;
import org.eclipse.emf.henshin.statespace.resource.StateSpaceResource;
import org.eclipse.emf.henshin.statespace.util.StateSpaceSearch;

/**
 * CADP state space validator.
 * @author Christian Krause
 */
public class CADPStateSpaceValidator implements StateSpaceValidator {
	
	// Property to be checked:
	private String property;

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.henshin.statespace.StateSpaceValidator#validate(org.eclipse.emf.henshin.statespace.StateSpace, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public ValidationResult validate(StateSpace stateSpace, IProgressMonitor monitor) throws Exception {
		
		monitor.beginTask("Validating property", 10);
		
		// Check the CADP path first:
		File cadpBin = getCADPBin();
		
		// Export the state space to an AUT file:
		File aut = File.createTempFile("aut",".aut");
		OutputStream out = new BufferedOutputStream(new FileOutputStream(aut));
		((StateSpaceResource) stateSpace.eResource()).exportAsAUT(out);
		monitor.worked(1);
		
		// Convert the AUT file to a BCG file:
		File bcg = File.createTempFile("cadp", ".bcg");
		Process process = Runtime.getRuntime().exec(new String[] {
				cadpBin.getAbsolutePath() + File.separator + "bcg_io",
				aut.getAbsolutePath(),
				bcg.getAbsolutePath()
		});
		if (process.waitFor()!=0) {
			throw new RuntimeException("bcg_io returned exit code " + process.exitValue());
		}
		monitor.worked(2);
		
		// Write the property to a MCL file:
		File mcl = File.createTempFile("prop", ".mcl");
		FileWriter writer = new FileWriter(mcl);
		writer.write(property);
		writer.close();
		
		// File for diagnostics output:
		File diag = File.createTempFile("diag", ".bcg");
		
		// Invoke the bcg_open script to run the evaluator:
		process = Runtime.getRuntime().exec(new String[] {
				cadpBin.getParent() + File.separator + "com/bcg_open",
				bcg.getAbsolutePath(),
				"evaluator",
				"-diag",
				diag.getAbsolutePath(),
				mcl.getAbsolutePath(),
		});
		
		// Parse the output
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		Boolean result = null;
		boolean parseTrace = false;
		List<String> path = new ArrayList<String>();
		
		String line;
		while ((line = reader.readLine())!=null) {
			line = line.trim();
			if (line.length()==0) continue;
			if (parseTrace) {
				if (line.indexOf("<goal state>")>=0) {
					parseTrace = false;
				} else {
					path.add(line.startsWith("\"") ? line.substring(1, line.length()-1) : line);
				}
			} else {
				if ("TRUE".equals(line)) {
					result = Boolean.TRUE;
				}
				else if ("FALSE".equals(line)) {
					result = Boolean.FALSE;
				}
				else if (line.indexOf("<initial state>")>=0) {
					parseTrace = true;
				}
			}			
		}
		monitor.worked(7);
		
		// Clean up first:
		aut.delete();
		bcg.delete();
		mcl.delete();
		diag.delete();
		
		monitor.worked(1);
		monitor.done();
		
		// Check the output:
		if (result==Boolean.TRUE) {
			return ValidationResult.VALID;
		}
		else if (result==Boolean.FALSE) {
			if (!path.isEmpty()) {
				return new ValidationResult(false, StateSpaceSearch.findTrace(stateSpace, path));
			}
			return ValidationResult.INVALID;			
		}
		else {
			throw new RuntimeException("CADP evaluator produced unexpected output.");
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.henshin.statespace.Validator#getName()
	 */
	@Override
	public String getName() {
		return "CADP";
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.henshin.statespace.Validator#setProperty(java.lang.String)
	 */
	@Override
	public void setProperty(String property) throws ParseException {
		this.property = property;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.henshin.statespace.Validator#setStateSpaceIndex(org.eclipse.emf.henshin.statespace.StateSpaceIndex)
	 */
	@Override
	public void setStateSpaceIndex(StateSpaceIndex index) {
		// We don't need the index.
	}
	
	/**
	 * Get the CADP 'bin.*' directory.
	 * @return The directory.
	 * @throws FileNotFoundException If the directory was not found.
	 */
	public static File getCADPBin() throws FileNotFoundException {
		String path = System.getenv("CADP");
		if (path==null) {
			throw new FileNotFoundException("CADP environment variable not set.");
		}
		File cadp = new File(path);
		if (!cadp.isDirectory()) {
			throw new FileNotFoundException("CADP home not found. Check the CADP environment variable.");
		}
		File[] bin = cadp.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith("bin");
			}
		});
		if (bin.length==0) {
			throw new FileNotFoundException("Cannot find 'bin' directory in CADP home.");
		}
		return bin[0];
	}
}