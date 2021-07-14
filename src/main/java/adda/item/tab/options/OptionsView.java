package adda.item.tab.options;

import adda.application.controls.ActionTableCellRenderer;
import adda.base.IAddaOption;
import adda.base.IAddaOptionsContainer;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.events.ModelPropertyChangeType;
import adda.base.models.IModel;
import adda.base.models.ModelBase;
import adda.base.views.IView;
import adda.item.tab.base.size.SizeEnum;
import adda.item.tab.base.size.SizeMeasureEnum;
import adda.item.tab.base.size.SizeModel;
import adda.utils.StringHelper;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.Vector;
import java.util.stream.Collectors;

public class OptionsView implements IView {

    public static final String DELIMITER = ", ";
    private Icon clearIcon;

    JTable table;

    JScrollPane scrollPane;

    JPanel panelOptions;

    JButton clearAllButton;

    public JButton getClearAllButton() {
        return clearAllButton;
    }

    @Override
    public void refresh() {

    }

    @Override
    public JComponent getRootComponent() {
        return panelOptions;
    }

    public JComponent getTable() {
        return table;
    }

    @Override
    public void initFromModel(IModel model) {
        IconFontSwing.register(FontAwesome.getIconFont());
        clearIcon = IconFontSwing.buildIcon(FontAwesome.TIMES_CIRCLE, 12, Color.DARK_GRAY);

        Object[] columnNames = {"Name", "Value", ""};

        OptionsModel optionsModel = (OptionsModel) model;

        DefaultTableModel tableModel;

        if (optionsModel.getContainers() == null || optionsModel.getContainers().size() == 0) {
            tableModel = new NoEditableTableModel(columnNames, 0);
        } else {
            Object[][] data = optionsModel.getContainers()
                    .stream()
                    .map(item -> new Object[]{item, item, getIcon(item)})
                    .toArray(size -> new Object[size][3]);
            tableModel = new NoEditableTableModel(data, columnNames);
        }

        table = new JTable(tableModel);
        table.setBackground(new Color(242, 242, 242));
        table.setGridColor(Color.DARK_GRAY);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);

        table.setFont(UIManager.getFont("Label.font").deriveFont(15));


        ActionTableCellRenderer<IAddaOptionsContainer> labelRenderer = new ActionTableCellRenderer<>(IAddaOptionsContainer::getLabel);
        table.getColumnModel().getColumn(0).setCellRenderer(labelRenderer);


        ActionTableCellRenderer<IAddaOptionsContainer> renderer =
                new ActionTableCellRenderer<IAddaOptionsContainer>(container -> StringHelper.removeTags(container.getDescription()));//IAddaOptionsContainer::getDescription);
//                new ActionTableCellRenderer<>(container -> container.getAddaOptions().stream().map(IAddaOption::getDisplayString).collect(Collectors.joining(DELIMITER)));
        table.getColumnModel().getColumn(1).setCellRenderer(renderer);

