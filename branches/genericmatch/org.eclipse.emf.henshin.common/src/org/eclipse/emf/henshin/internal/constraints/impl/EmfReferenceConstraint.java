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
package org.eclipse.emf.henshin.internal.constraints.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.henshin.internal.constraints.DomainChange;
import org.eclipse.emf.henshin.internal.constraints.ReferenceConstraint;
import org.eclipse.emf.henshin.internal.matching.Variable;

/**
 * This constraint checks whether the value of an EReference contains objects
 * from the target domain.
 */
public class EmfReferenceConstraint implements ReferenceConstraint<EObject> {
	private EReference reference;
	private Variable<EClass, EObject> target;

	public EmfReferenceConstraint(Variable<EClass, EObject> target, EReference eReference) {
		this.target = target;
		this.reference = eReference;
	}

	@SuppressWarnings("unchecked")
	public boolean check(EObject sourceValue, EObject targetValue) {
		if (reference.isMany()) {
			List<EObject> referedObjects = (List<EObject>) sourceValue
					.eGet(reference);
			return (referedObjects.contains(targetValue));
		} else {
			return sourceValue.eGet(reference) == targetValue;
		}
	}

	@SuppressWarnings("unchecked")
	public DomainChange<EObject> reduceTargetDomain(EObject sourceValue,
			List<EObject> targetDomain) {
		List<EObject> referredObjects = null;

		if (sourceValue.eGet(reference) != null) {
			if (reference.isMany()) {
				referredObjects = (List<EObject>) sourceValue.eGet(reference);
			} else {
				referredObjects = new ArrayList<EObject>();
				referredObjects.add((EObject) sourceValue.eGet(reference));
			}
		}

		if (referredObjects != null) {
			if (targetDomain != null) {
				List<EObject> removedObjects = new ArrayList<EObject>(
						targetDomain);
				targetDomain.retainAll(referredObjects);
				removedObjects.removeAll(targetDomain);

				return new DomainChange(targetDomain, removedObjects);
			} else {
				targetDomain = new ArrayList<EObject>(referredObjects);
				return new DomainChange(targetDomain, null);
			}
		}
		
		return new DomainChange(null, null);
	}

	public Variable<EClass, EObject> getTarget() {
		return target;
	}
}