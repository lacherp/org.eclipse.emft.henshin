/*******************************************************************************
 * Copyright (c) 2010 CWI Amsterdam, Technical University Berlin, 
 * Philipps-University Marburg and others. All rights reserved. 
 * This program and the accompanying materials are made 
 * available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Philipps-University Marburg - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.henshin.testframework;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.henshin.matching.EmfGraph;
import org.eclipse.emf.henshin.interpreter.RuleApplicationImpl;
import org.eclipse.emf.henshin.interpreter.UnitApplicationImpl;
import org.eclipse.emf.henshin.interpreter.impl.EmfEngine;
import org.eclipse.emf.henshin.interpreter.util.Match;
import org.eclipse.emf.henshin.model.Rule;

//TODO: add more support for non-injective interpreters

/**
 * Assertions for everything related to matches, e.g. matches produced by
 * {@link RuleApplicationImpl}s, {@link UnitApplicationImpl}s, (partial) matches on
 * element groups, etc.
 * 
 * @see Rules
 * @see TransformationUnits
 * @see ElementGroups
 * @see Graphs
 * @see GraphTransformations
 * @author Felix Rieger
 * @author Stefan Jurack (sjurack)
 * 
 */
public class Matches {
	/**
	 * Assert that an object is contained in all of a {@link Rule}'s matches.
	 * 
	 * @param r
	 *            {@link Rule}
	 * @param graph
	 *            {@link EmfGraph} the Rule will be applied to
	 * @param object
	 *            {@link EObject}
	 * @throws AssertionError
	 */
	public static void assertObjectContainedInAllMatches(Rule r, EmfGraph graph, EObject object)
			throws AssertionError {
		assertObjectContainedInAllMatches(r, new EmfEngine(graph), object);
	}
	
	/**
	 * Assert that an object is contained in all of a {@link Rule}'s matches.
	 * 
	 * @param r
	 *            {@link Rule}
	 * @param engine
	 *            {@link EmfEngine} by which the Rule will be executed
	 * @param object
	 *            {@link EObject}
	 * @throws AssertionError
	 */
	public static void assertObjectContainedInAllMatches(Rule r, EmfEngine engine, EObject object)
			throws AssertionError {
		assertObjectContainedInAllMatches(new RuleApplicationImpl(engine, r), object);
	}
	
	/**
	 * Assert that an object is contained in all of a {@link RuleApplicationImpl}'s
	 * matches
	 * 
	 * @param ra
	 *            {@link RuleApplicationImpl}
	 * @param object
	 *            {@link EObject}
	 * @throws AssertionError
	 */
	public static void assertObjectContainedInAllMatches(RuleApplicationImpl ra, EObject object)
			throws AssertionError {
		boolean cMatch = false;
		for (Match m : ra.findAllMatches()) {
			cMatch = false;
			if (m.getNodeMapping().values().contains(object)) {
				cMatch = true;
			}
			if (cMatch == false) {
				throw new AssertionError("expected: Object contained in every match, but it's not");
			}
		}
		if (ra.findAllMatches().size() == 0) {
			throw new AssertionError(
					"expected: Object contained in every match, but Rule produces no matches");
		}
	}
	
	/**
	 * Assert that an object is contained in none of a {@link Rule}'s matches
	 * 
	 * @param r
	 *            {@link Rule}
	 * @param graph
	 *            {@link EmfGraph} the {@link Rule} will be applied to
	 * @param object
	 *            {@link EObject}
	 * @throws AssertionError
	 */
	public static void assertObjectContainedInNoMatch(Rule r, EmfGraph graph, EObject object)
			throws AssertionError {
		assertObjectContainedInNoMatch(r, new EmfEngine(graph), object);
	}
	
	/**
	 * Assert that an object is contained in none of a {@link Rule}'s matches
	 * 
	 * @param r
	 *            {@link Rule}
	 * @param engine
	 *            {@link EmfEngine} by which the {@link Rule} will be executed
	 * @param object
	 *            {@link EObject}
	 * @throws AssertionError
	 */
	public static void assertObjectContainedInNoMatch(Rule r, EmfEngine engine, EObject object)
			throws AssertionError {
		assertObjectContainedInNoMatch(new RuleApplicationImpl(engine, r), object);
	}
	
