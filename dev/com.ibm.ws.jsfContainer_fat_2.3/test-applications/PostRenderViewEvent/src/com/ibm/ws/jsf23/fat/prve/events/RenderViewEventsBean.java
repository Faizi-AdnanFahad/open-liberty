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
package com.ibm.ws.jsf23.fat.prve.events;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * A bean that has two methods. One that is invoked when the PostRenderViewEvent is fired
 * and another that is to be invoked when the PreRenderViewEvent is fired.
 *
 */
@Named
@RequestScoped
public class RenderViewEventsBean {

    /**
     * Log some information when the PostRenderViewEvent is fired.
     */
    public void processPostRenderView() {
        FacesContext.getCurrentInstance().getExternalContext().log("Processing PostRenderViewEvent");
    }

    /**
     * Log some information when the PreRenderViewEvent is fired.
     */
    public void processPreRenderView() {
        FacesContext.getCurrentInstance().getExternalContext().log("Processing PreRenderViewEvent");
    }
}
