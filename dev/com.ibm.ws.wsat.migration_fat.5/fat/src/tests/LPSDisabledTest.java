/*******************************************************************************
 * Copyright (c) 2019, 2023 IBM Corporation and others.
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
package tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ibm.websphere.simplicity.ShrinkHelper;
import com.ibm.ws.transaction.fat.util.FATUtils;
import com.ibm.ws.wsat.fat.util.DBTestBase;

import componenttest.annotation.ExpectedFFDC;
import componenttest.annotation.Server;
import componenttest.custom.junit.runner.FATRunner;
import componenttest.topology.impl.LibertyServer;
import componenttest.topology.utils.HttpUtils;

@RunWith(FATRunner.class)
public class LPSDisabledTest extends DBTestBase {

	@Server("LPSDisabled")
	public static LibertyServer server;

	private static String BASE_URL;

	@BeforeClass
	public static void beforeTests() throws Exception {

		BASE_URL = "http://" + server.getHostname() + ":"
				+ server.getHttpDefaultPort();

		DBTestBase.initWSATTest(server);

		ShrinkHelper.defaultDropinApp(server, "LPSClient", "web.lpsclient.*");
		ShrinkHelper.defaultDropinApp(server, "LPSService", "web.lpsservice.*");

		server.setServerStartTimeout(START_TIMEOUT);
		FATUtils.startServers(server);
	}

	@AfterClass
    public static void tearDown() throws Exception {
		FATUtils.stopServers(new String[] {"WTRN0062E", "WTRN0063E"}, server);

		DBTestBase.cleanupWSATTest(server);
    }
	
	// LPS Disabled Test
	@Test
	public void testWSTXLPS001FVT() {
		callServlet("WSTXLPS001FVT");
	}
	
	@Test
	@ExpectedFFDC(value = { "javax.transaction.xa.XAException"})
	public void testWSTXLPS002FVT() {
		callServlet("WSTXLPS002FVT");
	}

	@Test
	@ExpectedFFDC(value = { "java.lang.IllegalStateException" })
	public void testWSTXLPS003FVT() {
		callServlet("WSTXLPS003FVT");
	}

	@Test
	@ExpectedFFDC(value = { "javax.transaction.RollbackException" })
	public void testWSTXLPS004FVT() {
		callServlet("WSTXLPS004FVT");
	}

	@Test
	@ExpectedFFDC(value = { "javax.transaction.RollbackException" })
	public void testWSTXLPS005FVT() {
		callServlet("WSTXLPS005FVT");
	}

	@Test
	public void testWSTXLPS006FVT() {
		callServlet("WSTXLPS006FVT");
	}

	@Test
	public void testWSTXLPS007FVT() {
		callServlet("WSTXLPS007FVT");
	}

	@Test
	public void testWSTXLPS008FVT() {
		callServlet("WSTXLPS008FVT");
	}

	@Test
	public void testWSTXLPS009FVT() {
		callServlet("WSTXLPS009FVT");
	}

	@Test
	public void testWSTXLPS010FVT() {
		callServlet("WSTXLPS010FVT");
	}
	
	@Test
	@ExpectedFFDC(value = { "javax.transaction.RollbackException", "java.lang.IllegalStateException" })
	public void testWSTXLPS011FVT() {
		callServlet("WSTXLPS011FVT");
	}
	
	private void callServlet(String testMethod){
		try {
			int testNumber = Integer.parseInt(testMethod.substring(7, 10));
			String providerURL = BASE_URL;
			String urlStr = BASE_URL + "/LPSClient/LPSClientServlet"
					+ "?method=" + testNumber + "&baseurl=" + providerURL;
			System.out.println(testMethod + " URL: " + urlStr);
            HttpURLConnection con = getHttpConnection(new URL(urlStr), 
            		HttpURLConnection.HTTP_OK, REQUEST_TIMEOUT);
            BufferedReader br = HttpUtils.getConnectionStream(con);
            String result = br.readLine();
            assertNotNull(result);
			System.out.println(testMethod + " Result : " + result);
			assertTrue("Cannot get expected reply from server, result = '" + result + "'",
					result.contains("Test passed"));
		} catch (Exception e) {
			fail("Exception happens: " + e.toString());
		}
	}
} 
