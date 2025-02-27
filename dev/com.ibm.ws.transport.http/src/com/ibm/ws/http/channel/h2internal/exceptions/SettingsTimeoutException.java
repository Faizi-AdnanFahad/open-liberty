/*******************************************************************************
 * Copyright (c) 1997, 2017 IBM Corporation and others.
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
package com.ibm.ws.http.channel.h2internal.exceptions;

import com.ibm.ws.http.channel.h2internal.Constants;

/**
 *
 */
public class SettingsTimeoutException extends Http2Exception {

    private static final long serialVersionUID = -6055280305221054576L;

    int errorCode = Constants.SETTINGS_TIMEOUT;
    String errorString = "SETTINGS_TIMEOUT";

    public SettingsTimeoutException(String s) {
        super(s);
    }

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorString() {
        return errorString;
    }
}
