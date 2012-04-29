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
package org.eclipse.emf.henshin.tests.basicTests;


import org.eclipse.emf.henshin.testframework.GraphTransformations;
import org.eclipse.emf.henshin.testframework.HenshinLoaders;
import org.eclipse.emf.henshin.testframework.HenshinTest;
import org.eclipse.emf.henshin.testframework.Rules;
import org.eclipse.emf.henshin.testframework.Tools;
import org.junit.Before;
import org.junit.Test;

/**
 * tests deleting nodes
 * @author Felix Rieger
 * @author Stefan Jurack (sjurack)
 *
 */
public class DeleteNodes extends HenshinTest {

	@Before
	public void setUp() throws Exception {
		init("basicTestRules/basicTests.henshin");
		setModelGraphProperties("basicTestModels/deleteNodesModels/", "testmodel");
	}
	
/*
 * ======================================
 * 				DELETING NODES
 * ======================================
 */
	
	@Test
	public void testDeleteNodeWithContainmentEdge() {
		loadGraph("graphWithOneNode");
		loadRule("deleteNodeWithContainmentEdge");
		GraphTransformations.assertTransformsGraph(htRule, htEmfGraph, HenshinLoaders.loadGraph(getGraphURI("graphWithCont")), 0.9);
	}
	
	@Test
	public void testDeleteNodeWithUnidirectionalEdge1() {
		loadGraph("graphBefore_deleteNodeWithUnidirectionalEdge");
		loadRule("deleteNodeWithUnidirectionalEdge1");
		GraphTransformations.assertTransformsGraph(htRule, htEmfGraph, HenshinLoaders.loadGraph(getGraphURI("graphAfter_deleteNodeWithUnidirectionalEdge1")), 0.9);
	}
	
	@Test
	public void testDeleteNodeWithUnidirectionalEdge2() {
		loadGraph("graphBefore_deleteNodeWithUnidirectionalEdge");
		loadRule("deleteNodeWithUnidirectionalEdge2");
		GraphTransformations.assertTransformsGraph(htRule, htEmfGraph, HenshinLoaders.loadGraph(getGraphURI("graphAfter_deleteNodeWithUnidirectionalEdge2")), 0.9);
	}

	// removed testDeleteNodeWithBidirectionalEdge1
	// removed testDeleteNodeWithBidirectionalEdge2

	
	@Test
	public void testDeleteNodeWithBidirectionalEdge3() {
		loadGraph("graphBefore_deleteNodeWithBidirectionalEdge");
		loadRule("deleteNodeWithBidirectionalEdge3");
		GraphTransformations.assertTransformsGraph(htRule, htEmfGraph, HenshinLoaders.loadGraph(getGraphURI("graphAfter_deleteNodeWithBidirectionalEdge")), 0.9);
	}
	
	// removed testDeleteNodeWithBidirectionalEdge4
	// removed testDeleteNodeWithBidirectionalEdge5

	
	

	@Test
	public void testDeleteNodesEdgeOrder1() {
		/**
		 * Deletes several Nodes:
       	 *		   cont
		 *		   |    \
		 *		 Node(1) Node(2)
		 *		   |
		 *		  Val
		 *		  
		 *	Specified in the Rule's LHS (all Nodes, Vals and Edges are deleted):
		 *	Edge cont->Node(1)
		 *	Edge cont->Val
		 *	Edge cont->Node(2)
		 *	Edge Node(1)->Val
		 *	
		 *	When the Rule is applied, Node(1) will be the first element in an internal list;
		 *	as soon as it is deleted, the reference to Node(2) (the second element in the internal list)
		 *	will become incorrect, resulting in an error.
		 *	This should not be the case, as the order of Edges in the LHS is irrelevant. (See testDeleteNodesEdgeOrder2)
		 */
		loadGraph("graphBefore_deleteNodesEdgeOrder");
		loadRule("deleteNodesEdgeOrder1");
		Tools.printGraph(htEmfGraph);
		Tools.printMatches(htRuleApp.findAllMatches());
		//System.out.println(htRuleApp.apply());
		//Tools.printGraph(htEmfGraph);
		GraphTransformations.assertTransformsGraph(htRule, htEmfGraph, HenshinLoaders.loadGraph(getGraphURI("graphWithCont")), 0.9);
	}
	
	@Test
	public void testDeleteNodesEdgeOrder2() {
		/**
		 * Deletes several Nodes:
       	 *		   cont
		 *		   |    \
		 *		 Node(1) Node(2)
		 *		   |
		 *		  Val
		 *		  
		 *	Specified in the Rule's LHS (all Nodes, Vals and Edges are deleted):
		 *	Edge cont->Node(2)
		 *	Edge cont->Node(1)
		 *	Edge cont->Val
		 *	Edge Node(1)->Val
		 */
		loadGraph("graphBefore_deleteNodesEdgeOrder");
		loadRule("deleteNodesEdgeOrder2");
		Tools.printGraph(htEmfGraph);
		Tools.printMatches(htRuleApp.findAllMatches());
		//System.out.println(htRuleApp.apply());
		//Tools.printGraph(htEmfGraph);
		GraphTransformations.assertTransformsGraph(htRule, htEmfGraph, HenshinLoaders.loadGraph(getGraphURI("graphWithCont")), 0.9);
	}
	
	// ----
	
	@Test
	public void testDeleteRootNode() {
		/**
		 * Delete a root node in a graph
		 */
		loadGraph("graphWithRootNode");
		loadRule("deleteRootNode");
		Rules.assertRuleCanBeApplied(htRuleApp);
		if (Tools.getGraphRoot(htEmfGraph) != null) {
			throw new AssertionError("expected: Root node deleted, but a root node still exists.");
		}
	}
}