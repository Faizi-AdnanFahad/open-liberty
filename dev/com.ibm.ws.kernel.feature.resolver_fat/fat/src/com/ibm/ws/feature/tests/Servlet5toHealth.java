/*******************************************************************************
 * Copyright (c) 2023 IBM Corporation and others.
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
package com.ibm.ws.feature.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import componenttest.annotation.Server;
import componenttest.custom.junit.runner.FATRunner;
import componenttest.custom.junit.runner.Mode;
import componenttest.custom.junit.runner.Mode.TestMode;
import componenttest.rules.repeater.EERepeatActions;
import componenttest.rules.repeater.RepeatTests;
import componenttest.topology.impl.LibertyServer;
import componenttest.topology.impl.LibertyServerFactory;

@RunWith(FATRunner.class)
public class Servlet5toHealth {

    public static final String SERVER_NAME = "Servlet5toHealth";

    @Server(SERVER_NAME)
    public static LibertyServer server;

    @After
    public void tearDown() throws Exception {
        server.stopServer("CWWKF0001E");
    }

    @Test
    public void servlet5HealthMaxTest() throws Exception {
        String envVar = "mpHealth-4.0,mpHealth-3.1,mpHealth-3.0,mpHealth-2.2,mpHealth-2.1,mpHealth-2.0,mpHealth-1.0";

        //log out the method name "Entering methodName..."

        server = LibertyServerFactory.getLibertyServer(SERVER_NAME);
        server.addEnvVar("PREFERRED_FEATURE_VERSIONS", envVar);
        server.startServer();

        assertNotNull("Expected mpHealth-4.0 after features resolved but got: " + server.findStringsInLogs("CWWKF0012I: The server installed the following features:"), server.waitForStringInLog("mpHealth-4.0"));

        server.stopServer("CWWKF0001E");
    }

    @Test
    public void servlet5HealthMinTest() throws Exception {
        String envVar = "mpHealth-1.0,mpHealth-2.0,mpHealth-2.1,mpHealth-2.2,mpHealth-3.0,mpHealth-3.1,mpHealth-4.0";

        //log out the method name "Entering methodName..."

        server = LibertyServerFactory.getLibertyServer(SERVER_NAME);
        server.addEnvVar("PREFERRED_FEATURE_VERSIONS", envVar);
        server.startServer();

        assertNotNull("Expected mpHealth-4.0 after features resolved but got: " + server.findStringsInLogs("CWWKF0012I: The server installed the following features:"), server.waitForStringInLog("mpHealth-4.0"));

        server.stopServer("CWWKF0001E");
    }
}