	/**
	 * Assert that an object is contained in none of a {@link RuleApplicationImpl}'s
	 * matches
	 * 
	 * @param ra
	 *            {@link RuleApplicationImpl}
	 * @param object
	 *            {@link EObject}
	 * @throws AssertionError
	 */
	public static void assertObjectContainedInNoMatch(RuleApplicationImpl ra, EObject object)
			throws AssertionError {
		for (Match m : ra.findAllMatches()) {
			if (m.getNodeMapping().values().contains(object)) {
				throw new AssertionError(
						"expected: Object contained in no match, but contained in at least one");
			}
		}
	}
	
	/**
	 * Assert that an object is contained in exactly n of a {@link Rule}'s
	 * matches
	 * 
	 * @param r
	 *            {@link Rule}
	 * @param graph
	 *            {@link EmfGraph} the {@link Rule} will be applied to
	 * @param object
	 *            {@link EObject}
	 * @param n
	 *            Number of expected matches
	 * @throws AssertionError
	 */
	public static void assertObjectContainedInNMatches(Rule r, EmfGraph graph, EObject object, int n)
			throws AssertionError {
		assertObjectContainedInNMatches(r, new EmfEngine(graph), object, n);
	}
	
	/**
	 * Assert that an object is contained in exactly n of a {@link Rule}'s
	 * matches
	 * 
	 * @param r
	 *            {@link Rule}
	 * @param engine
	 *            {@link EmfEngine} by which the {@link Rule} will be executed
	 * @param object
	 *            {@link EObject}
	 * @param n
	 *            Number of expected matches
	 * @throws AssertionError
	 */
	public static void assertObjectContainedInNMatches(Rule r, EmfEngine engine, EObject object,
			int n) throws AssertionError {
		assertObjectContainedInNMatches(new RuleApplicationImpl(engine, r), object, n);
	}
	
	/**
	 * Assert that an object is contained in exactly n of a
	 * {@link RuleApplicationImpl}'s matches
	 * 
	 * @param ra
	 *            {@link RuleApplicationImpl}
	 * @param object
	 *            {@link EObject}
	 * @param n
	 *            Number of expected matches
	 * @throws AssertionError
	 */
	public static void assertObjectContainedInNMatches(RuleApplicationImpl ra, EObject object, int n)
			throws AssertionError {
		int num = n;
		for (Match m : ra.findAllMatches()) {
			if (m.getNodeMapping().values().contains(object)) {
				num--;
			}
			if (num < 0) {
				throw new AssertionError("expected: Object contained in " + n
						+ " matches, but contained in more");
			}
		}
		if (num > 0) {
			throw new AssertionError("expected: Object contained in " + n
					+ " matches, but contained in less");
		}
	}
	
	/**
	 * Assert that an object is contained in at least one of a {@link Rule}'s
	 * matches
	 * 
	 * @param r
	 *            {@link Rule}
	 * @param graph
	 *            {@link EmfGraph} the {@link Rule} will be applied to
	 * @param object
	 *            {@link EObject}
	 * @throws AssertionError
	 */
	public static void assertObjectContainedInAtLeastOneMatch(Rule r, EmfGraph graph, EObject object)
			throws AssertionError {
		assertObjectContainedInAtLeastOneMatch(r, new EmfEngine(graph), object);
	}
	
	/**
	 * Assert that an object is contained in at least one of a {@link Rule}'s
	 * matches
	 * 
	 * @param r
	 *            {@link Rule}
	 * @param engine
	 *            {@link EmfEngine} by which the {@link Rule} will be executed
	 * @param object
	 *            {@link EObject}
	 * @throws AssertionError
	 */
	public static void assertObjectContainedInAtLeastOneMatch(Rule r, EmfEngine engine,
			EObject object) throws AssertionError {
		assertObjectContainedInAtLeastOneMatch(new RuleApplicationImpl(engine, r), object);
	}
	
