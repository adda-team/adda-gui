package adda.settings.formatters.xml;

import adda.settings.formatters.IFormatter;
import adda.settings.formatters.IFormatterItem;
import org.apache.xml.serializer.OutputPropertiesFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

public class XmlFormatter implements IFormatter {

    protected Document document;


    @Override
    public IFormatterItem createFormatterItem(String name, String value) {
        Element element = document.createElement(name);
        element.appendChild(document.createTextNode(value));
        return new XmlFormatterItem(element);
    }

    @Override
    public IFormatterItem createFormatterItem(String name) {
        return new XmlFormatterItem(document.createElement(name));
    }

    @Override
    public IFormatterItem getRootFormatterItem() {
        return new XmlFormatterItem(document.getDocumentElement() == null ? document : document.getDocumentElement());
    }

    @Override
    public void parse(String str) {
        DocumentBuilderFactory dbf =
                DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        try {
            document = db.parse(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        document = builder.newDocument();
    }

    public String getFormattedText() {
        document.normalizeDocument();
        DOMSource domSource = new DOMSource(document);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();

        Transformer transformer = null;
        try {
            transformer = tf.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        try {
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputPropertiesFactory.S_KEY_INDENT_AMOUNT, "2");

            transformer.transform(domSource, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return writer.toString();
    }
}
