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

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;

/**
 * Utility class containing XML Schema-related functions.
 */
public final class XsdUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(XsdUtils.class);

    /**
     * Validates a data node against a schema. This method simplifies the call to
     * the method: {@code javax.xml.validation.Validator#validate(Source)}.
     *
     * @param dataFile   DOM data node to validate. Cannot be null.
     * @param schemaFile XML schema node to validate against. Cannot be null.
     * @throws SAXException                 Thrown when there are validation errors. Also thrown on parsing
     *                                      errors (e.g. from imports).
     * @throws IOException                  Thrown when there are I/O errors (e.g. from imports).
     * @throws ParserConfigurationException Thrown when couldn't create XML parser.
     */
    public static void validate(File dataFile, File schemaFile)
            throws SAXException, IOException, ParserConfigurationException {
        assert (dataFile != null) : "'dataFile' is null.";
        assert (schemaFile != null) : "'schemaFile' is null.";

        Node dataNode = DomUtils.parse(dataFile);
        Node schemaNode = DomUtils.parse(schemaFile);

        validate(dataNode, schemaNode, new FileXsdResourceResolver(
                schemaFile.getParentFile()));
    }

    /**
     * Validates a data node against a schema. This method simplifies the call to
     * the method: {@code javax.xml.validation.Validator#validate(Source)}.
     *
     * @param dataNode   DOM data node to validate. Cannot be null.
     * @param schemaNode XML schema node to validate against. Cannot be null.
     * @throws SAXException Thrown when there are validation errors. Also thrown on parsing
     *                      errors (e.g. from imports).
     * @throws IOException  Thrown when there are I/O errors (e.g. from imports).
     */
    public static void validate(Node dataNode, Node schemaNode)
            throws SAXException, IOException {
        validate(dataNode, schemaNode, null);
    }

    /**
     * Validates a data node against a schema. This method simplifies the call to
     * the method: {@code javax.xml.validation.Validator#validate(Source)}.
     *
     * @param dataNode            DOM data node to validate. Cannot be null.
     * @param schemaNode          XML schema node to validate against. Cannot be null.
     * @param xsdResourceResolver Resolves imports and include references in the schema.
     * @throws SAXException Thrown when there are validation errors. Also thrown on parsing
     *                      errors (e.g. from imports).
     * @throws IOException  Thrown when there are I/O errors (e.g. from imports).
     */
    public static void validate(Node dataNode, Node schemaNode,
                                LSResourceResolver xsdResourceResolver) throws SAXException, IOException {
        assert (dataNode != null) : "'dataNode' is null.";
        assert (schemaNode != null) : "'schemaNode' is null.";

        // Check if it is an XSD or WSDL

        // Get schema factory
        SchemaFactory schemaFactory = SchemaFactory
                .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        // Set resolver
        if (xsdResourceResolver != null) {
            schemaFactory.setResourceResolver(xsdResourceResolver);
        }

        // Create new schema
        Source xsdSource = new DOMSource(schemaNode);
        Schema schema = schemaFactory.newSchema(xsdSource);

        // Get validator
        Validator validator = schema.newValidator();

        // Validate
        Source dataSource = new DOMSource(dataNode);
        validator.validate(dataSource);
    }

    /*
     * Private constructor to prevent instantiation of utility class.
     */
    private XsdUtils() {
    }

    private static final class FileXsdResourceResolver extends SimpleXsdResourceResolver {

        private final File baseDir;

        private FileXsdResourceResolver(File baseDir) {
            this.baseDir = baseDir;
        }

        @Override
        protected String resolve(String namespaceURI, String systemId) {
            File refXsdFile = new File(this.baseDir, systemId);
            try {
                return IOUtils.read(refXsdFile);
            } catch (IOException e) {
                LOGGER.warn("Couldn't load file: " + refXsdFile + ".", e);
                return null;
            }
        }
    }
}