        table.getColumnModel().getColumn(2).setPreferredWidth(30);
        table.getColumnModel().getColumn(2).setMaxWidth(30);
        table.setRowSelectionAllowed(false);
        table.setFont(UIManager.getLookAndFeel().getDefaults().getFont("Label.font"));
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel()) {
            @Override
            public boolean isSortable(int column) {
                return false;
            }
        };
        table.setRowSorter(sorter);
        table.getTableHeader().setReorderingAllowed(false);
        scrollPane = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        panelOptions = new JPanel(new BorderLayout());
        JPanel additionalPanel = new JPanel(new BorderLayout());

        clearAllButton = new JButton("Clear all");

        clearAllButton.setEnabled(isClearAllEnabled(optionsModel));

        final JLabel label = new JLabel("Configured Options");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setPreferredSize(new Dimension(150, 25));

        //clearButton = new JButton(StringHelper.toDisplayString("clear all"));

        additionalPanel.add(label);
        additionalPanel.add(clearAllButton, BorderLayout.EAST);
        additionalPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        panelOptions.add(additionalPanel, BorderLayout.NORTH);
        panelOptions.add(scrollPane);

    }

    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
        if (event.getPropertyValue() instanceof IAddaOptionsContainer) {

            final DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
            if (event.getPropertyChangeType().equals(ModelPropertyChangeType.ADD)) {
                tableModel.addRow(new Object[]{event.getPropertyValue(), event.getPropertyValue(), getIcon(event.getPropertyValue())});
                tableModel.fireTableCellUpdated(tableModel.getDataVector().size() - 1, 0);
                tableModel.fireTableCellUpdated(tableModel.getDataVector().size() - 1, 1);
                tableModel.fireTableCellUpdated(tableModel.getDataVector().size() - 1, 2);
//                updateRowHeights();

                SwingUtilities.invokeLater(() -> {
                    JScrollBar vertical = scrollPane.getVerticalScrollBar();
                    vertical.setValue(vertical.getMaximum());
                });

            } else if (event.getPropertyChangeType().equals(ModelPropertyChangeType.REMOVE)
                    || event.getPropertyChangeType().equals(ModelPropertyChangeType.CHANGE)) {
                Vector<Vector> data = tableModel.getDataVector();
                int row = -1;
                for (int i = 0; i < data.size(); i++) {
                    Vector vector = data.get(i);
                    if (event.getPropertyValue().equals(vector.get(0))) {
                        row = i;
                        break;
                    }
                }
                if (row >= 0) {
                    if (event.getPropertyChangeType().equals(ModelPropertyChangeType.REMOVE)) {
                        tableModel.removeRow(row);
                    } else {
                        data.get(row).set(2, getIcon(event.getPropertyValue()));
                        tableModel.fireTableCellUpdated(row, 0);
                        tableModel.fireTableCellUpdated(row, 1);
                        tableModel.fireTableCellUpdated(row, 2);
//                        updateRowHeights();
                    }

                }
            }

        }

        if (sender instanceof OptionsModel) {
            OptionsModel optionsModel = (OptionsModel) sender;
            clearAllButton.setEnabled(isClearAllEnabled(optionsModel));
            //clearIcon = optionsModel.getContainers().stream().map(IAddaOptionsContainer::getAddaOptions).coll(List::).anyMatch(x -> x.)
        }

    }

    private boolean isClearAllEnabled(OptionsModel optionsModel) {
        boolean isEnabled = false;
        for (IAddaOptionsContainer container: optionsModel.getContainers()){
            if (container instanceof SizeModel) {
                SizeModel sizeModel = (SizeModel) container;
                isEnabled = !SizeEnum.AlongOX.equals(sizeModel.getType()) || sizeModel.getValue() != 1 || !SizeMeasureEnum.um.equals(sizeModel.getMeasure());
            } else if (container instanceof ModelBase && !((ModelBase) container).isDefaultState()) {
                isEnabled = true;
            }

            if (isEnabled) break;
        }
        return isEnabled;
    }

//    private void updateRowHeights()
//    {
//        for (int row = 0; row < table.getRowCount(); row++)
//        {
//            int rowHeight = table.getRowHeight();
//
//            for (int column = 0; column < table.getColumnCount(); column++)
//            {
//                Component comp = table.prepareRenderer(table.getCellRenderer(row, column), row, column);
//                rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
//            }
//
//            table.setRowHeight(row, rowHeight);
//        }
//    }

    private Icon getIcon(Object item) {
        if (item instanceof SizeModel) {
            SizeModel sizeModel = (SizeModel) item;
            if (SizeEnum.AlongOX.equals(sizeModel.getType()) && sizeModel.getValue() == 1 && SizeMeasureEnum.um.equals(sizeModel.getMeasure())) {
                return null;
            }
        }
        return clearIcon;
    }

    static class NoEditableTableModel extends DefaultTableModel {
        public NoEditableTableModel(Object[][] data, Object[] columnNames) {
            super(data, columnNames);
        }

        public NoEditableTableModel(Object[] columnNames, int i) {
            super(columnNames, i);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return column > 1;
        }
    }
}