package org.eclipse.emf.henshin.model.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.henshin.model.Edge;
import org.eclipse.emf.henshin.model.Graph;
import org.eclipse.emf.henshin.model.HenshinFactory;
import org.eclipse.emf.henshin.model.Mapping;
import org.eclipse.emf.henshin.model.Node;
import org.eclipse.emf.henshin.model.Rule;

/**
 * Common utility methods for Henshin models.
 * @generated NOT
 * @author Christian Krause
 */
public class HenshinUtil {
	
	/**
	 * Get the rule a graph is contained in.
	 * @param graph Graph.
	 * @return Rule or <code>null</code>.
	 */
	public static Rule getRule(Graph graph) {
		EObject container = graph.eContainer();
		while (container!=null) {
			if (container instanceof Rule) return (Rule) container;
			container = container.eContainer();
		}
		return null;
	}
	
	/**
	 * Find a mapping for a given node origin and image. This assumes that
	 * both nodes are properly contained in a graph and a rule.
	 * @param origin Node origin.
	 * @param image Node image.
	 * @return The mapping if found, otherwise <code>null</code>.
	 */
	public static Mapping getMapping(Node origin, Node image) {
		Rule rule = getRule(origin.getGraph());
		for (Mapping mapping : rule.getMappings()) {
			if (mapping.getOrigin()==origin && mapping.getImage()==image) {
				return mapping;
			}
		}
		return null;
	}
	
	/**
	 * Find a mapping for a given origin {@link Node} and target graph. This method 
	 * assumes that the argument {@link Node} is properly contained in a {@link Rule}.
	 * @param origin Origin node.
	 * @param graph Target graph.
	 * @return Mapping if found, <code>null</code> otherwise.
	 */
	public static Mapping getOriginMapping(Node origin, Graph graph) {
		for (Mapping mapping : getRule(graph).getMappings()) {
			if (mapping.getOrigin()==origin) return mapping;
		}
		return null;
	}

	/**
	 * Find a mapping for a given image {@link Node}. This method assumes 
	 * that the argument {@link Node} is properly contained in a {@link Rule}.
	 * @param image Image node.
	 * @return Mapping if found, <code>null</code> otherwise.
	 */
	public static Mapping getImageMapping(Node image) {		
		for (Mapping mapping : getRule(image.getGraph()).getMappings()) {
			if (mapping.getImage()==image) return mapping;
		}
		return null;
	}
	
	/**
	 * Create a mapping for a given node origin and image. This automatically
	 * adds the mapping to the rule.
	 * @param origin Origin node.
	 * @param image Image node.
	 * @return The created mapping.
	 */
	public static Mapping createMapping(Node origin, Node image) {
		Mapping mapping = HenshinFactory.eINSTANCE.createMapping();
		mapping.setOrigin(origin);
		mapping.setImage(image);
		Rule rule = getRule(origin.getGraph()); 
		if (rule!=null) {
			rule.getMappings().add(mapping);
		}
		return mapping;
	}
	
	/**
	 * Delete a node. This removes the node from its container graph and
	 * deletes all mappings that refer to it. If the node is not contained
	 * in a graph, nothing happens.
	 * @param node Node to be deleted.
	 */
	public static void deleteNode(Node node) {
		
		// Delete the edges first:
		List<Edge> edges = new ArrayList<Edge>();
		edges.addAll(node.getIncoming());
		edges.addAll(node.getOutgoing());
		for (Edge edge : edges) {
			deleteEdge(edge);
		}
		
		// Check if it is contained in a graph:
		Graph graph = node.getGraph();
		if (graph!=null) {
			
			// Remove the node from the graph:
			graph.getNodes().remove(node);
			Rule rule = getRule(graph);
			if (rule!=null) {
				
				// Remove the mappings:
				for (int i=0; i<rule.getMappings().size(); i++) {
					Mapping mapping = rule.getMappings().get(i);
					if (mapping.getOrigin()==node || mapping.getImage()==node) {
						rule.getMappings().remove(i--);
					}
				}	
			}
		}
	}
	
	/**
	 * Delete an edge. This detaches the edge from its source and target node
	 * and removes it from the graph it is contained in.
	 * @param edge Edge to be deleted.
	 */
	public static void deleteEdge(Edge edge) {
		edge.setSource(null);
		edge.setTarget(null);
		edge.setGraph(null);
	}
	
}
