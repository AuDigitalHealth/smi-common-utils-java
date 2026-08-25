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

import java.io.InputStream;
import java.io.Reader;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

public abstract class SimpleXsdResourceResolver implements LSResourceResolver {

    @Override
    public LSInput resolveResource(String type, String namespaceURI,
                                   String publicId, String systemId, String baseURI) {
        if (!ArgumentUtils.isNullOrBlank(systemId)) {
            String refXsdContents = resolve(namespaceURI, systemId);

            if (refXsdContents != null) {
                // Create LSInput record
                ResolveRecord record = new ResolveRecord();
                record.setBaseURI(baseURI);
                record.setPublicId(publicId);
                record.setSystemId(systemId);
                record.setStringData(refXsdContents);
                return record;
            }
        }
        return null;
    }

    protected abstract String resolve(String namespaceURI, String systemId);

    private class ResolveRecord implements LSInput {

        private Reader characterStream;

        private InputStream byteStream;

        private String stringData;

        private String systemId;

        private String publicId;

        private String baseURI;

        private String encoding;

        private boolean certifiedText;

        public Reader getCharacterStream() {
            return this.characterStream;
        }

        public void setCharacterStream(Reader characterStream) {
            this.characterStream = characterStream;
        }

        public InputStream getByteStream() {
            return this.byteStream;
        }

        public void setByteStream(InputStream byteStream) {
            this.byteStream = byteStream;
        }

        public String getStringData() {
            return this.stringData;
        }

        public void setStringData(String stringData) {
            this.stringData = stringData;
        }

        public String getSystemId() {
            return this.systemId;
        }

        public void setSystemId(String systemId) {
            this.systemId = systemId;
        }

        public String getPublicId() {
            return this.publicId;
        }

        public void setPublicId(String publicId) {
            this.publicId = publicId;
        }

        public String getBaseURI() {
            return this.baseURI;
        }

        public void setBaseURI(String baseURI) {
            this.baseURI = baseURI;
        }

        public String getEncoding() {
            return this.encoding;
        }

        public void setEncoding(String encoding) {
            this.encoding = encoding;
        }

        public boolean getCertifiedText() {
            return this.certifiedText;
        }

        public void setCertifiedText(boolean certifiedText) {
            this.certifiedText = certifiedText;
        }

    }
}
