/*******************************************************************************
 * Copyright (c) 2022 IBM Corporation and others.
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
package basic.war.config;

import java.util.HashSet;
import java.util.Set;

import jakarta.websocket.Endpoint;
import jakarta.websocket.server.ServerApplicationConfig;
import jakarta.websocket.server.ServerEndpointConfig;

public class ServerUserPropertiesEndPointSetup implements ServerApplicationConfig {

    @Override
    public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> arg0) {
        return arg0;
    }

    @Override
    public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> arg0) {
        Set<ServerEndpointConfig> configs = new HashSet<ServerEndpointConfig>();

        configs.add(new UserPropertiesServerEndpointConfig());
       
        return configs;
    }

}