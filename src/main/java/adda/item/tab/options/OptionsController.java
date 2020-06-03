package adda.item.tab.options;

import adda.application.controls.ButtonColumn;
import adda.base.IAddaOption;
import adda.base.IAddaOptionsContainer;
import adda.base.controllers.ControllerBase;
import adda.base.models.IModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

public class OptionsController extends ControllerBase {

    @Override
    protected void createAndBindListenersFromView() {
        if (model instanceof OptionsModel && view instanceof OptionsView) {
            OptionsModel optionsModel = (OptionsModel) model;
            OptionsView optionsView = (OptionsView) view;
            JTable table = (JTable) optionsView.getTable();

            Action delete = new AbstractAction()
            {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    JTable table = (JTable)actionEvent.getSource();
                    int modelRow = Integer.parseInt(actionEvent.getActionCommand());
                    ((IModel)((Vector)((DefaultTableModel)table.getModel()).getDataVector().get(modelRow)).get(0)).applyDefaultState();
//                    optionsModel.remove((IAddaOptionsContainer)((DefaultTableModel)table.getModel()).getDataVector().get(modelRow).get(0));

                }
            };

            ButtonColumn buttonColumn = new ButtonColumn(table, delete, 2);
            buttonColumn.setMnemonic(KeyEvent.VK_D);
        }
    }
}