	/**
	 * Assert that an object is contained in at least one of a
	 * {@link RuleApplicationImpl}'s matches
	 * 
	 * @param ra
	 *            {@link RuleApplicationImpl}
	 * @param object
	 *            {@link EObject}
	 * @throws AssertionError
	 */
	public static void assertObjectContainedInAtLeastOneMatch(RuleApplicationImpl ra, EObject object)
			throws AssertionError {
		for (Match m : ra.findAllMatches()) {
			if (m.getNodeMapping().values().contains(object)) {
				return;
			}
		}
		
		throw new AssertionError("expected: Object is contained in at least one match of "
				+ ra.getRule().getName() + ", but is contained in none");
	}
	
	/**
	 * Assert that no element contained in the specified {@link Collection} is
	 * contained in any {@link Match} produced by applying the specified
	 * {@link Rule} on the {@link EmfGraph}
	 * 
	 * @param r
	 *            {@link Rule}
	 * @param graph
	 *            {@link EmfGraph} the {@link Rule} will be applied to.
	 * @param group
	 *            {@link Collection} of {@link EObject}s
	 */
	public static void assertNoObjectFromGroupContainedInAnyMatch(Rule r, EmfGraph graph,
			Collection<? extends EObject> group) {
		assertNoObjectFromGroupContainedInAnyMatch(r, new EmfEngine(graph), group);
	}
	
	/**
	 * Assert that no element contained in the specified {@link Collection} is
	 * contained in any {@link Match} produced by executing the specified
	 * {@link Rule} on the {@link EmfEngine}
	 * 
	 * @param r
	 *            {@link Rule}
	 * @param engine
	 *            {@link EmfEngine} by which the {@link Rule} will be executed
	 * @param group
	 *            {@link Collection} of {@link EObject}s
	 * @throws AssertionError
	 */
	public static void assertNoObjectFromGroupContainedInAnyMatch(Rule r, EmfEngine engine,
			Collection<? extends EObject> group) throws AssertionError {
		assertNoObjectFromGroupContainedInAnyMatch(new RuleApplicationImpl(engine, r), group);
	}
	
	/**
	 * Assert that no element contained in the specified {@link Collection} is
	 * contained in any {@link Match} produced by the specified
	 * {@link RuleApplicationImpl}
	 * 
	 * @param ra
	 *            {@link RuleApplicationImpl}
	 * @param group
	 *            {@link Collection} of {@link EObject}s
	 * @throws AssertionError
	 */
	public static void assertNoObjectFromGroupContainedInAnyMatch(RuleApplicationImpl ra,
			Collection<? extends EObject> group) throws AssertionError {
		for (Match m : ra.findAllMatches()) {
			for (EObject eo : group) {
				if (m.getNodeMapping().values().contains(eo)) {
					throw new AssertionError(
							"expected: No object from group is contained in any match, but at least "
									+ eo + " is contained in at least one");
				}
			}
		}
	}
	
	/**
	 * Assert that the whole group is not contained in any {@link Match}
	 * produced by applying the {@link Rule} on the {@link EmfGraph}
	 * 
	 * @param r
	 *            {@link Rule}
	 * @param graph
	 *            {@link EmfGraph} the {@link Rule} will be applied to
	 * @param group
	 *            {@link Collection} of {@link EObject}s
	 * @throws AssertionError
	 */
	public static void assertGroupContainedInNoMatch(Rule r, EmfGraph graph,
			Collection<? extends EObject> group) throws AssertionError {
		assertGroupContainedInNoMatch(r, new EmfEngine(graph), group);
	}
	
	/**
	 * Assert that the whole group is not contained in any {@link Match}
	 * produced by executing the {@link Rule} on the {@link EmfEngine}
	 * 
	 * @param r
	 *            {@link Rule}
	 * @param engine
	 *            {@link EmfEngine} by which the {@link Rule} will be executed
	 * @param group
	 *            {@link Collection} of {@link EObject}s
	 * @throws AssertionError
	 */
	public static void assertGroupContainedInNoMatch(Rule r, EmfEngine engine,
			Collection<? extends EObject> group) throws AssertionError {
		assertGroupContainedInNoMatch(new RuleApplicationImpl(engine, r), group);
	}
	
