/*
 * Copyright 2009 NEHTA
 *
 * Licensed under the NEHTA Open Source (Apache) License; you may not use this
 * file except in compliance with the License. A copy of the License is in the
 * 'LICENSE.txt' file, which should be provided with this work.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package au.gov.nehta.common.utils;

import java.util.UUID;

/**
 * Utility class to manage URIs.
 */
public final class UriUtils {

    /**
     * Generate a random URI.
     *
     * @return A randomly generated URI.
     */
    public static String randomUri() {
        return "urn:uuid:" + UUID.randomUUID().toString();
    }
}
