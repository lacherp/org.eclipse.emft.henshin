/*******************************************************************************
 * Copyright (c) 2010 CWI Amsterdam, Technical University of Berlin, 
 * University of Marburg and others. All rights reserved. 
 * This program and the accompanying materials are made 
 * available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Technical University of Berlin - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.henshin.internal.constraints;

import java.util.List;

public class DomainChange<T> {
	public List<T> removedObjects;
	public List<T> remainingObjects;

	public DomainChange(List<T> remainingObjects, List<T> removedObjects) {
		this.remainingObjects = remainingObjects;
		this.removedObjects = removedObjects;
	}
}
