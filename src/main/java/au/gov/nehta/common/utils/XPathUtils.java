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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Utility class containing XPath-related functions. These methods simplify the
 * call to the method: {@code javax.xml.xpath.XPath#evaluate(String, Object,
 * QName)}.
 */
public final class XPathUtils {

    /**
     * Evaluates an XPath expression over a DOM node and casts the result to a
     * Java type.
     *
     * @param <T>          Java type
     * @param xpath        XPath expression to evaluate. Cannot be null nor an empty string.
     * @param contextNode  DOM node to evaluate XPath over. Cannot be null.
     * @param namespaceMap Mapping of namespace prefixes in XPath expression to namespace
     *                     URIs.
     * @param expectedType The Java type to cast the result object to. Cannot be null.
     *                     Possible values are: java.lang.String.class (for a string value),
     *                     java.lang.Boolean.class (for a boolean value),
     *                     java.lang.Double.class (for a number value), org.w3c.dom.NodeList
     *                     (for a node set), org.w3c.dom.Node (for a node value) or
     *                     sub-classes of org.w3c.dom.Node (such as a DOM element).
     * @return Result object that the XPath expression evaluates to.
     * @throws XPathExpressionException Thrown when the XPath expression cannot be evaluated.
     */
    public static <T> T evaluate(String xpath, Node contextNode,
                                 Map<String, String> namespaceMap, Class<T> expectedType)
            throws XPathExpressionException {
        assert (xpath != null) : "'xpath' is null.";
        assert (!xpath.trim().isEmpty()) : "'xpath' is a blank string.";
        assert (contextNode != null) : "'contextNode' is null.";
        assert (expectedType != null) : "'expectedType' is null.";

        // Get return type
        QName returnType;
        if (expectedType == String.class) {
            returnType = XPathConstants.STRING;
        } else if (expectedType == Boolean.class) {
            returnType = XPathConstants.BOOLEAN;
        } else if (expectedType == Double.class) {
            returnType = XPathConstants.NUMBER;
        } else if (Node.class.isAssignableFrom(expectedType)) {
            returnType = XPathConstants.NODE;
        } else if (expectedType == NodeList.class) {
            returnType = XPathConstants.NODESET;
        } else {
            throw new IllegalArgumentException(
                    "Unknown expected type for result of XPath expression: "
                            + expectedType.getName() + ".");
        }

        // Evaluate expression
        Object result = evaluate(xpath, contextNode, namespaceMap, returnType);

        // Cast the result
        return expectedType.cast(result);
    }

    /**
     * Evaluates an XPath expression over a DOM node.
     *
     * @param xpath        XPath expression to evaluate. Cannot be null nor an empty string.
     * @param contextNode  DOM node to evaluate XPath over. Cannot be null.
     * @param namespaceMap Mapping of namespace prefixes in XPath expression to namespace
     *                     URIs.
     * @param returnType   Expected type of result object. Cannot be null. The value must
     *                     come from the {@code XPathConstants} class.
     * @return Result object that the XPath expression evaluates to.
     * @throws XPathExpressionException Thrown when the XPath expression cannot be evaluated.
     */
    public static Object evaluate(String xpath, Node contextNode,
                                  Map<String, String> namespaceMap, QName returnType)
            throws XPathExpressionException {
        assert (xpath != null) : "'xpath' is null.";
        assert (!xpath.trim().isEmpty()) : "'xpath' is a blank string.";
        assert (contextNode != null) : "'contextNode' is null.";
        assert (returnType != null) : "'returnType' is null.";

        // Create a XPath object
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpathObj = factory.newXPath();

        // Set namespace context
        if (namespaceMap != null) {
            xpathObj.setNamespaceContext(new MappedNamespaceContext(namespaceMap));
        }

        // Evaluate expression
        return xpathObj.evaluate(xpath, contextNode, returnType);
    }

    /**
     * Evaluates an XPath expression over a DOM node and returns the count of DOM
     * nodes that the expression evaluated to.
     *
     * @param xpath        XPath expression to evaluate. Cannot be null nor an empty string.
     * @param contextNode  DOM node to evaluate XPath over. Cannot be null.
     * @param namespaceMap Mapping of namespace prefixes in XPath expression to namespace
     *                     URIs.
     * @return Count of the DOM nodes that the XPath expression evaluates to.
     * @throws XPathExpressionException Thrown when the XPath expression cannot be evaluated.
     */
    public static int getCount(String xpath, Node contextNode,
                               Map<String, String> namespaceMap) throws XPathExpressionException {
        assert (xpath != null) : "'xpath' is null.";
        assert (!xpath.trim().isEmpty()) : "'xpath' is a blank string.";
        assert (contextNode != null) : "'contextNode' is null.";

        Double doubleCount = evaluate("count(" + xpath + ")", contextNode,
                namespaceMap, Double.class);
        return doubleCount.intValue();
    }

    /**
     * Evaluates an XPath expression to a list of Elements.
     *
     * @param xpath        XPath expression to evaluate. Cannot be null nor an empty string.
     * @param contextNode  DOM node to evaluate XPath over. Cannot be null.
     * @param namespaceMap Mapping of namespace prefixes in XPath expression to namespace
     *                     URIs.
     * @return List of elements the XPath expression evaluated to, or an empty
     * list if none matching.
     * @throws XPathExpressionException Thrown when the XPath expression cannot be evaluated.
     */
    public static List<Element> getElementList(String xpath, Node contextNode,
                                               Map<String, String> namespaceMap) throws XPathExpressionException {
        assert (xpath != null) : "'xpath' is null.";
        assert (!xpath.trim().isEmpty()) : "'xpath' is a blank string.";
        assert (contextNode != null) : "'contextNode' is null.";

        NodeList nodeList = evaluate(xpath, contextNode, namespaceMap,
                NodeList.class);
        List<Element> elementList = new ArrayList<>();
        if (nodeList != null) {
            for (int idx = 0; idx < nodeList.getLength(); idx++) {
                Node node = nodeList.item(idx);
                if (node instanceof Element) {
                    elementList.add((Element) node);
                }
            }
        }
        return elementList;
    }

    /*
     * Private constructor to prevent instantiation of utility class.
     */
    private XPathUtils() {
    }
}
