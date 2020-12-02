package adda.base.views;

import adda.base.models.IModel;
import adda.base.events.IModelPropertyChangeEvent;
import adda.application.controls.ComboBoxItem;
import adda.application.controls.JNumericField;
import adda.base.models.ModelBase;
import adda.help.HelpProvider;
import adda.help.HelpUtil;
import adda.utils.ListUtils;
import adda.utils.ReflectionHelper;
import adda.utils.StringHelper;
import net.java.balloontip.BalloonTip;
import net.java.balloontip.styles.RoundedBalloonStyle;


import javax.help.CSH;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

public class ViewBase implements IView {

    protected JPanel panel;

    @Override
    public void refresh() {

    }

    @Override
    public JComponent getRootComponent() {
        return this.panel;
    }

    protected Map<String, Component> components = new HashMap<String, Component>(); //todo may be initialization can delete
    protected Map<String, JPanel> wrappers = new HashMap<String, JPanel>(); //todo may be initialization can delete

    protected static final String WRAPPER_POSTFIX = "_wrapper_jPanel_postfix_STR";
    @Override
    public void initFromModel(IModel model) {
        initPanel();
        initLabel(model);
        initFromModelInner(model);
    }

    protected void initFromModelInner(IModel model) {
        components = new HashMap<>();
        Map<String, String> bindProperties = model.getBindProperties();
        model.getViewableProperties()
                .entrySet()
                .forEach(entry -> {
                    String entryName = entry.getKey();
                    Class<?> entryClass = entry.getValue();
                    Component component = null;
                    String label = model.getViewableLabel(entryName);
                    boolean addLabel = false;

                    if (entryClass.isEnum()) {

                        if (!StringHelper.isEmpty(label)) {
                            addLabel = true;
                        }
                        Vector<ComboBoxItem> dataSource = ListUtils.getDataSource(entryClass);
                        JComboBox comboBox = new JComboBox(dataSource);
                        comboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
//                        comboBox.setMinimumSize(new Dimension(50, 20));
                        //comboBox.setRenderer(new ComboBoxItemRenderer());
                        Object val = ReflectionHelper.getPropertyValue(model, entryName);

                        Optional<ComboBoxItem> selectedComboBoxItem =
                                dataSource
                                        .stream()
                                        .filter(item -> val.equals(item.getKey()))
                                        .findFirst();

                        if (selectedComboBoxItem.isPresent())
                            comboBox.setSelectedItem(selectedComboBoxItem.get());

                        component = comboBox;
                    } else if (entryClass.equals(String.class) && !ModelBase.LABEL_FIELD_NAME.equals(entryName)) {
                        if (!StringHelper.isEmpty(label)) {
                            addLabel = true;
                        }
                        JTextField textField = new JTextField("");
                        textField.setPreferredSize(new Dimension(80, 20));
                        String val = (String) ReflectionHelper.getPropertyValue(model, entryName);
                        textField.setText(val);
                        component = textField;
                    } else if (entryClass.equals(boolean.class)) {
                        JCheckBox checkBox = new JCheckBox();
                        boolean val = (boolean) ReflectionHelper.getPropertyValue(model, entryName);
                        checkBox.setAlignmentX(Component.LEFT_ALIGNMENT);
                        checkBox.setSelected(val);
                        if (!StringHelper.isEmpty(label)) {
                            checkBox.setText(label);//todo localization
                        }
                        component = checkBox;
                    } else if (entryClass.equals(int.class) || entryClass.equals(Integer.class)) {
                        if (!StringHelper.isEmpty(label)) {
                            addLabel = true;
                        }
                        SpinnerModel spinnerModel = new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1);
                        JSpinner spinner = new JSpinner(spinnerModel);
                        spinner.setPreferredSize(new Dimension(80, 20));
                        int val = (int) ReflectionHelper.getPropertyValue(model, entryName);
                        spinner.setValue(val);
                        component = spinner;
                    } else if (entryClass.equals(double.class) || entryClass.equals(Double.class)) {
                        if (!StringHelper.isEmpty(label)) {
                            addLabel = true;
                        }
                        JNumericField numericField = new JNumericField();
                        numericField.setMaxLength(20);
                        numericField.setPrecision(10);
                        numericField.setAllowNegative(false);
                        numericField.setPreferredSize(new Dimension(80, 20));

                        double val = (double) ReflectionHelper.getPropertyValue(model, entryName);
                        numericField.setDouble(val);
                        component = numericField;
                    }

                    if (component != null) {
                        component.setName(entryName);
                        CSH.setHelpIDString(component, HelpProvider.getHelpID(model).get(0));
                        components.put(entryName, component);
                        if (addLabel && !StringHelper.isEmpty(label)) {
                            JPanel wrapper = getWrapperPanel();
                            final JLabel infoLabel = new JLabel(label);
                            setHelpTooltip(model, infoLabel);
                            wrapper.add(panel.add(infoLabel));
                            wrapper.add(component);
                            wrappers.put(entryName + WRAPPER_POSTFIX, wrapper);
                            wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
                            panel.add(wrapper);
                        } else {
                            panel.add(component);
                        }


                        Optional<String> bindProperty = bindProperties.entrySet()
                                .stream()
                                .filter(innerEntry -> entryName.equals(innerEntry.getValue()))
                                .map(Map.Entry::getKey)
                                .findFirst();

                        if (bindProperty.isPresent()) {
                            try {
                                boolean val = (boolean) ReflectionHelper.getPropertyValue(model, bindProperty.get());
                                if (model.isVisibleIfDisabled()) {
                                    Component finalComponent = component;
                                    component.addMouseListener(new MouseAdapter() {

                                        volatile BalloonTip balloonTip;

                                        @Override
                                        public void mouseEntered(MouseEvent me) {
                                            if (!(boolean) ReflectionHelper.getPropertyValue(model, bindProperty.get())
                                                    && (balloonTip == null || !balloonTip.isVisible())
                                                    && model.getValidationErrors().containsKey(bindProperty.get())
                                                    && !StringHelper.isEmpty(model.getValidationErrors().get(bindProperty.get()))
                                            ) {
                                                RoundedBalloonStyle style = new RoundedBalloonStyle(5, 5, Color.WHITE, Color.black);
                                                balloonTip = new BalloonTip(
                                                        (JComponent) finalComponent,
                                                        new JLabel(model.getValidationErrors().get(bindProperty.get())),
                                                        style,
                                                        BalloonTip.Orientation.RIGHT_ABOVE,
                                                        BalloonTip.AttachLocation.NORTHWEST,
                                                        30, 10,
                                                        false
                                                );
                                            }

                                        }

                                        @Override
                                        public void mouseExited(MouseEvent e) {
                                            if (balloonTip != null) {
                                                balloonTip.closeBalloon();
                                                balloonTip = null;
                                            }
                                        }
                                    });
                                    component.setEnabled(val);
                                } else {
                                    component.setVisible(val);
                                }

                                if (addLabel && !StringHelper.isEmpty(label)) {
                                    wrappers.get(entryName + WRAPPER_POSTFIX).setVisible(val);
                                }
//                                component.setEnabled(val);
                            } catch (ClassCastException e) {
                                System.out.println(e.fillInStackTrace());
                            }
                        }
                    }
                });
    }

    protected JPanel getWrapperPanel() {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new FlowLayout(FlowLayout.LEFT));
        return wrapper;
    }


    protected void initPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
