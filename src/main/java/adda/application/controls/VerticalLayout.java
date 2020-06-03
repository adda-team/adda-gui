package adda.application.controls;

import java.awt.*;

public class VerticalLayout implements LayoutManager {
    private Dimension size = new Dimension();

    private boolean isHeightOnly = false;

    public VerticalLayout() {
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
        int currentY = 5;
        for (int i = 0; i < list.length; i++) {

            Dimension pref = list[i].getPreferredSize();
            list[i].setBounds(5, currentY, isHeightOnly ? container.getWidth() - 10 : pref.width, pref.height);

            currentY += 5;
            currentY += pref.height;
        }
    }

    private Dimension calculateBestSize(Container c) {
        Component[] list = c.getComponents();
        size.width = c.getWidth() - 5;
        if (!isHeightOnly) {
            int maxWidth = 0;
            for (int i = 0; i < list.length; i++) {
                int width = list[i].getWidth();
                if (width > maxWidth)
                    maxWidth = width;
            }

            size.width = maxWidth + 5;
        }

        int height = 0;
        for (int i = 0; i < list.length; i++) {
            height += 5;
//            height += list[i].getHeight();
            height += list[i].getPreferredSize() != null ? list[i].getPreferredSize().height : list[i].getHeight();
        }
        size.height = height;
        return size;
    }
}