	/**
	 * Assert that the whole group is not contained in any {@link Match}
	 * produced by the {@link RuleApplicationImpl}
	 * 
	 * @param ra
	 *            {@link RuleApplicationImpl}
	 * @param group
	 *            {@link Collection} of {@link EObject}s
	 * @throws AssertionError
	 */
	public static void assertGroupContainedInNoMatch(RuleApplicationImpl ra,
			Collection<? extends EObject> group) throws AssertionError {
		for (Match m : ra.findAllMatches()) {
			if (m.getNodeMapping().values().containsAll(group)) {
				throw new AssertionError(
						"expected: Group is contained in no match, but is contained in at least one");
			}
		}
	}
	
	/**
	 * Assert that the whole group is contained in at least one {@link Match}
	 * produced by applying the specified {@link Rule} to the {@link EmfGraph}
	 * 
	 * @param r
	 *            {@link Rule}
	 * @param graph
	 *            {@link EmfGraph} the {@link Rule} will be applied to
	 * @param group
	 *            {@link Collection} of {@link EObject}s
	 * @throws AssertionError
	 */
	public static void assertGroupContainedInAtLeastOneMatch(Rule r, EmfGraph graph,
			Collection<? extends EObject> group) throws AssertionError {
		assertGroupContainedInAtLeastOneMatch(r, new EmfEngine(graph), group);
	}
	
	/**
	 * Assert that the whole group is contained in at least one {@link Match}
	 * produced by executing the specified {@link Rule} with the
	 * {@link EmfEngine}
	 * 
	 * @param r
	 *            {@link Rule}
	 * @param engine
	 *            {@link EmfEngine} by which the {@link Rule} will be executed
	 * @param group
	 *            {@link Collection} of {@link EObject}s
	 * @throws AssertionError
	 */
	public static void assertGroupContainedInAtLeastOneMatch(Rule r, EmfEngine engine,
			Collection<? extends EObject> group) throws AssertionError {
		assertGroupContainedInAtLeastOneMatch(new RuleApplicationImpl(engine, r), group);
	}
	
	/**
	 * Assert that the whole group is contained in at least one {@link Match}
	 * produced by the {@link RuleApplicationImpl}
	 * 
	 * @param ra
	 *            {@link RuleApplicationImpl}
	 * @param group
	 *            {@link Collection} of {@link EObject}s
	 * @throws AssertionError
	 */
	public static void assertGroupContainedInAtLeastOneMatch(RuleApplicationImpl ra,
			Collection<? extends EObject> group) throws AssertionError {
		for (Match m : ra.findAllMatches()) {
			if (m.getNodeMapping().values().containsAll(group)) {
				return;
			}
		}
		
		throw new AssertionError("expected: Group is contained in at least one match of "
				+ ra.getRule().getName() + ", but is contained in none");
	}
	
	/**
	 * Assert that at least one object from the group is contained in at least
	 * on {@link Match} produced by applying the specified {@link Rule} on the
	 * {@link EmfGraph}
	 * 
	 * @param r
	 *            {@link Rule}
	 * @param graph
	 *            {@link EmfGraph} the {@link Rule} will be applied to
	 * @param group
	 *            {@link Collection} of {@link EObject}s
	 */
	public static void assertAnyObjectFromGroupContainedInAtLeastOneMatch(Rule r, EmfGraph graph,
			Collection<? extends EObject> group) throws AssertionError {
		assertAnyObjectFromGroupContainedInAtLeastOneMatch(r, new EmfEngine(graph), group);
	}
	
