/*******************************************************************************
 * Copyright (c) 2018 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.ws.microprofile.config.converters;

import java.lang.reflect.Type;

import com.ibm.websphere.ras.annotation.Trivial;

/**
 * A converter with an explicit Type and priority
 */
public abstract class PriorityConverter {

    private final Type type;
    private final int priority;

    /**
     * Construct a new PriorityConverter using explicit type and priority values
     *
     * @param type The type to convert to
     * @param priority The priority of the converter
     * @param converter The actual converter
     */
    @Trivial
    public PriorityConverter(Type type, int priority) {
        this.priority = priority;
        this.type = type;
    }

    /**
     * @return the priority of this converter
     */
    @Trivial
    public int getPriority() {
        return this.priority;
    }

    /**
     * @return the type of this converter
     */
    @Trivial
    public Type getType() {
        return this.type;
    }

    @Override
    public abstract String toString();

    /**
     * @param rawString
     * @return
     */
    public abstract Object convert(String rawString);
}
