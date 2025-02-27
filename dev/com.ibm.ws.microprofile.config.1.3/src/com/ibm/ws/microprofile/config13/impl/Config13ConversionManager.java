/*******************************************************************************
 * Copyright (c) 2018, 2020 IBM Corporation and others.
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

package com.ibm.ws.microprofile.config13.impl;

import com.ibm.websphere.ras.Tr;
import com.ibm.websphere.ras.TraceComponent;
import com.ibm.ws.ffdc.annotation.FFDCIgnore;
import com.ibm.ws.microprofile.config.converters.PriorityConverterMap;
import com.ibm.ws.microprofile.config12.converters.ImplicitConverter;
import com.ibm.ws.microprofile.config12.impl.Config12ConversionManager;
import com.ibm.ws.microprofile.config13.converters.Config13ImplicitConverter;

import io.openliberty.microprofile.config.internal.common.ConfigException;

public class Config13ConversionManager extends Config12ConversionManager {

    private static final TraceComponent tc = Tr.register(Config13ConversionManager.class);

    /**
     * @param converters all the converters to use
     */
    public Config13ConversionManager(PriorityConverterMap converters, ClassLoader classLoader) {
        super(converters, classLoader);
    }

    @Override
    @FFDCIgnore(IllegalArgumentException.class)
    protected <T> ImplicitConverter newImplicitConverter(Class<T> type) {
        ImplicitConverter implicitConverter = null;
        try {
            implicitConverter = new Config13ImplicitConverter(type);
        } catch (IllegalArgumentException e) {
            //no FFDC
            //this means that a suitable string constuctor method could not be found for the given class
            //ignore the exception and return null
            if (TraceComponent.isAnyTracingEnabled() && tc.isDebugEnabled()) {
                Tr.debug(tc, "getConverter (INFO): An automatic converter for type ''{0}'' could not be constructed: {2}.", type, e);
            }
        } catch (Throwable t) {
            if (TraceComponent.isAnyTracingEnabled() && tc.isDebugEnabled()) {
                Tr.debug(tc, "getConverter: An automatic converter for type ''{0}'' could not be constructed: {2}.", type, t);
            }
            throw new ConfigException(t);
        }
        return implicitConverter;
    }

}
