/*
 * Copyright 2026 ADHA
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. A copy of the License
 * is in the LICENSE.txt file at the repository root.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package au.gov.nehta.common.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Locks this artifact on the Java 24 / 24.0.0 release line.
 */
public class ReleaseLineTest {

    @Test
    public void runtimeFeatureIsAtLeast24() {
        assertTrue(Runtime.version().feature() >= 24);
    }

    @Test
    public void mavenCompilerReleaseIs24() {
        assertEquals("24", System.getProperty("smi.common.utils.compiler.release"));
    }

    @Test
    public void projectVersionStartsWith24() {
        String version = System.getProperty("smi.common.utils.project.version");
        assertNotNull(version);
        assertTrue(version.startsWith("24."));
    }
}