	/**
	 * Assert that at least one object from the group is contained in at least
	 * one {@link Match} produced by executing the specified {@link Rule} with
	 * the {@link EmfEngine}
	 * 
	 * @param r
	 *            {@link Rule}
	 * @param engine
	 *            {@link EmfEngine} by which the {@link Rule} is executed
	 * @param group
	 *            {@link Collection} of {@link EObject}s
	 * @throws AssertionError
	 */
	public static void assertAnyObjectFromGroupContainedInAtLeastOneMatch(Rule r, EmfEngine engine,
			Collection<? extends EObject> group) throws AssertionError {
		assertAnyObjectFromGroupContainedInAtLeastOneMatch(new RuleApplicationImpl(engine, r), group);
	}
	
	/**
	 * Assert that at least one object from the group is contained in at least
	 * one {@link Match} produced by the {@link RuleApplicationImpl}
	 * 
	 * @param ra
	 *            {@link RuleApplicationImpl}
	 * @param group
	 *            {@link Collection} of {@link EObject}s
	 * @throws AssertionError
	 */
	public static void assertAnyObjectFromGroupContainedInAtLeastOneMatch(RuleApplicationImpl ra,
			Collection<? extends EObject> group) throws AssertionError {
		List<Match> matches = ra.findAllMatches();
		for (Match m : matches) {
			for (EObject eo : group) {
				if (m.getNodeMapping().values().contains(eo)) {
					return;
				}
			}
		}
		
		throw new AssertionError(
				"expected: At least one object from group is contained in at least one match, but isn't.");
	}
	
	/**
	 * Asserts that an {@link EObject} is contained in at least one match
	 * produced by the {@link RuleApplicationImpl}s created by executing the
	 * {@link UnitApplicationImpl}.
	 * 
	 * @param ua
	 *            {@link UnitApplicationImpl}
	 * @param element
	 *            {@link EObject}
	 * @throws AssertionError
	 */
	public static void assertElementMatchedByUnitApplication(UnitApplicationImpl ua, EObject element)
			throws AssertionError {
		for (RuleApplicationImpl ra : ua.getAppliedRules()) {
			if (ra.getMatch().getNodeMapping().containsValue(element)) {
				return;
			}
		}
		
		throw new AssertionError("expected: Element " + element
				+ " matched by a Rule in the UnitApplication, but wasn't.");
	}
	
	/**
	 * Asserts that an {@link EObject} is not contained in any matches produced
	 * by the {@link RuleApplicationImpl}s created by executing the
	 * {@link UnitApplicationImpl}.
	 * 
	 * @param ua
	 *            {@link UnitApplicationImpl}
	 * @param element
	 *            {@link EObject}
	 * @throws AssertionError
	 */
	public static void assertElementNotMatchedByUnitApplication(UnitApplicationImpl ua, EObject element)
			throws AssertionError {
		for (RuleApplicationImpl ra : ua.getAppliedRules()) {
			if (ra.getMatch().getNodeMapping().containsValue(element)) {
				throw new AssertionError("expected: Element " + element
						+ " not matched by a Rule in the UnitApplication, but was matched by "
						+ (ra.getRule().getName()));
			}
		}
	}
	
	/**
	 * Asserts that an {@link EObject} is contained in n matches produced by the
	 * {@link RuleApplicationImpl}s created by executing the {@link UnitApplicationImpl}
	 * .
	 * 
	 * @param ua
	 *            {@link UnitApplicationImpl}
	 * @param element
	 *            {@link EObject}
	 * @param n
	 *            number of times the element should be matched
	 * @throws AssertionError
	 */
	public static void assertElementMatchedByUnitApplicationNTimes(UnitApplicationImpl ua,
			EObject element, int n) throws AssertionError {
		int ctr = n;
		for (RuleApplicationImpl ra : ua.getAppliedRules()) {
			if (ra.getMatch().getNodeMapping().containsValue(element)) {
				ctr--;
			}
		}
		
		if (ctr == 0) {
			return;
		} else {
			throw new AssertionError("expected: Element " + element
					+ " matched by a Rule in the UnitApplication " + n + " times, but was matched "
					+ (n - ctr) + " times");
		}
	}
	
	// ---- 2010-11-23 ----vvv
	
