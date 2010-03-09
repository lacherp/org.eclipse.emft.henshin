/*******************************************************************************
 * Copyright (c) 2010 CWI Amsterdam, Technical University of Berlin, 
 * University of Marburg and others. All rights reserved. 
 * This program and the accompanying materials are made 
 * available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     CWI Amsterdam - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.henshin.statespace;

import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;

/**
 * State space manager interface. State managers are used to
 * modify and explore state spaces.
 * @author Christian Krause
 * @generated NOT
 */
public interface StateSpaceManager extends StateSpaceIndex {
	
	/**
	 * Create a new initial state to the state space. This throws a 
	 * runtime exception if the state is not contained in a resource.
	 * If there is already a state for it, it is returned instead.
	 * @param model Model of the initial state.
	 * @return The newly created state.
	 * @exception StateSpaceException If the state space contains errors.
	 */
	State createInitialState(Resource model) throws StateSpaceException;
	
	/**
	 * Remove a state from the state space. Unreachable states are automatically
	 * removed afterwards and the open-attributes are updated.
	 * @param state State to be removed.
	 * @return List of removed states.
	 * @exception StateSpaceException If the state space contains errors.
	 */
	List<State> removeState(State state) throws StateSpaceException;
	
	/**
	 * Explore a state. This computes all outgoing transitions
	 * and their target states and adds them to the state space
	 * if they do not exist yet.
	 * @param state State to be explored.
	 * @param generateLocation Whether to generate a location for the new states.
	 * @return List of newly created outgoing transitions.
	 * @exception StateSpaceException If the state space contains errors.
	 */
	List<Transition> exploreState(State state, boolean generateLocation) throws StateSpaceException;
	
	/**
	 * Reset the state space managed by this instance.
	 * This removes all derived states and all transitions.
	 */
	void resetStateSpace();
	
}
