/*
 * Copyright (c) 2010 HPI Potsdam, Technical University Berlin, 
 * Philipps-University Marburg and others. All rights reserved. 
 * This program and the accompanying materials are made 
 * available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     HPI Potsdam - initial API and implementation
 */
package org.eclipse.emf.henshin.diagram.edit.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.henshin.diagram.providers.HenshinElementTypes;
import org.eclipse.emf.henshin.model.HenshinFactory;
import org.eclipse.emf.henshin.model.HenshinPackage;
import org.eclipse.emf.henshin.model.TransformationSystem;
import org.eclipse.emf.henshin.model.TransformationUnit;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.ui.menus.PopupMenu;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.commands.EditElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

/**
 * This command creates a new transformation unit.
 * It pops up a drop-down menu for choosing the unit type.
 * @generated NOT
 */
public class UnitCreateCommand extends EditElementCommand {

	// Parent shell to be used for displaying the menu.
	private Shell shell;

	/**
	 * Default constructor (without menu).
	 * @generated
	 */
	public UnitCreateCommand(CreateElementRequest request) {
		super(request.getLabel(), null, request);
	}

	/**
	 * Constructor with shell (with menu).
	 * @generated NOT
	 */
	public UnitCreateCommand(CreateElementRequest request, Shell shell) {
		this(request);
		this.shell = shell;
	}

	/**
	 * FIXME: replace with setElementToEdit()
	 * @generated
	 */
	protected EObject getElementToEdit() {
		EObject container = ((CreateElementRequest) getRequest())
				.getContainer();
		if (container instanceof View) {
			container = ((View) container).getElement();
		}
		return container;
	}

	/**
	 * @generated
	 */
	public boolean canExecute() {
		return true;

	}

	/**
	 * We need to ask the user what kind of transformation unit should be created.
	 * @generated NOT
	 */
	protected CommandResult doExecuteWithResult(IProgressMonitor monitor,
			IAdaptable info) throws ExecutionException {

		// The fall-back unit type if the shell is not set:
		EClass unitType = HenshinPackage.eINSTANCE.getSequentialUnit();

		// Display the pop-up menu:
		if (shell != null) {
			PopupMenu menu = getPopupMenu();
			if (menu.show(shell) == false) {
				monitor.setCanceled(true);
				return CommandResult.newCancelledCommandResult();
			}
			unitType = (EClass) menu.getResult();
		}

		// Create the transformation unit:
		TransformationUnit unit = (TransformationUnit) HenshinFactory.eINSTANCE
				.create(unitType);

		// Add it to the transformation system:
		TransformationSystem system = (TransformationSystem) getElementToEdit();
		system.getTransformationUnits().add(unit);

		// Configure the unit:
		doConfigure(unit, monitor, info);

		// Done.
		((CreateElementRequest) getRequest()).setNewElement(unit);
		return CommandResult.newOKCommandResult(unit);

	}

	/**
	 * Create a pop-up menu for choosing the unit type.
	 * @return Pop-up menu instance.
	 * @generated NOT
	 */
	protected PopupMenu getPopupMenu() {

		// Supported unit types:
		List<EClass> unitTypes = new ArrayList<EClass>();
		unitTypes.add(HenshinPackage.eINSTANCE.getSequentialUnit());
		unitTypes.add(HenshinPackage.eINSTANCE.getPriorityUnit());
		unitTypes.add(HenshinPackage.eINSTANCE.getIndependentUnit());
		unitTypes.add(HenshinPackage.eINSTANCE.getConditionalUnit());
		unitTypes.add(HenshinPackage.eINSTANCE.getCountedUnit());

		// Label provider:
		ILabelProvider labelProvider = new org.eclipse.jface.viewers.LabelProvider() {
			@Override
			public Image getImage(Object element) {
				return HenshinElementTypes.getImage((EClass) element);
			}

			@Override
			public String getText(Object element) {
				return ((EClass) element).getName().replaceFirst("Unit",
						" Unit");
			}
		};

		// Create and return the pop-up menu:
		return new PopupMenu(unitTypes, labelProvider);

	}

	/**
	 * @generated
	 */
	protected void doConfigure(TransformationUnit newElement,
			IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		IElementType elementType = ((CreateElementRequest) getRequest())
				.getElementType();
		ConfigureRequest configureRequest = new ConfigureRequest(
				getEditingDomain(), newElement, elementType);
		configureRequest.setClientContext(((CreateElementRequest) getRequest())
				.getClientContext());
		configureRequest.addParameters(getRequest().getParameters());
		ICommand configureCommand = elementType
				.getEditCommand(configureRequest);
		if (configureCommand != null && configureCommand.canExecute()) {
			configureCommand.execute(monitor, info);
		}
	}

}
