/*******************************************************************************
 * Copyright (c) 2006, 2020 IBM Corporation and others.
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

package com.ibm.ws.ejbcontainer.ejblink.ejbwar;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import com.ibm.ws.ejbcontainer.ejblink.ejb.AutoLinkLocal2OtherWar;
import com.ibm.ws.ejbcontainer.ejblink.ejb.BasicEjbLinkTest;
import com.ibm.ws.ejbcontainer.ejblink.ejb.EjbLinkDriverLocal;
import com.ibm.ws.ejbcontainer.ejblink.ejb.EjbLinkDriverRemote;

/**
 * Basic Stateless Bean implementation for testing AutoLink
 **/
@Stateless
@Remote(EjbLinkDriverRemote.class)
public class TestAutoLinkWar2OtherWar extends BasicEjbLinkTest implements EjbLinkDriverLocal {
    @EJB
    public AutoLinkLocal2OtherWar beanInOtherWarModuleTwice;

    public String verifyAmbiguousEJBReferenceException() {
        return "Failed";
    }

    public TestAutoLinkWar2OtherWar() {
        // intentionally blank
    }
}