	/**
	 * Assert that the group (as a whole) is matched by the any
	 * {@link RuleApplicationImpl} executed by the {@link UnitApplicationImpl}
	 * 
	 * @param ua
	 *            {@link UnitApplicationImpl}
	 * @param group
	 *            {@link Collection} of {@link EObject}s
	 * @throws AssertionError
	 */
	public static void assertGroupMatchedByUnitApplication(UnitApplicationImpl ua,
			Collection<? extends EObject> group) throws AssertionError {
		for (RuleApplicationImpl ra : ua.getAppliedRules()) {
			if (ra.getMatch().getNodeMapping().values().containsAll(group)) {
				return;
			}
		}
		
		throw new AssertionError("expected: Group matched by unit application");
	}
	
	/**
	 * Assert that the group (as a whole) is not matched by any
	 * {@link RuleApplicationImpl} executed by the {@link UnitApplicationImpl}
	 * 
	 * @param ua
	 *            {@link UnitApplicationImpl}
	 * @param group
	 *            {@link Collection} of {@link EObject}s
	 * @throws AssertionError
	 */
	public static void assertGroupNotMatchedByUnitApplication(UnitApplicationImpl ua,
			Collection<? extends EObject> group) throws AssertionError {
		for (RuleApplicationImpl ra : ua.getAppliedRules()) {
			if (ra.getMatch().getNodeMapping().values().containsAll(group)) {
				throw new AssertionError("expected: Group not matched by unit application");
			}
		}
	}
	
	/**
	 * Assert that no element in the specified group is matched by any
	 * {@link RuleApplicationImpl} executed by the {@link UnitApplicationImpl}.
	 * 
	 * @param ua
	 *            {@link UnitApplicationImpl}
	 * @param group
	 *            {@link Collection} of {@link EObject}s
	 * @throws AssertionError
	 */
	public static void assertNoElementInGroupMatchedByAnyRuleInUnitApplication(UnitApplicationImpl ua,
			Collection<? extends EObject> group) throws AssertionError {
		for (RuleApplicationImpl ra : ua.getAppliedRules()) {
			for (EObject eo : group) {
				if (ra.getMatch().getNodeMapping().containsValue(eo)) {
					throw new AssertionError(
							"Expected: No element from group matched by any RuleApplication, but <"
									+ eo + "> was matched by <" + ra.getRule().getName() + ">");
				}
			}
		}
	}
	
	/**
	 * Assert that all elements contained in the specified group are matched by
	 * the {@link UnitApplicationImpl}, i.e. each element is matched at least once
	 * by at least one executed {@link RuleApplicationImpl}
	 * 
	 * @param ua
	 *            {@link UnitApplicationImpl}
	 * @param group
	 *            {@link Collection} of {@link EObject}s
	 * @throws AssertionError
	 */
	public static void assertAllElementsInGroupMatchedByUnitApplication(UnitApplicationImpl ua,
			Collection<? extends EObject> group) throws AssertionError {
		HashMap<EObject, Boolean> matchStatus = new HashMap<EObject, Boolean>(); // create
																					// a
																					// new
																					// HashMap
		for (EObject eo : group) {
			matchStatus.put(eo, false); // add all objects from group, set value
										// = false
		}
		
		// iterate through the applied rules
		for (RuleApplicationImpl ruleApp : ua.getAppliedRules()) {
			for (EObject eo : ruleApp.getMatch().getNodeMapping().values()) {
				if (matchStatus.containsKey(eo)) {
					// if a matched object is contained in the group (and thus
					// in the HashMap),
					// change its corresponding value in the HashMap to true,
					// indicating it was matched by a Rule
					matchStatus.put(eo, true);
				}
			}
		}
		
		if (matchStatus.containsValue(false)) {
			throw new AssertionError(
					"Expected: All elements from group are matched, but some are not.");
		}
	}
	
