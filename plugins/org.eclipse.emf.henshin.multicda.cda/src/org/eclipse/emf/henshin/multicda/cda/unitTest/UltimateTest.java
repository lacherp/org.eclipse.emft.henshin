package org.eclipse.emf.henshin.multicda.cda.unitTest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.emf.henshin.multicda.cda.framework.CdaWorker;
import org.eclipse.emf.henshin.multicda.cda.framework.Condition.CCR;
import org.eclipse.emf.henshin.multicda.cda.framework.Condition.CEDNCR;
import org.eclipse.emf.henshin.multicda.cda.framework.Condition.ChCR;
import org.eclipse.emf.henshin.multicda.cda.framework.Condition.ConditionsSet;
import org.eclipse.emf.henshin.multicda.cda.framework.Condition.DCR;
import org.eclipse.emf.henshin.multicda.cda.framework.Condition.Edge;
import org.eclipse.emf.henshin.multicda.cda.framework.Condition.Node;
import org.eclipse.emf.henshin.multicda.cda.framework.Condition.ReasonSize;
import org.eclipse.emf.henshin.multicda.cda.framework.Condition.StateProvider;
import org.eclipse.emf.henshin.multicda.cda.framework.CpaWorker;
import org.eclipse.emf.henshin.multicda.cda.framework.Options;
import org.eclipse.emf.henshin.multicda.cda.units.Reason;
import org.eclipse.emf.henshin.multicda.cpa.result.CriticalPair;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UltimateTest {

	private static CdaWorker cda;
	private static CpaWorker cpa;
	private static CdaWorker cdaD;
	private static CpaWorker cpaD;
	private static String henshin = "testData/main/test.henshin";
	private static Options options = new Options(Options.PRINT_WHEN_RESULT);
	private static Options optionsD = new Options(Options.PRINT_WHEN_RESULT, Options.DEPENDENCY);
	private static boolean executeAll = false;
	private static boolean executeCpa = false;
	private static boolean compare = executeCpa && true;

	@BeforeClass
	public static void before() {
		CdaWorker.ignore = prooved;
		options.remove(Options.PRINT_HEADER, Options.PRINT_RESULT);
		optionsD.remove(Options.PRINT_HEADER, Options.PRINT_RESULT);
	}

	private static String time(long time) {
		long milis = time % 1000;
		long seconds = (time / 1000) % 60;
		long minutes = (time / 60000) % 60;
		long hours = (time / 3600000) % 24;
		long days = (time / 86400000) % 365;
		long years = (time / 31536000000L);

		return (years == 0 ? "" : years + "y, ") + (days == 0 ? "" : days + "d, ") + (hours == 0 ? "" : hours + "h, ")
				+ (minutes == 0 ? "" : minutes + "m, ") + (seconds + "s, " + milis + "ms");
	}

	private static Map<String, Set<String>> prooved = new HashMap<>();

	private static Set<Reason> computeReasons(String r1, String r2) {
		Set<String> r2s = prooved.get(r1);
		Set<Reason> cdaResult = new TreeSet<>();
		if (r2s == null || !r2s.contains(r2)) {
			Set<CriticalPair> cpaResult = new HashSet<>();
			if (executeAll)
				cda = new CdaWorker(henshin, options);
			else
				cda = new CdaWorker(henshin, r1, r2, options);
			cdaResult.addAll(cda.getResult());
			if (executeCpa) {
				if (executeAll)
					cpa = new CpaWorker(henshin, options);
				else
					cpa = new CpaWorker(henshin, new String[] { r1 }, new String[] { r2 }, options);
				cpaResult.addAll(cpa.getResult());
			}
			if (compare)
				cda.compare(cpa.getResult());
			if (options.is(Options.PRINT_HEADER) || options.is(Options.PRINT_RESULT))
				System.out.println("----------------------------------------------------------------");
			if (executeAll)
				cdaD = new CdaWorker(henshin, optionsD);
			else
				cdaD = new CdaWorker(henshin, r1, r2, optionsD);
			cdaResult.addAll(cdaD.getResult());
			if (executeCpa) {
				if (executeAll)
					cpaD = new CpaWorker(henshin, optionsD);
				else
					cpaD = new CpaWorker(henshin, new String[] { r1 }, new String[] { r2 }, optionsD);
				cpaResult.addAll(cpaD.getResult());
			}
			if (options.is(Options.PRINT_HEADER) || options.is(Options.PRINT_RESULT))
				System.out.println("Reasons found: " + cdaResult.size());
			if (executeCpa)
				System.out.println("Critical Pairs found: " + cpaResult.size());
			if (compare)
				cdaD.compare(cpaD.getResult());
			if (options.is(Options.PRINT_HEADER) || options.is(Options.PRINT_RESULT))
				System.out.println("______________________________________________________________________________");
			if (r2s == null) {
				r2s = new HashSet<>();
			}
			r2s.add(r2);
			prooved.put(r1, r2s);
		} else {
			cda.reset();
			cdaD.reset();
		}
		return cdaResult;
	}

	private static long allTime = 0;

	@Test
	public void deleteTest() {
		long time = System.currentTimeMillis();
		computeReasons("d1", "p1");
		cda.check(new ReasonSize(1));
		cdaD.check(new ReasonSize());
		computeReasons("d1", "p2");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("d1", "p6");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("d1", "d1");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("d1", "d2");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("d1", "d6");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("d1", "d7");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("d1", "c1");
		cda.check(new ReasonSize(1));
		cdaD.check(new ReasonSize());
		computeReasons("d1", "c3");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("d1", "c4");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("d1", "c6");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("d1", "f1");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(1));
		computeReasons("d1", "f2");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(2));
		computeReasons("d1", "f3");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("d1", "f4");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("d1", "f6");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("d1", "f7");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(2));
		computeReasons("d1", "r1");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("d1", "r2");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("d1", "r3");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize());
		computeReasons("d1", "r6");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("d1", "r7");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("d1", "ch");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());

		computeReasons("d2", "p1");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("d2", "p2");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d2", "p6");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d2", "d1");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d2", "d2");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d2", "d6");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d2", "d7");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d2", "c1");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("d2", "c3");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d2", "c4");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d2", "c6");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d2", "f1");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(2));
		computeReasons("d2", "f2");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("d2", "f3");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d2", "f4");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d2", "f6");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d2", "f7");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("d2", "r1");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d2", "r2");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d2", "r6");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d2", "r7");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d2", "ch");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());

		computeReasons("d3", "p3");
		cda.check(new ReasonSize(1));
		cdaD.check(new ReasonSize());
		computeReasons("d3", "d3");
		cda.check(new ReasonSize(1));
		cdaD.check(new ReasonSize());
		computeReasons("d3", "d4");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize(6));
		computeReasons("d3", "f4");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(2));
		computeReasons("d3", "f3");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(1));
		computeReasons("d3", "f5");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(1));
		computeReasons("d3", "r5");
		cda.check(new ReasonSize(1));
		cdaD.check(new ReasonSize());
		computeReasons("d3", "r3");
		cda.check(new ReasonSize(1));
		cdaD.check(new ReasonSize());
		computeReasons("d3", "r4");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());

		computeReasons("d4", "p1");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("d4", "p2");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d4", "p3");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("d4", "p6");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d4", "d3");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("d4", "d4");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize(6));
		computeReasons("d4", "d5");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize(6));
		computeReasons("d4", "d6");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d4", "c1");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("d4", "c3");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d4", "c4");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d4", "c6");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d4", "f3");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d4", "f4");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(3));
		computeReasons("d4", "f5");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(2));
		computeReasons("d4", "f6");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d4", "f7");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("d4", "r1");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d4", "r3");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("d4", "r2");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d4", "r4");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d4", "r5");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("d4", "r6");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d4", "r7");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d4", "ch");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());

		computeReasons("d5", "p1");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("d5", "p3");
		cda.check(new ReasonSize(1));
		cdaD.check(new ReasonSize());
		computeReasons("d5", "p2");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d5", "p6");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d5", "d1");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("d5", "d4");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("d5", "d5");
		cda.check(new ReasonSize(1));
		cdaD.check(new ReasonSize());
		computeReasons("d5", "d3");
		cda.check(new ReasonSize(1));
		cdaD.check(new ReasonSize());
		computeReasons("d5", "d6");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d5", "c1");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("d5", "c3");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d5", "c4");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d5", "c6");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d5", "f1");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(2));
		computeReasons("d5", "f2");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("d5", "f3");
		cda.check(new ReasonSize(5));
		cdaD.check(new ReasonSize());
		computeReasons("d5", "f4");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d5", "f5");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(1));
		computeReasons("d5", "f6");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d5", "f7");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("d5", "r1");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d5", "r4");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("d5", "r5");
		cda.check(new ReasonSize(1));
		cdaD.check(new ReasonSize());
		computeReasons("d5", "r2");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d5", "r3");
		cda.check(new ReasonSize(1));
		cdaD.check(new ReasonSize());
		computeReasons("d5", "r6");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d5", "r7");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d5", "r7");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d5", "p6");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d5", "r6");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d5", "ch");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());

		computeReasons("d6", "p6");
		cda.check(new ReasonSize(3));
		cdaD.check(new ReasonSize());
		computeReasons("d6", "d6");
		cda.check(new ReasonSize(3));
		cdaD.check(new ReasonSize());
		computeReasons("d6", "c6");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("d6", "d7");
		cda.check(new ReasonSize(3));
		cdaD.check(new ReasonSize());
		computeReasons("d6", "r6");
		cda.check(new ReasonSize(3));
		cdaD.check(new ReasonSize());
		computeReasons("d6", "r7");
		cda.check(new ReasonSize(3));
		cdaD.check(new ReasonSize());
		computeReasons("d6", "ch");
		cda.check(new ReasonSize(3));
		cdaD.check(new ReasonSize());

		computeReasons("d7", "p1");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("d7", "p2");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d7", "p6");
		cda.check(new ReasonSize(3));
		cdaD.check(new ReasonSize());
		computeReasons("d7", "d1");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d7", "d2");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d7", "d6");
		cda.check(new ReasonSize(3));
		cdaD.check(new ReasonSize());
		computeReasons("d7", "d7");
		cda.check(new ReasonSize(3));
		cdaD.check(new ReasonSize());
		computeReasons("d7", "c1");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("d7", "c3");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d7", "c4");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d7", "f1");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(2));
		computeReasons("d7", "f2");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("d7", "f3");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d7", "f4");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d7", "f7");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("d7", "r1");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d7", "r2");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("d7", "r6");
		cda.check(new ReasonSize(3));
		cdaD.check(new ReasonSize());
		computeReasons("d7", "r7");
		cda.check(new ReasonSize(3));
		cdaD.check(new ReasonSize());
		computeReasons("d7", "ch");
		cda.check(new ReasonSize(3));
		cdaD.check(new ReasonSize());
		time = System.currentTimeMillis() - time;
		allTime += time;
		System.out.println("Delete complete... " + time(time));
	}

	@Test
	public void createTest() {
		long time = System.currentTimeMillis();
		computeReasons("c1", "p1");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(1));
		computeReasons("c1", "p2");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(2));
		computeReasons("c1", "d1");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(2));
		computeReasons("c1", "d2");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(2));
		computeReasons("c1", "c1");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(1));
		computeReasons("c1", "c3");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(2));
		computeReasons("c1", "c4");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(2));
		computeReasons("c1", "c6");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(2));
		computeReasons("c1", "f1");
		cda.check(new ReasonSize(1));
		cdaD.check(new ReasonSize());
		computeReasons("c1", "f2");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("c1", "f3");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(2));
		computeReasons("c1", "f4");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(2));
		computeReasons("c1", "f6");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(2));
		computeReasons("c1", "f7");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("c1", "r1");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(2));
		computeReasons("c1", "r2");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(2));

		computeReasons("c2", "p1");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(2));
		computeReasons("c2", "p2");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("c2", "d1");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("c2", "d2");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("c2", "c1");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(2));
		computeReasons("c2", "c3");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("c2", "c4");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("c2", "c6");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("c2", "f1");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("c2", "f2");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("c2", "f3");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("c2", "f4");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("c2", "f6");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("c2", "f7");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("c2", "r1");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("c2", "r2");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));

		computeReasons("c3", "p3");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(1));
		computeReasons("c3", "f5");
		cda.check(new ReasonSize(1));
		cdaD.check(new ReasonSize());
		computeReasons("c3", "d3");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(1));
		computeReasons("c3", "f3");
		cda.check(new ReasonSize(1));
		cdaD.check(new ReasonSize());
		computeReasons("c3", "f4");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("c3", "r5");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(1));
		computeReasons("c3", "r3");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(1));
		computeReasons("c3", "r4");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(2));

		computeReasons("c4", "p1");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(2));
		computeReasons("c4", "p2");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("c4", "p3");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(2));
		computeReasons("c4", "d3");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(2));
		computeReasons("c4", "c1");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(2));
		computeReasons("c4", "c3");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("c4", "c4");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("c4", "c6");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("c4", "f3");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("c4", "f4");
		cda.check(new ReasonSize(3));
		cdaD.check(new ReasonSize());
		computeReasons("c4", "f5");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("c4", "f6");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("c4", "f7");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("c4", "r1");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("c4", "r2");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("c4", "r3");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(2));
		computeReasons("c4", "r4");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("c4", "r5");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(2));
		computeReasons("c4", "r5");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));

		computeReasons("c5", "p1");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(2));
		computeReasons("c5", "p3");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(1));
		computeReasons("c5", "p2");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("c5", "d1");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(2));
		computeReasons("c5", "d3");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(1));
		computeReasons("c5", "d4");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(2));
		computeReasons("c5", "d5");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(1));
		computeReasons("c5", "c1");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(2));
		computeReasons("c5", "c3");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("c5", "c4");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("c5", "c6");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("c5", "f1");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("c5", "f2");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("c5", "f3");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(5));
		computeReasons("c5", "f4");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("c5", "f5");
		cda.check(new ReasonSize(1));
		cdaD.check(new ReasonSize());
		computeReasons("c5", "f6");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("c5", "f7");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("c5", "r1");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("c5", "r5");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(1));
		computeReasons("c5", "r2");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("c5", "r3");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(1));
		computeReasons("c5", "r4");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(2));

		computeReasons("c6", "p6");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(3));
		computeReasons("c6", "d6");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(3));
		computeReasons("c6", "d7");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(3));
		computeReasons("c6", "c6");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("c6", "ch");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(3));
		computeReasons("c6", "r6");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(3));
		computeReasons("c6", "r7");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(3));

		computeReasons("c7", "p1");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(2));
		computeReasons("c7", "p2");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("c7", "p6");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(3));
		computeReasons("c7", "d1");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("c7", "d2");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("c7", "d6");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(3));
		computeReasons("c7", "d7");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(3));
		computeReasons("c7", "c1");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(2));
		computeReasons("c7", "c3");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("c7", "c4");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("c7", "f1");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("c7", "f2");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("c7", "f3");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("c7", "f4");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("c7", "f7");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("c7", "r1");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("c7", "r2");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("c7", "r6");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(3));
		computeReasons("c7", "r7");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(3));
		computeReasons("c7", "ch");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(3));
		time = System.currentTimeMillis() - time;
		allTime += time;
		System.out.println("Create complete... " + time(time));

	}

	@Test
	public void changeTest() {
		long time = System.currentTimeMillis();
		computeReasons("ch", "p6");
		cda.check(new ReasonSize(3));
		cdaD.check(new ReasonSize(3));
		computeReasons("ch", "d6");
		cda.check(new ReasonSize(3));
		cdaD.check(new ReasonSize(3));
		computeReasons("ch", "r6");
		cda.check(new ReasonSize(3));
		cdaD.check(new ReasonSize(3));
		computeReasons("ch", "r7");
		cda.check(new ReasonSize(3));
		cdaD.check(new ReasonSize(3));

		computeReasons("ch", "ch");
		cda.check(new ReasonSize(3));
		cdaD.check(new ReasonSize(3));
		computeReasons("ch", "d7");
		cda.check(new ReasonSize(3));
		cdaD.check(new ReasonSize(3));

		time = System.currentTimeMillis() - time;
		allTime += time;
		System.out.println("Change complete... " + time(time));
	}

	@Test
	public void edgeNodeTest() {
		long time = System.currentTimeMillis();
		computeReasons("c3", "d1");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize());
		computeReasons("c3", "d2");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("c3", "d4");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize(2));
		computeReasons("c3", "d5");
		cda.check(new ReasonSize(5));
		cdaD.check(new ReasonSize(1));
		computeReasons("c3", "d7");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("c4", "d1");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize(2));
		computeReasons("c4", "d2");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());
		computeReasons("c4", "d4");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize(6));
		computeReasons("c4", "d5");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize(2));
		computeReasons("c4", "d7");
		cda.check(new ReasonSize(6));
		cdaD.check(new ReasonSize());

		computeReasons("d3", "d1");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(2));
		computeReasons("d3", "d2");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("d3", "d5");
		cda.check(new ReasonSize(1));
		cdaD.check(new ReasonSize(5));
		computeReasons("d3", "d7");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("d4", "d1");
		cda.check(new ReasonSize(2));
		cdaD.check(new ReasonSize(2));
		computeReasons("d4", "d2");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		computeReasons("d4", "d7");
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize(6));
		time = System.currentTimeMillis() - time;
		allTime += time;
		System.out.println("Edge-Node complete... " + time(time));
	}

	@Test
	public void restTest() {
		long time = System.currentTimeMillis();
		computeReasons(null, null);
		cda.check(new ReasonSize());
		cdaD.check(new ReasonSize());
		time = System.currentTimeMillis() - time;
		allTime += time;
		System.out.println("Zero Reason pairs complete... " + time(time));
	}

	@Test
	public void heavyTest() {
		String henshin = "testData/main/attributes.henshin";
		cda = new CdaWorker(henshin, "complex", new Options());
		cdaD = new CdaWorker(henshin, "complex", new Options(Options.DEPENDENCY));

		ConditionsSet cons = new ConditionsSet(false, false, true);
		cons.add(new ChCR(StateProvider.REQUIRE, new Node("3_2")));
		cons.add(new ChCR(new Node("3_3")));
		cons.add(new DCR(new Node("8_8")));
		cons.add(new DCR(new Node("1_1")));
		cons.add(new DCR(new Node("1_1"), new Node("8_3")));
		cons.add(new DCR(new Node("1_1"), new Node("8_8")));

		cons.add(new CCR(new Node("4_4")));
		cons.add(new CCR(new Node("6_6")));
		cons.add(new CCR(new Node("6_6"), new Node("4_4")));
		cons.add(new CEDNCR(new Node("3_8")));
		cons.add(new CEDNCR(new Node("3_8"), new Node("1_1")));
		cda.check(cons);

		cons = new ConditionsSet(false, false, true);
		cons.add(new ChCR(StateProvider.DEPENDENCY, new Node("3_1")));
		cons.add(new CCR(StateProvider.DEPENDENCY, new Edge("4_4", "3_1")));
		cons.add(new CCR(StateProvider.DEPENDENCY, new Edge("4_4", "3_1"), new Node("6_3")));
		cons.add(new CCR(StateProvider.DEPENDENCY, new Edge("4_4", "3_1"), new Node("6_2")));
		cons.add(new CCR(StateProvider.DEPENDENCY_REQUIRE, new Node("4_2")));
		cons.add(new CCR(StateProvider.DEPENDENCY_REQUIRE, new Node("6_2")));
		cdaD.check(cons);
	}

	@Test
	public void heavyTestF() {
		String henshin = "testData/main/attributes.henshin";
		cda = new CdaWorker(henshin, "complexF1", "complexF2", new Options());
		cdaD = new CdaWorker(henshin, "complexF1", "complexF2", new Options(Options.DEPENDENCY));

		ConditionsSet cons = new ConditionsSet(false, false, true);
		cons.add(new CCR(StateProvider.FORBID, new Node("4_44")));
		cons.add(new CCR(StateProvider.FORBID, new Node("3_44")));
		cons.add(new CCR(StateProvider.FORBID, new Edge("5_22", "4_11")));
		cons.add(new CCR(StateProvider.FORBID, new Edge("5_22", "4_33")));
		cons.add(new CCR(StateProvider.FORBID, new Edge("5_22", "4_11"), new Node("3_44")));
		cons.add(new CCR(StateProvider.FORBID, new Edge("5_22", "4_33"), new Node("3_44")));
		cda.check(cons);

		cons = new ConditionsSet(false, false, true);
		cons.add(new DCR(StateProvider.DEPENDENCY_FORBID, new Node("1_44")));
		cons.add(new DCR(StateProvider.DEPENDENCY_FORBID, new Edge("2_22", "1_11")));
		cons.add(new DCR(StateProvider.DEPENDENCY_FORBID, new Edge("2_22", "1_33")));
		cdaD.check(cons);
	}

	@AfterClass
	public static void after() {
		System.out.println("\ncomplete time: " + time(allTime));
	}
}
