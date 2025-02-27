/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation and others.
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
package com.ibm.ws.microprofile.appConfig.cdi.broken.beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@RequestScoped
public class ConfigUnnamedConstructorInjectionBean {

    private String SIMPLE_KEY4 = null;
    private Config config = null;

    //this will fail
    @Inject
    public ConfigUnnamedConstructorInjectionBean(@ConfigProperty String SIMPLE_KEY4, Config config) {
        this.SIMPLE_KEY4 = SIMPLE_KEY4;
        this.config = config;
    }

    public ConfigUnnamedConstructorInjectionBean() {}

    public String getSIMPLE_KEY4() {
        return SIMPLE_KEY4;
    }

    public String getSIMPLE_KEY5() {
        return config.getValue("SIMPLE_KEY5", String.class);
    }
}
