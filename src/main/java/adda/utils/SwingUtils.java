package adda.utils;

import adda.application.controls.JNumericField;
import adda.base.boxes.IBox;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class SwingUtils {

    private static void setComponentEnabled(JComponent panel, Boolean isEnabled) {
        panel.setEnabled(isEnabled);
        Component[] components  = panel.getComponents();

        if (null == components) {
            return;
        }

        for (Component component : components) {
            if (component instanceof JPanel) {
                setComponentEnabled((JPanel) component, isEnabled);
            } else if (component instanceof JScrollPane) {
                setComponentEnabled(((JScrollPane) component).getViewport(), isEnabled);
            } else if (component instanceof JTable) {
                JTable table = (JTable) component;
                Vector<Vector> data = ((DefaultTableModel) table.getModel()).getDataVector();
                for (int i = 0; i < data.size(); i++) {
                    Object value = table.getModel().getValueAt(i, 2);//button in third column
                    table.getCellRenderer(i, 2).getTableCellRendererComponent(table, value, false, false, i, 2).setEnabled(isEnabled);//button in third column
                }

            } else if (component.getClass().equals(JComboBox.class)
                    || component.getClass().equals(JCheckBox.class)
                    || component.getClass().equals(JTextField.class)
                    || component.getClass().equals(JSpinner.class)
                    || component.getClass().equals(JNumericField.class)
                    || component.getClass().equals(JButton.class)
            ) {
                component.setEnabled(isEnabled);
            }
        }
    }

    public static void setBoxEnabled(IBox box, Boolean isEnabled) {
        setComponentEnabled(box.getLayout(), isEnabled);

        if (null == box.getChildren()) return;

        for (IBox child : box.getChildren()) {
            setBoxEnabled(child, isEnabled);
        }
    }
}