//        panel.setAlignmentY(Component.TOP_ALIGNMENT);
//        panel.setPreferredSize(new Dimension(999999, 30));
//        panel.setBackground(Color.white);
        this.panel = panel;
    }

    protected void initLabel(IModel model) {
        JLabel label = createLabel(model);

        components.put("label", label);
        panel.add(label);
    }

    protected JLabel createLabel(IModel model) {
        JLabel label = new JLabel();
        label.setText(model.getLabel());
        label.setPreferredSize(new Dimension(120, 30));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setAlignmentY(Component.TOP_ALIGNMENT);


        setHelpTooltip(model, label);


        //ToolTipUtils.balloonToToolTip(balloonTip, 500, 30000);

//                            infoLabel.addMouseListener(new MouseAdapter() {
//                                public void mouseEntered(MouseEvent me) {
//                                    balloonTip.setVisible(true);
//                                }
//                                public void mouseExited(MouseEvent e) {
//                                    balloonTip.setVisible(false);
//                                }
//                            });

//        JTextArea textArea = new JTextArea(1, 15);
//        textArea.setPreferredSize(new Dimension(120, 30));
//        textArea.setText(model.getLabel());
//        textArea.setWrapStyleWord(true);
//        textArea.setLineWrap(true);
//        textArea.setAlignmentX(Component.CENTER_ALIGNMENT);
//        textArea.setOpaque(false);
//        textArea.setEditable(false);
//        textArea.setFocusable(true);
//        textArea.setBackground(UIManager.getColor("Label.background"));
//        textArea.setFont(UIManager.getFont("Label.font"));
//        textArea.setBorder(UIManager.getBorder("Label.border"));
//        textArea.setAlignmentY(Component.CENTER_ALIGNMENT);
        return label;
    }

    protected void setHelpTooltip(IModel model, JComponent component) {
        RoundedBalloonStyle style = new RoundedBalloonStyle(5, 5, Color.WHITE, Color.black);
        BalloonTip balloonTip = new BalloonTip(
                component,
                HelpProvider.getHelpPanel(model),
                style,
                BalloonTip.Orientation.LEFT_ABOVE,
                BalloonTip.AttachLocation.CENTER,
                30, 10,
                false
        );
        balloonTip.setMaximumSize(new Dimension(200, 9999));
        balloonTip.setMinimumSize(new Dimension(200, 10));
        //balloonTip.setPreferredSize(new Dimension(200, 0));
        HelpUtil.balloonToHelpToolTip(balloonTip, 500);
    }

    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
        Map<String, String> bindProperties = sender.getBindProperties();
        if (bindProperties.containsKey(event.getPropertyName())
                && components.containsKey(bindProperties.get(event.getPropertyName()))) {
            Component component = components.get(bindProperties.get(event.getPropertyName()));
            try {
                boolean val = (boolean) event.getPropertyValue();
                if (sender.isVisibleIfDisabled()) {
                    component.setEnabled(val);
                } else {
                    component.setVisible(val);
                }

                if (wrappers.containsKey(bindProperties.get(event.getPropertyName()) + WRAPPER_POSTFIX)) {
                    final JPanel panel = wrappers.get(bindProperties.get(event.getPropertyName()) + WRAPPER_POSTFIX);
                    panel.setVisible(val);
                }
                //component.setEnabled(val);
            } catch (ClassCastException e) {
                System.out.println(e.fillInStackTrace());
            }

        }
        if (components.containsKey(event.getPropertyName())) {
            Component component = components.get(event.getPropertyName());
            //!return in every if!
            //usual case contains only one changes for one control, non-trivial case mean overriding modelPropertyChanged

            if (component instanceof JComboBox) {
                JComboBox comboBox = (JComboBox) component;
                if (event.getPropertyValue() == null) {
                    comboBox.setSelectedIndex(-1);
                    return;
                }

                ComboBoxModel comboBoxModel = comboBox.getModel();
                for (int i = 0; i < comboBoxModel.getSize(); i++) {
                    ComboBoxItem item = (ComboBoxItem) comboBoxModel.getElementAt(i);
                    if (event.getPropertyValue().equals(item.getKey())) {
                        if (comboBox.getSelectedIndex() != i) {
                            comboBox.setSelectedIndex(i);
                        }
                        return;
                    }
                }
                return;
            }

            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                label.setText(sender.getLabel());
                return;//usual case contains only one changes for one control, non-trivial case mean overriding modelPropertyChanged
            }

            if (component instanceof JCheckBox) {
                JCheckBox checkbox = (JCheckBox) component;
                boolean value = (boolean) event.getPropertyValue();//todo check cast
                boolean currentValue = checkbox.isSelected();
                if (currentValue != value) {
                    checkbox.setSelected(value);
                }
                return;
            }

            if (component instanceof JNumericField) {
                JNumericField numericField = (JNumericField) component;
                double value = (double) event.getPropertyValue();//todo check cast
                double currentValue = numericField.getDouble();
                if (value != currentValue) {
                    numericField.setDouble(value);
                }
                return;
            }


            if (component instanceof JTextField) {
                JTextField textField = (JTextField) component;
                String value = (String) event.getPropertyValue();//todo check cast
                String currentValue = textField.getText();
                if (!(currentValue + "").equals(value)) {
                    textField.setText(value);
                }
                return;
            }

            if (component instanceof JSpinner) {
                JSpinner spinner = (JSpinner) component;
                int value = (int) event.getPropertyValue();//todo check cast
                int currentValue = (int) spinner.getValue();
                if (value != currentValue) {
                    spinner.setValue(value);
                }
                //return;
            }



        }
    }
}
