package adda.base.controllers;

import adda.Context;
import adda.application.validation.validator.ModelValidator;
import adda.application.validation.validator.NotEmptyValidator;
import adda.base.models.IModel;
import adda.base.views.IView;
import adda.application.controls.ComboBoxItem;
import adda.application.controls.JNumericField;
import adda.utils.ReflectionHelper;
import adda.utils.StringHelper;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class ControllerBase implements IController {

    protected IView view;
    protected IModel model;


    protected static final Class[] DEFAULT_CLASS_LIST = new Class[]{JComboBox.class, JTextField.class, JCheckBox.class, JSpinner.class, JNumericField.class};

    public IModel getModel() {
        return model;
    }

    public void setModel(IModel model) {
        this.model = model;
    }

    public void setView(IView view) {
        this.view = view;
        //createAndBindListenersFromView();
    }

    public void init() {
        createAndBindListenersFromView();
    }

    protected void createAndBindListenersFromView() {
        List<Component> components = getRecursiveComponents(this.view.getRootComponent(), new ArrayList<Component>() {});

        for (Component component : components) {
            if (component.getClass().equals(JComboBox.class)) {
                JComboBox comboBox = (JComboBox) component;
                comboBox.setInputVerifier(new ModelValidator(Context.getInstance().getMainFrame(), comboBox, model, comboBox.getName()));
                comboBox.addActionListener(actionEvent -> {
                    processComboBox(comboBox);
                });
            }
            if (component.getClass().equals(JCheckBox.class)) {
                JCheckBox checkBox = (JCheckBox) component;
                checkBox.addItemListener(e -> {
                    processCheckBox(checkBox);
                });
            }
            if (component.getClass().equals(JTextField.class)) {
                JTextField textField = (JTextField) component;
                textField.addActionListener(actionEvent -> {
                    processTextField(textField);
                });
            }
            if (component.getClass().equals(JSpinner.class)) {
                JSpinner spinner = (JSpinner) component;
                spinner.addChangeListener(actionEvent -> {
                    processIntegerField(spinner);
                });
            }
            if (component.getClass().equals(JNumericField.class)) {
                JNumericField numericField = (JNumericField) component;
                final ModelValidator inputVerifier = new ModelValidator(Context.getInstance().getMainFrame(), numericField, model, numericField.getName());
                numericField.setInputVerifier(inputVerifier);
                numericField.addFocusListener(inputVerifier);
                SwingUtilities.invokeLater(() -> inputVerifier.verify(numericField));
//                numericField.getDocument().addDocumentListener(new DocumentListener() {
//                    @Override
//                    public void insertUpdate(DocumentEvent e) {
////                        processDoubleField(numericField);
//                        inputVerifier.verify(numericField);
//                    }
//
//                    @Override
//                    public void removeUpdate(DocumentEvent e) {
////                        processDoubleField(numericField);
//                        inputVerifier.verify(numericField);
//                    }
//
//                    @Override
//                    public void changedUpdate(DocumentEvent e) {
////                        processDoubleField(numericField);
//                        inputVerifier.verify(numericField);
//                    }
//                });
                numericField.addKeyListener(new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent e) {

                    }

                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode()==KeyEvent.VK_ENTER){
                            processDoubleField(numericField);
                        }
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {

                    }
                });
                numericField.addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent focusEvent) {

                    }

                    @Override
                    public void focusLost(FocusEvent focusEvent) {
                        if (StringHelper.isEmpty(numericField.getText())) {
                            numericField.setText("1.0");
                            //processDoubleField(numericField);
                        }
                        processDoubleField(numericField);
                        Context.getInstance().setLastParamsComponent(numericField);
                    }
                });

            }
        }
    }

    protected void processTextField(JTextField textField) {
        String text = textField.getText();
        if (validate(textField, text)) {
            ReflectionHelper.setPropertyValue(model, textField.getName(), text);
        }
    }

    protected void processIntegerField(JSpinner spinner) {
        int value = (int) spinner.getValue();
        if (validate(spinner, value)) {
            ReflectionHelper.setPropertyValue(model, spinner.getName(), value);
        }
    }

    protected void processDoubleField(JNumericField numericField) {
        final InputVerifier inputVerifier = numericField.getInputVerifier();
        if (inputVerifier != null) {
            inputVerifier.verify(numericField);
        }
        if (StringHelper.isEmpty(numericField.getText())) return;
        double value = numericField.getDouble();
        if (validate(numericField, value)) {
            ReflectionHelper.setPropertyValue(model, numericField.getName(), value);
            if (inputVerifier != null) {
                inputVerifier.verify(numericField);
            }
        }
    }

    protected void processCheckBox(JCheckBox checkBox) {
        boolean isSelected = checkBox.isSelected();
        if (validate(checkBox, isSelected)) {
            ReflectionHelper.setPropertyValue(model, checkBox.getName(), isSelected);
        }
    }

    protected void processComboBox(JComboBox comboBox) {
        ComboBoxItem selectedItem = (ComboBoxItem) comboBox.getSelectedItem();
        if (selectedItem != null && validate(comboBox, selectedItem)) {
            ReflectionHelper.setPropertyValue(model, comboBox.getName(), selectedItem.getKey());
        }
        if (comboBox.getInputVerifier() != null) {
            comboBox.getInputVerifier().verify(comboBox);
        }
    }

    protected List<Component> getRecursiveComponents(JComponent panel, List<Component> list) {
        for (Component component : panel.getComponents()) {
            if (component.getClass().equals(JPanel.class)) {
                getRecursiveComponents((JPanel) component, list);
            }
            if (Stream.of(DEFAULT_CLASS_LIST).anyMatch(x -> x.equals(component.getClass()))) {
                list.add(component);
            }
        }
        return list;
    }


    @Override
    public IView getView() {
        return view;
    }

    protected boolean validate(Component component, Object value) {
        return true;
    }
}
