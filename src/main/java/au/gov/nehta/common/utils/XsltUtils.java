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

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public final class XsltUtils {

    /**
     * Runs an XSLT transformation returning the result as a DOM document. This
     * method simplifies the call to the method: {@code
     * javax.xml.transform.Transformer#transform(Source, Result)}.
     *
     * @param xsltNode  DOM node representing the XSLT stylesheet. Cannot be null.
     * @param inputNode DOM node containing the input XML to be transformed. Cannot be
     *                  null.
     * @return Resulting DOM document.
     * @throws TransformerException Thrown when there are errors running the XSLT transformation.
     */
    public static Document transformToDom(Node xsltNode, Node inputNode)
            throws TransformerException {
        return transformToDom(xsltNode, inputNode, null, null);
    }

    /**
     * Runs an XSLT transformation returning the result as a DOM document. This
     * method simplifies the call to the method: {@code
     * javax.xml.transform.Transformer#transform(Source, Result)}.
     *
     * @param xsltNode   DOM node representing the XSLT stylesheet. Cannot be null.
     * @param inputNode  DOM node containing the input XML to be transformed. Cannot be
     *                   null.
     * @param parameters Map of parameter names to values to be passed to the XSLT
     *                   stylesheet.
     * @return Resulting DOM document.
     * @throws TransformerException Thrown when there are errors running the XSLT transformation.
     */
    public static Document transformToDom(Node xsltNode, Node inputNode,
                                          Map<String, String> parameters) throws TransformerException {
        return transformToDom(xsltNode, inputNode, parameters, null);
    }

    /**
     * Runs an XSLT transformation returning the result as a DOM document. This
     * method simplifies the call to the method: {@code
     * javax.xml.transform.Transformer#transform(Source, Result)}.
     *
     * @param xsltNode    DOM node representing the XSLT stylesheet. Cannot be null.
     * @param inputNode   DOM node containing the input XML to be transformed. Cannot be
     *                    null.
     * @param parameters  Map of parameter names to values to be passed to the XSLT
     *                    stylesheet.
     * @param uriResolver Resolves imports, include and other references in the XSLT
     *                    stylesheet.
     * @return Resulting DOM document.
     * @throws TransformerException Thrown when there are errors running the XSLT transformation.
     */
    public static Document transformToDom(Node xsltNode, Node inputNode,
                                          Map<String, String> parameters, URIResolver uriResolver)
            throws TransformerException {
        assert (xsltNode != null) : "'xsltNode' is null.";
        assert (inputNode != null) : "'inputNode' is null.";

        // Create source and result objects
        Source xsltSource = new DOMSource(xsltNode);
        Source inputSource = new DOMSource(inputNode);
        DOMResult result = new DOMResult();

        // Run transformation
        transform(xsltSource, inputSource, result, parameters, uriResolver);

        // Return DOM document
        return (Document) result.getNode();
    }

    /**
     * Runs an XSLT transformation returning the result as a DOM document. This
     * method simplifies the call to the method: {@code
     * javax.xml.transform.Transformer#transform(Source, Result)}.
     *
     * @param xsltNode  DOM node representing the XSLT stylesheet. Cannot be null.
     * @param inputNode DOM node containing the input XML to be transformed. Cannot be
     *                  null.
     * @return Resulting DOM document.
     * @throws TransformerException Thrown when there are errors running the XSLT transformation.
     */
    public static String transformToString(Node xsltNode, Node inputNode)
            throws TransformerException {
        return transformToString(xsltNode, inputNode, null,
                null);
    }

    /**
     * Runs an XSLT transformation returning the result as a DOM document. This
     * method simplifies the call to the method: {@code
     * javax.xml.transform.Transformer#transform(Source, Result)}.
     *
     * @param xsltNode   DOM node representing the XSLT stylesheet. Cannot be null.
     * @param inputNode  DOM node containing the input XML to be transformed. Cannot be
     *                   null.
     * @param parameters Map of parameter names to values to be passed to the XSLT
     *                   stylesheet.
     * @return Resulting DOM document.
     * @throws TransformerException Thrown when there are errors running the XSLT transformation.
     */
    public static String transformToString(Node xsltNode, Node inputNode,
                                           Map<String, String> parameters) throws TransformerException {
        return transformToString(xsltNode, inputNode, parameters, null);
    }

    /**
     * Runs an XSLT transformation returning the result as a DOM document. This
     * method simplifies the call to the method: {@code
     * javax.xml.transform.Transformer#transform(Source, Result)}.
     *
     * @param xsltNode    DOM node representing the XSLT stylesheet. Cannot be null.
     * @param inputNode   DOM node containing the input XML to be transformed. Cannot be
     *                    null.
     * @param parameters  Map of parameter names to values to be passed to the XSLT
     *                    stylesheet.
     * @param uriResolver Resolves imports, include and other references in the XSLT
     *                    stylesheet.
     * @return Resulting DOM document.
     * @throws TransformerException Thrown when there are errors running the XSLT transformation.
     */
    public static String transformToString(Node xsltNode, Node inputNode,
                                           Map<String, String> parameters, URIResolver uriResolver)
            throws TransformerException {
        assert (xsltNode != null) : "'xsltNode' is null.";
        assert (inputNode != null) : "'inputNode' is null.";

        // Create source and result objects
        Source xsltSource = new DOMSource(xsltNode);
        Source inputSource = new DOMSource(inputNode);
        StringWriter output = new StringWriter();
        StreamResult result = new StreamResult(output);

        // Run transformation
        transform(xsltSource, inputSource, result, parameters, uriResolver);

        // Return string
        output.flush();
        return output.toString();
    }

    /**
     * Runs an XSLT transformation writing the results out to a character stream.
     * This method simplifies the call to the method: {@code
     * javax.xml.transform.Transformer#transform(Source, Result)}.
     *
     * @param xsltNode  DOM node representing the XSLT stylesheet. Cannot be null.
     * @param inputNode DOM node containing the input XML to be transformed. Cannot be
     *                  null.
     * @param output    Character stream to write to. Cannot be null.
     * @throws TransformerException Thrown when there are errors running the XSLT transformation.
     * @throws IOException          Thrown when there are errors writing to the output character
     *                              stream.
     */
    public static void transform(Node xsltNode, Node inputNode, Writer output)
            throws TransformerException, IOException {
        transform(xsltNode, inputNode, output, null, null);
    }

    /**
     * Runs an XSLT transformation writing the results out to a character stream.
     * This method simplifies the call to the method: {@code
     * javax.xml.transform.Transformer#transform(Source, Result)}.
     *
     * @param xsltNode   DOM node representing the XSLT stylesheet. Cannot be null.
     * @param inputNode  DOM node containing the input XML to be transformed. Cannot be
     *                   null.
     * @param output     Character stream to write to. Cannot be null.
     * @param parameters Map of parameter names to values to be passed to the XSLT
     *                   stylesheet.
     * @throws TransformerException Thrown when there are errors running the XSLT transformation.
     * @throws IOException          Thrown when there are errors writing to the output character
     *                              stream.
     */
    public static void transform(Node xsltNode, Node inputNode, Writer output,
                                 Map<String, String> parameters) throws TransformerException, IOException {
        transform(xsltNode, inputNode, output, parameters, null);
    }

    /**
     * Runs an XSLT transformation writing the results out to a character stream.
     * This method simplifies the call to the method: {@code
     * javax.xml.transform.Transformer#transform(Source, Result)}.
     *
     * @param xsltNode    DOM node representing the XSLT stylesheet. Cannot be null.
     * @param inputNode   DOM node containing the input XML to be transformed. Cannot be
     *                    null.
     * @param output      Character stream to write to. Cannot be null.
     * @param parameters  Map of parameter names to values to be passed to the XSLT
     *                    stylesheet.
     * @param uriResolver Resolves imports, include and other references in the XSLT
     *                    stylesheet.
     * @throws TransformerException Thrown when there are errors running the XSLT transformation.
     * @throws IOException          Thrown when there are errors writing to the output character
     *                              stream.
     */
    public static void transform(Node xsltNode, Node inputNode, Writer output,
                                 Map<String, String> parameters, URIResolver uriResolver)
            throws TransformerException, IOException {
        assert (xsltNode != null) : "'xsltNode' is null.";
        assert (inputNode != null) : "'inputNode' is null.";
        assert (output != null) : "'output' is null.";

        // Create source and result objects
        Source xsltSource = new DOMSource(xsltNode);
        Source inputSource = new DOMSource(inputNode);
        StreamResult result = new StreamResult(output);

        // Run transformation
        transform(xsltSource, inputSource, result, parameters, uriResolver);

        // Flush output stream
        output.flush();
    }

    /**
     * Runs XSLT transformation that takes in XSLT Source and Result objects as
     * parameters. This method simplifies the call to the method: {@code
     * javax.xml.transform.Transformer#transform(Source, Result)}.
     *
     * @param xsltSource   Source object containing the XSLT stylesheet. Cannot be null.
     * @param inputSource  Source object containing the XML data to be transformed. Cannot be
     *                     null.
     * @param outputResult Result object to write the results of the XSLT transformation to.
     *                     Cannot be null.
     * @param parameters   Map of parameter names to values to be passed to the XSLT
     *                     stylesheet.
     * @param uriResolver  Resolves imports, include and other references in the XSLT
     *                     stylesheet.
     * @throws TransformerException Thrown when there are errors running the XSLT transformation.
     */
    public static void transform(Source xsltSource, Source inputSource,
                                 Result outputResult, Map<String, String> parameters,
                                 URIResolver uriResolver) throws TransformerException {
        assert (xsltSource != null) : "'xsltSource' is null.";
        assert (inputSource != null) : "'inputSource' is null.";
        assert (outputResult != null) : "'outputResult' is null.";

        // Create transformer
        Transformer transformer = createTransformer(xsltSource, uriResolver);

        // Set parameters
        if (parameters != null) {
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                transformer.setParameter(entry.getKey(), entry.getValue());
            }
        }

        // Do transformation
        transformer.transform(inputSource, outputResult);
    }

    /*
     * Creates the transformer
     */
    private static Transformer createTransformer(Source xsltSource,
                                                 URIResolver uriResolver) throws TransformerConfigurationException {
        TransformerFactory factory = TransformerFactory.newInstance();

        // Set URI resolver
        if (uriResolver != null) {
            factory.setURIResolver(uriResolver);
        }

        // Create transformer
        Transformer transformer;
        if (xsltSource == null) {
            transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        } else {
            transformer = factory.newTransformer(xsltSource);
        }
        return transformer;
    }

    /*
     * Private constructor to prevent instantiation of utility class.
     */
    private XsltUtils() {
    }
}
