/*******************************************************************************
 * Copyright (c) 2010 CWI Amsterdam, Technical University Berlin, 
 * Philipps-University Marburg and others. All rights reserved. 
 * This program and the accompanying materials are made 
 * available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     CWI Amsterdam - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.henshin.statespace.resource;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * @author Christian Krause
 */
public class StateSpaceResourceWorker implements Runnable {
	
	// URI of the state space resource:
	private URI uri;
	
	// Resource set:
	private ResourceSet resourceSet;
	
	// The actual state space resource:
	private StateSpaceResource resource;

	// IOException:
	private IOException exception;
	
	
	/**
	 * Constructor for a worker that loads a state space resource.
	 * @param uri URI of the state space resource.
	 */
	public StateSpaceResourceWorker(URI uri, ResourceSet resourceSet) {
		this.uri = uri;
		this.resourceSet = resourceSet;
	}

	/**
	 * Constructor for a worker that saves a state space resource.
	 * @param resource The state space resource.
	 */
	public StateSpaceResourceWorker(StateSpaceResource resource) {
		this.resource = resource;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		
		try {
			if (uri!=null) {
				// Loading:
				resource = (StateSpaceResource) resourceSet.getResource(uri,true);
			} else {
				// Saving:
				resource.save(null);
			}
		} catch (IOException e) {
			this.exception = e;
		}
		
	}
	
	/**
	 * Get the loaded resource.
	 * @return State space resource.
	 */
	public StateSpaceResource getResource() {
		return resource;
	}
	
	public IOException getException() {
		return exception;
	}
	
}
