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
package org.eclipse.emf.henshin.diagram.edit.actions;

import java.text.ParseException;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.graphics.Color;

/**
 * An enum for action types.
 * @generated NOT
 */
public enum ActionType {
	
	PRESERVE(ColorConstants.gray), 
	CREATE(new Color(null, 0, 200, 0)), 
	DELETE(ColorConstants.red), 
	FORBID(ColorConstants.blue),
	REQUIRE(new Color(null, 170, 68, 0));
	
	// Color to be used for this action type.
	private Color color;

	/*
	 * Constructor.
	 */
	private ActionType(Color color) {
		this.color = color;
	}
	
	/**
	 * Parse an element action type.
	 * @param value String representation.
	 * @return The parsed action type.
	 * @throws ParseException On parse errors.
	 */
	public static ActionType parse(String value) throws ParseException {
		value = value.trim();
		for (ActionType type : values()) {
			if (type.name().equalsIgnoreCase(value)) return type;
		}
		// Some convenience...
		if ("remove".equalsIgnoreCase(value)) {
			return DELETE;
		}
		if ("new".equalsIgnoreCase(value)) {
			return CREATE;
		}
		if ("none".equalsIgnoreCase(value)) {
			return PRESERVE;
		}
		throw new ParseException("Unknown action type: " + value, 0);
	}
	
	/**
	 * Get the color for this action type.
	 * @return Color.
	 */
	public Color getColor() {
		return color;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}
	
}