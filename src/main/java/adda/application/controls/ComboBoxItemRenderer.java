package adda.application.controls;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;

public class ComboBoxItemRenderer extends BasicComboBoxRenderer {
    public Component getListCellRendererComponent(
            JList list, Object value, int index,
            boolean isSelected, boolean cellHasFocus)
    {
        super.getListCellRendererComponent(list, value, index,
                isSelected, cellHasFocus);

        if (value != null || index == -1)
        {
            ComboBoxItem item = (ComboBoxItem)value;
            setText( "" + item.getDescription() );
        }
        return this;
    }
}
