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
package com.ibm.websphere.security.jwt;

public class JwtException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 9102992736215798635L;

    public JwtException(String msg) {
        super(msg);
    }

    public JwtException(String msg, Throwable e) {
        super(msg, e);
    }
}
