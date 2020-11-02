package adda.application.controls;

import java.awt.*;

public class VerticalLayout implements LayoutManager {
    private Dimension size = new Dimension();

    private boolean isHeightOnly = false;

    private int margin = 5;

    public VerticalLayout() {
    }

    public VerticalLayout(int margin) {
        this.margin = margin;
    }


    public void addLayoutComponent(String name, Component comp) {
    }

    public void removeLayoutComponent(Component comp) {
    }


    public Dimension minimumLayoutSize(Container c) {
        return calculateBestSize(c);
    }

    public Dimension preferredLayoutSize(Container c) {
        return calculateBestSize(c);
    }


    public void layoutContainer(Container container) {

        Component list[] = container.getComponents();
        int currentY = margin;
        for (int i = 0; i < list.length; i++) {

            Dimension pref = list[i].getPreferredSize();
            list[i].setBounds(5, currentY, isHeightOnly ? container.getWidth() - 10 : pref.width, pref.height);

            currentY += margin;
            currentY += pref.height;
        }
    }

    private Dimension calculateBestSize(Container c) {
        Component[] list = c.getComponents();
        size.width = c.getWidth() - margin;
        if (!isHeightOnly) {
            int maxWidth = 0;
            for (int i = 0; i < list.length; i++) {
                int width = list[i].getWidth();
                if (width > maxWidth)
                    maxWidth = width;
            }

            size.width = maxWidth + margin;
        }

        int height = 0;
        for (int i = 0; i < list.length; i++) {
            height += margin;
//            height += list[i].getHeight();
            height += list[i].getPreferredSize() != null ? list[i].getPreferredSize().height : list[i].getHeight();
        }
        size.height = height;
        return size;
    }
}