	// 2011-01-12
	/**
	 * Asserts that a RuleApplication matches all elements from the group (not
	 * neccessarily in one match; if this is desired, use
	 * {@link assertGroupContainedInAtLeastOneMatch})
	 * 
	 * @param ra
	 *            {@link RuleApplicationImpl}
	 * @param group
	 *            {@link Collection} of {@link EObject}s
	 * @throws AssertionError
	 */
	public static void assertGroupIsMatched(RuleApplicationImpl ra, Collection<? extends EObject> group)
			throws AssertionError {
		HashMap<EObject, Boolean> matchContained = new HashMap<EObject, Boolean>();
		for (EObject eo : group) {
			matchContained.put(eo, false);
		}
		
		for (Match m : ra.findAllMatches()) {
			for (EObject eo2 : m.getNodeMapping().values()) {
				matchContained.put(eo2, true);
			}
		}
		
		if (matchContained.containsValue(false)) {
			System.out.println("~~~~~~");
			for (Entry<EObject, Boolean> s : matchContained.entrySet()) {
				if (s.getValue() == false) {
					System.out.println(s.getKey() + " -> " + s.getValue());
				}
			}
			throw new AssertionError("expected: group is matched, but some elements aren't.");
		}
	}
	
	/**
	 * Asserts that a {@link RuleApplicationImpl} matches all elements from the
	 * group (not neccessarily in one match) and the match doesn't contain any
	 * objects not in the group.
	 * 
	 * @param ra
	 * @param group
	 * @throws AssertionError
	 */
	public static void assertOnlyGroupIsMatched(RuleApplicationImpl ra,
			Collection<? extends EObject> group) throws AssertionError {
		if ((ra.getInterpreterEngine() instanceof EmfEngine) &&
				(!(ra.getRule().isInjectiveMatching()))) {
//				&& (!(((EmfEngine) ra.getInterpreterEngine()).getOptions().isInjective()))) {
			// non-injective mode
			HashMap<EObject, Integer> matchContents = new HashMap<EObject, Integer>();
			
			for (EObject eo : group) {
				if (matchContents.containsKey(eo)) {
					matchContents.put(eo, matchContents.get(eo) + 1);
				} else {
					matchContents.put(eo, 1);
				}
			}
			
			for (Match m : ra.findAllMatches()) {
				for (EObject eo : m.getNodeMapping().values()) {
					if (group.contains(eo)) {
						matchContents.put(eo, matchContents.get(eo) - 1);
					} else {
						throw new AssertionError(
								"expected: entire group + only elements from group are matched, but match contains elements not in group");
					}
				}
			}
			
			for (Integer i : matchContents.values()) {
				if (i != 0) {
					throw new AssertionError(
							"expected: group is matched, but some elements aren't.");
				}
			}
			
		} else {
			HashMap<EObject, Boolean> matchContents = new HashMap<EObject, Boolean>();
			
			for (EObject eo : group) {
				matchContents.put(eo, false);
			}
			
			for (Match m : ra.findAllMatches()) {
				for (EObject eo : m.getNodeMapping().values()) {
					if (group.contains(eo)) {
						matchContents.put(eo, true);
					} else {
						throw new AssertionError(
								"expected: entire group + only elements from group are matched, but match contains elements not in group");
					}
				}
			}
			
			if (matchContents.containsValue(false)) {
				throw new AssertionError("expected: group is matched, but some elements aren't.");
			}
		}
	}
	
	/**
	 * Assert that a {@link Match} contains only elements from the group.
	 * 
	 * @param match
	 * @param group
	 * @throws AssertionError
	 */
	public static void assertMatchIsGroup(Match match, Collection<? extends EObject> group)
			throws AssertionError {
		HashMap<EObject, Integer> matchContents = new HashMap<EObject, Integer>();
		
		for (EObject eo : group) {
			if (matchContents.containsKey(eo)) {
				matchContents.put(eo, matchContents.get(eo) + 1);
			} else {
				matchContents.put(eo, 1);
			}
		}
		
		for (EObject eo : match.getNodeMapping().values()) {
			if (group.contains(eo)) {
				matchContents.put(eo, matchContents.get(eo) - 1);
			} else {
				throw new AssertionError("match contains elements not in group");
			}
		}
		
		for (Integer i : matchContents.values()) {
			if (i != 0) {
				throw new AssertionError(
						"expected: match is group, but some elements in the group are not in the match");
			}
		}
	}
	
}