/*******************************************************************************
 * Copyright (c) 2022, 2023 IBM Corporation and others.
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
package componenttest.rules.repeater;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ibm.websphere.simplicity.OperatingSystem;
import com.ibm.websphere.simplicity.log.Log;

import componenttest.common.apiservices.LocalMachine;
import componenttest.rules.repeater.RepeatActions.EEVersion;

/**
 *
 */
public class FeatureUtilities {

    private static final Class<?> c = FeatureUtilities.class;

    private static final boolean isZOS;

    static {
        boolean zos = false;
        try {
            zos = LocalMachine.getInstance().getOperatingSystem() == OperatingSystem.ZOS;
        } catch (Exception e) {
            Log.error(c, "<clinit>", e,
                      "Could not detect OS, assume it is not z/OS.");
        }
        isZOS = zos;
    }

    /**
     * Returns the set of all public Java/Jakarta EE feature short names
     *
     * @return the set of short names
     */
    public static Set<String> allEeFeatures(boolean openLibertyOnly) {
        Set<String> features = new HashSet<>();
        if (!openLibertyOnly) {
            features.addAll(EE6FeatureReplacementAction.EE6_FEATURE_SET);
        }
        features.addAll(EE7FeatureReplacementAction.EE7_FEATURE_SET);
        features.addAll(EE8FeatureReplacementAction.EE8_FEATURE_SET);
        features.addAll(JakartaEE9Action.EE9_FEATURE_SET);
        features.addAll(JakartaEE10Action.EE10_FEATURE_SET);
        features.addAll(JakartaEE11Action.EE11_FEATURE_SET);

        // EE-related features which aren't in one of the feature sets
        features.add("appSecurity-1.0");
        features.add("data-1.1");
        features.add("jsp-2.2");
        features.add("websocket-1.0");

        return features;
    }

    /**
     * Returns the set of all public MicroProfile feature short names
     *
     * @return the set of short names
     */
    public static Set<String> allMpFeatures() {
        return Stream.concat(MicroProfileActions.ALL.stream(),
                             MicroProfileActions.STANDALONE_ALL.stream())
                        .flatMap(s -> s.getFeatures().stream())
                        .collect(Collectors.toSet());
    }

    /**
     * Returns the set of public MicroProfile features which are compatible with a given Java/Jakarta EE version
     *
     * @param  eeVersion the EE version
     * @return           the set of compatible MP feature short names
     */
    public static Set<String> compatibleMpFeatures(EEVersion eeVersion) {
        return Stream.concat(MicroProfileActions.ALL.stream(),
                             MicroProfileActions.STANDALONE_ALL.stream())
                        .filter(s -> s.getEEVersion() == eeVersion)
                        .flatMap(s -> s.getFeatures().stream())
                        .collect(Collectors.toSet());
    }

    /**
     * Returns the set of public test features
     *
     * @return the set of short names of public test features
     */
    public static Set<String> allTestFeatures() {
        Set<String> testFeatures = new HashSet<>();
        testFeatures.add("componenttest-1.0");
        testFeatures.add("componenttest-2.0");
        testFeatures.add("txtest-1.0");
        testFeatures.add("txtest-2.0");
        testFeatures.add("ejbTest-1.0");
        testFeatures.add("ejbTest-2.0");
        testFeatures.add("enterpriseBeansTest-2.0");
        return testFeatures;
    }

    /**
     * Get the set of public feature short names by reading the feature files from a liberty install
     *
     * @param  libertyInstallRoot the wlp directory containing the liberty install
     * @return                    the list of public short names
     */
    public static Set<String> getFeaturesFromServer(File libertyInstallRoot, boolean openLibertyOnly) {
        try {
            File featureDir = new File(libertyInstallRoot, "lib/features");
            Set<String> features = new HashSet<>();
            // If there was a problem building projects before this test runs, "lib/features" won't exist
            if (featureDir != null && featureDir.exists()) {
                for (File feature : featureDir.listFiles()) {
                    if (feature.getName().startsWith("io.openliberty.") ||
                        feature.getName().startsWith("com.ibm.")) {
                        String shortName = parseShortName(feature, openLibertyOnly);
                        if (shortName != null) {
                            features.add(shortName);
                        }
                    }
                }
            }
            return features;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the short name from a feature file
     *
     * @param  feature     the feature file
     * @return             the short name, or {@code null} if it could not be found
     * @throws IOException if there is a problem reading the feature file
     */
    private static String parseShortName(File feature, boolean openLibertyOnly) throws IOException {
        // Only scan *.mf files
        if (feature.isDirectory() || !feature.getName().endsWith(".mf"))
            return null;

        try (Scanner scanner = new Scanner(feature)) {
            String shortName = null;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("IBM-ShortName:")) {
                    shortName = line.substring("IBM-ShortName:".length()).trim();
                } else if (line.contains("IBM-Test-Feature:") && line.contains("true")) {
                    Log.info(c, "parseShortName", "Skipping test feature: " + feature.getName());
                    return null;
                } else if (line.startsWith("Subsystem-SymbolicName:") && !line.contains("visibility:=public")) {
                    Log.info(c, "parseShortName", "Skipping non-public feature: " + feature.getName());
                    return null;
                } else if (openLibertyOnly && line.startsWith("IBM-ProductID") && !line.contains("io.openliberty")) {
                    Log.info(c, "parseShortName", "Skipping non Open Liberty feature: " + feature.getName());
                    return null;
                }
            }
            // some test feature files do not have a short name and do not have IBM-Test-Feature set.
            // We do not want those ones.
            if (shortName != null) {
                if (!isZOS && (shortName.startsWith("zos") || shortName.startsWith("batchSMFLogging"))) {
                    Log.info(c, "parseShortName", "Skipping zos feature: " + feature.getName());
                    return null;
                }
                return shortName;
            }
        }
        return null;
    }

}
