/*******************************************************************************
 * Copyright (c) 2018, 2022 IBM Corporation and others.
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
package io.openliberty.microprofile.metrics.internal.monitor_fat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ibm.websphere.simplicity.log.Log;

import componenttest.annotation.AllowedFFDC;
import componenttest.annotation.Server;
import componenttest.custom.junit.runner.FATRunner;
import componenttest.topology.impl.LibertyServer;

@RunWith(FATRunner.class)
@AllowedFFDC("javax.management.InstanceNotFoundException")
public class MetricsMonitorTest {

    private static Class<?> c = MetricsMonitorTest.class;

    @Server("MetricsMonitorServer")
    public static LibertyServer server;

    @BeforeClass
    public static void setUp() throws Exception {
        trustAll();
    }

    private static void trustAll() throws Exception {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[] { new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            } }, new SecureRandom());
            SSLContext.setDefault(sslContext);
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        } catch (Exception e) {
            Log.error(c, "trustAll", e);
        }
    }

    @After
    public void tearDown() throws Exception {
        if (server != null && server.isStarted()) {
            server.stopServer("CWWKS4000E", "CWWKZ0014W");
            server.removeAllInstalledAppsForValidation();
        }
    }

    @Test
    public void testVendorMetricsPresent() throws Exception {

        String testName = "testVendorMetricsPresent";

        Log.info(c, testName,
                "------- Enable mpMetrics-5.0 vendor metrics should be available ------");
        server.startServer();
        Assert.assertNotNull("LTPA keys are not created/ready within timeout period of " + 60000 + "ms.",
                server.waitForStringInLog("CWWKS4104A.*|CWWKS4105I.*", 60000));
        Assert.assertNotNull("CWWKO0219I NOT FOUND", server.waitForStringInLog("defaultHttpEndpoint-ssl", 60000));
        Log.info(c, testName, "------- server started -----");
        Assert.assertNotNull("CWWKT0016I NOT FOUND", server.waitForStringInLogUsingMark("CWWKT0016I"));

        Assert.assertNotNull("No CWPMI2003I message", server.waitForStringInLogUsingMark("CWPMI2003I"));
        
        checkStrings(getHttpsServlet("/metrics"), new String[] { "mp_scope=\"base\"", "p_scope=\"vendor\"" }, new String[] {});

        server.setMarkToEndOfLog();
        Log.info(c, testName, "------- Remove mpMetrics-5.0: no metrics should be available ------");

        server.setServerConfigurationFile("server_monitorOnly.xml");
        Assert.assertNotNull("CWWKF0008I NOT FOUND", server.waitForStringInLogUsingMark("CWWKF0008I"));
       
        //Check for Trace output from MetricsConfig indicating that the WAB is unregistered (this would imply /metrics no longer active)
        Assert.assertNotNull(server.waitForStringInTrace("io.openliberty.microprofile.metrics50.internal.MetricsConfig 1 Unregistered web app bundle"));
        
        //For additional checking, check that we will get an ConnectException when trying to connect to /metrics
        String exceptionString = null;
        try {
            getHttpsServlet("/metrics");
        } catch (ConnectException exception) {
            exceptionString = exception.toString();
            
        }
        Assert.assertNotNull(exceptionString);
    }

    private String getHttpServlet(String servletPath) throws Exception {
        HttpURLConnection con = null;
        try {
            String sURL = "http://" + server.getHostname() + ":" + server.getHttpDefaultPort() + servletPath;
            URL checkerServletURL = new URL(sURL);
            con = (HttpURLConnection) checkerServletURL.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestMethod("GET");

            String sep = System.getProperty("line.separator");
            String line = null;
            StringBuilder lines = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

            while ((line = br.readLine()) != null && line.length() > 0) {
                lines.append(line).append(sep);
            }
            Log.info(c, "getHttpServlet", sURL);
            return lines.toString();
        } finally {
            if (con != null)
                con.disconnect();
        }
    }

    private String getHttpsServlet(String servletPath) throws Exception {
        HttpsURLConnection con = null;
        try {
            String sURL = "https://" + server.getHostname() + ":" + server.getHttpDefaultSecurePort() + servletPath;
            URL checkerServletURL = new URL(sURL);
            con = (HttpsURLConnection) checkerServletURL.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
            String authorization = "Basic "
                    + Base64.getEncoder().encodeToString(("theUser:thePassword").getBytes(StandardCharsets.UTF_8)); // Java
                                                                                                                    // 8
            con.setRequestProperty("Authorization", authorization);
            con.setRequestProperty("Accept", "text/plain");
            con.setRequestMethod("GET");


            String sep = System.getProperty("line.separator");
            String line = null;
            StringBuilder lines = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

            while ((line = br.readLine()) != null && line.length() > 0) {
                if (!line.startsWith("#"))
                    lines.append(line).append(sep);
            }
            Log.info(c, "getHttpsServlet", sURL);
            return lines.toString();
        } finally {
            if (con != null)
                con.disconnect();
        }
    }

    private void checkStrings(String metricsText, String[] expectedString, String[] unexpectedString) {
        for (String m : expectedString) {
            if (!metricsText.contains(m)) {
                Log.info(c, "checkStrings", "Failed:\n" + metricsText);
                Assert.fail("Did not contain string: " + m);
            }
        }
        for (String m : unexpectedString) {
            if (metricsText.contains(m)) {
                Log.info(c, "checkStrings", "Failed:\n" + metricsText);
                Assert.fail("Contained string: " + m);
            }
        }
    }
}
