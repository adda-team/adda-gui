package adda.item.tab.shape.selector;

import javax.swing.*;

public class ShapeDomainInfo {
    protected String name;
    protected int order;
    protected ImageIcon icon;

    public ShapeDomainInfo(String name, int order, ImageIcon icon) {
        this.name = name;
        this.order = order;
        this.icon = icon;
    }

    public ShapeDomainInfo(String name, int order) {
        this(name, order, null);
    }


    public String getName() {
        return name;
    }

    public int getOrder() {
        return order;
    }

    public ImageIcon getIcon() {
        return icon;
    }
}
