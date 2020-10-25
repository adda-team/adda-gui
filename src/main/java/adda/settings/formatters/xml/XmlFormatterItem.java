package adda.settings.formatters.xml;

import adda.settings.formatters.IFormatterItem;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class XmlFormatterItem implements IFormatterItem {

    protected Node element;

    public XmlFormatterItem(Node element) {
        this.element = element;
    }

    @Override
    public void appendChild(IFormatterItem child) {
        if (child instanceof XmlFormatterItem) {
            element.appendChild(((XmlFormatterItem) child).getAssignedXmlElement());
        }
    }

    @Override
    public List<IFormatterItem> getChildren() {
        List<IFormatterItem> list = new ArrayList<>();
        NodeList nl = element.getChildNodes();
        if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    list.add(new XmlFormatterItem(nl.item(i)));
                }
            }
        }
        return list;
    }

    @Override
    public String getName() {
        return element.getNodeName();
    }

    @Override
    public String getValue() {
        return element.getFirstChild().getNodeValue();
    }

    @Override
    public IFormatterItem getParent() {
        return new XmlFormatterItem(element.getParentNode());
    }

    @Override
    public void setArrayAttribute(boolean isArray) {

    }

    @Override
    public void setValue(String value) {
        //element.getFirstChild().setNodeValue(value);
        element.setTextContent(value);
    }

    @Override
    public boolean isArray() {
        return false;
    }


    public Node getAssignedXmlElement() {
        return element;
    }
}
