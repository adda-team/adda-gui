package adda.base.controllers;

import adda.Context;
import adda.application.controls.CustomOkCancelModalDialog;
import adda.base.models.IModel;
import adda.base.views.ViewDialogBase;
import adda.application.controls.ComboBoxItem;
import adda.application.controls.JNumericField;
import adda.item.tab.base.propagation.PropagationDialog;
import adda.item.tab.base.propagation.PropagationModel;
import adda.utils.ReflectionHelper;
import adda.utils.StringHelper;

import javax.swing.*;
import java.awt.*;

public abstract class ControllerDialogBase extends ControllerBase {

    private volatile boolean isDialogModelDisabled = false;

    protected abstract boolean needOpenDialog(String fieldName, Object fieldValue);

    protected abstract CustomOkCancelModalDialog getDialog(IModel dialogModel);

    public boolean processDialog(IModel dialogModel) {
        CustomOkCancelModalDialog dialog = getDialog(dialogModel);
        if (dialog != null) {
            dialog.pack();
            dialog.setVisible(true);
            return dialog.isOkPressed();
        }
        return false;
    }

    @Override
    protected void createAndBindListenersFromView() {
        super.createAndBindListenersFromView();
        if (view instanceof ViewDialogBase) {
            ViewDialogBase viewDialogBase = (ViewDialogBase) view;
            if (viewDialogBase.getEditButton() != null) {
                viewDialogBase.getEditButton().addActionListener(e -> {
                    try {
                        IModel clone = (IModel) model.clone();//todo non-deep clone! override clone method or serialize/deserialize to get deep clone
                        if (processDialog(clone)) {
                            model.copyProperties(clone);
                        }
                    } catch (CloneNotSupportedException ex) {
                        ex.printStackTrace();
                    }
                });
            }

            if (viewDialogBase.getClearButton() != null) {
                viewDialogBase.getClearButton().addActionListener(e -> {
                    model.applyDefaultState();
                });
            }
        }

    }

    @Override
    protected void processTextField(JTextField textField) {
        processValue(textField, textField.getText());
    }

    @Override
    protected void processCheckBox(JCheckBox checkBox) {
        processValue(checkBox, checkBox.isSelected());
    }

    @Override
    protected void processComboBox(JComboBox comboBox) {
        ComboBoxItem selectedItem = (ComboBoxItem) comboBox.getSelectedItem();
        if (selectedItem != null) {
            processValue(comboBox, selectedItem.getKey());

        }
    }

    @Override
    protected void processIntegerField(JSpinner spinner) {
        processValue(spinner, spinner.getValue());
    }

//    protected void processDoubleField(JFormattedTextField formattedTextField) {
//        processValue(formattedTextField, formattedTextField.getValue());
//    }

    protected void processDoubleField(JNumericField numericField) {
        if (StringHelper.isEmpty(numericField.getText())) return;
        processValue(numericField, numericField.getDouble());
    }
    protected void processValue(JComponent component, Object value) {
        //invokeLater for fix combobox close list issue
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (validate(component, value)) {
                    if (!Context.getInstance().isGlobalBlockDialogs()
                            && !model.isUnderCopy()
                            && !isDialogModelDisabled
                            && needOpenDialog(component.getName(), value)
                            && view instanceof ViewDialogBase
                    ) {
                        try {
                            IModel clone = (IModel) model.clone();
                            ReflectionHelper.setPropertyValue(clone, component.getName(), value);

                            if (processDialog(clone)) {
                                model.copyProperties(clone);//todo non-deep clone! override clone method or serialize/deserialize to get deep clone
                                ReflectionHelper.setPropertyValue(model, component.getName(), value);
                            } else {
                                //set new value and next set previous value, need for view control state change
                                //100% we will fire changes twice 1) set wrong new value (for sync UI control value and model)
                                //                                2) revert correct old value by execute 'set property' -> refresh vodel -> refresh view
                                //so we need disable dialog mode for reverting to prevent dialog opening when old value also open dialog
                                isDialogModelDisabled = true;
                                IModel additionalClone = (IModel) model.clone();

                                ReflectionHelper.setPropertyValue(model, component.getName(), value);
                                model.copyProperties(additionalClone);

                            }
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        ReflectionHelper.setPropertyValue(model, component.getName(), value);
                        isDialogModelDisabled = false;
                    }
                }
                if (component.getInputVerifier() != null) {
                    component.getInputVerifier().verify(component);
                }
            }
        });
    }
}