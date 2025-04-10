package wales.nhs.dhcw.inthub.wpasHl7;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.util.EncodedMessageComparator;
import ca.uhn.hl7v2.util.XMLUtils;
import org.junit.jupiter.api.Assertions;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.util.HashSet;

public class HL7Assertions {

    static void assertMatchingExpectedMessage(String expected, AbstractMessage actual)
            throws SAXException, HL7Exception {

        Assertions.assertEquals(
                cleanExpectedMessage(expected),
                EncodedMessageComparator.standardizeXML(encodeHl7Xml(actual))
        );
    }

    private static String cleanExpectedMessage(String messageXml) {
        var doc =  XMLUtils.parse(messageXml);

        stripNamespacePrefixes(doc.getDocumentElement());
        removeEmptyElements(doc.getDocumentElement());
        removeReceivingAppAndFacility(doc.getDocumentElement());
        removeAdditionalNamespaces(doc.getDocumentElement());

        return XMLUtils.serialize(doc, true);
    }

    private static void removeReceivingAppAndFacility(Element documentElement) {
        removeElementByName(documentElement, "MSH.5");
        removeElementByName(documentElement, "MSH.6");
    }

    private static void removeElementByName(Element documentElement, String name) {
        var elements = documentElement.getElementsByTagName(name);
        for (int i = 0; i < elements.getLength(); i++) {
            var element = elements.item(i);
            var parent = element.getParentNode();
            parent.removeChild(element);
        }
    }

    private static void removeEmptyElements(Element element) {
        reviewDescendants(element);
        removeEmptyChildren(element);
    }

    private static void removeEmptyChildren(Element element) {
        var children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            var child = children.item(i);
            if (child instanceof Element childElement) {
                if (isEmptyElement(childElement)) {
                    try {
                        element.removeChild(childElement);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            }
        }
    }

    private static void reviewDescendants(Element element) {
        var children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            var child = children.item(i);
            if (child instanceof Element childElement) {
                removeEmptyElements((childElement));
            }
        }
    }

    private static boolean isEmptyElement(Element element) {
        var children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            var child = children.item(i);
            switch (child.getNodeType()) {
                case Node.ELEMENT_NODE,
                     Node.CDATA_SECTION_NODE,
                     Node.ENTITY_NODE,
                     Node.ENTITY_REFERENCE_NODE:
                    return false;
                case Node.TEXT_NODE:
                    if (!child.getTextContent().isBlank())
                        return false;
            }
        }
        return true;
    }


    private static void stripNamespacePrefixes(Element element) {
        element.setPrefix("");
        var elements = element.getElementsByTagName("*");
        for (int i = 0; i < elements.getLength(); i++) {
            stripNamespacePrefixes((Element) elements.item(i));
        }
    }

    private static void removeAdditionalNamespaces(Element element) {
        var attributes = element.getAttributes();
        var attrNames = new HashSet<String>();
        for (int i = 0; i < attributes.getLength(); i++) {
            attrNames.add(attributes.item(i).getNodeName());
        }
        for (String attrName : attrNames) {
            if (attrName.startsWith("xmlns:")) {
                element.removeAttribute(attrName);
            }
        }
    }

    private static String encodeHl7Xml(AbstractMessage result) throws HL7Exception {
        HapiContext context = new DefaultHapiContext();
        Parser parser = context.getXMLParser();
        return parser.encode(result);
    }
}
