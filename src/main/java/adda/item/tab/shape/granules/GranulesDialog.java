package adda.item.tab.shape.granules;

import adda.application.controls.ComboBoxItem;
import adda.application.controls.CustomOkCancelModalDialog;
import adda.application.controls.JComplexNumberInput;
import adda.application.controls.JNumericField;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.utils.ListenerHelper;
import adda.utils.ReflectionHelper;
import adda.utils.StringHelper;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Optional;
import java.util.Vector;
import java.util.stream.Collectors;

public class GranulesDialog extends CustomOkCancelModalDialog {


    public GranulesDialog(IModel model) {
        super(model, "Granules configuration");//todo localization
        if (model instanceof GranulesModel) {
            final GranulesModel granulesModel = (GranulesModel) model;

            JPanel panel = new JPanel(new GridLayout(5, 2));
            panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
            panel.add(new JLabel("volume fraction" + ", [5; 95]% "));

            SpinnerModel spinnerModel = new SpinnerNumberModel((int)granulesModel.fraction, 5, 95, 1);
            JSpinner spinner = new JSpinner(spinnerModel);
            spinner.setPreferredSize(new Dimension(100, 20));
            spinner.addChangeListener(actionEvent -> {
                int value = (int) spinner.getValue();
                granulesModel.setFraction(value);
            });
            panel.add(surroundWithPanel(spinner));

            panel.add(new JLabel(StringHelper.toDisplayString("diameter") + ", " + granulesModel.getMeasure()));

            JNumericField diameterField = new JNumericField();
            diameterField.setMaxLength(20);
            diameterField.setPrecision(10);
            diameterField.setAllowNegative(false);
            diameterField.setPreferredSize(new Dimension(100, 20));
            diameterField.setDouble(granulesModel.getDiameter());
            diameterField.getDocument().addDocumentListener(ListenerHelper.getSimpleDocumentListener(() -> {
                processDiameter(diameterField, granulesModel);
            }));

            diameterField.addFocusListener(ListenerHelper.getSimpleFocusListener(() -> {
                if (StringHelper.isEmpty(diameterField.getText())) {
                    diameterField.setText("1.0");
                    processDiameter(diameterField, granulesModel);
                }
            }));

            panel.add(surroundWithPanel(diameterField));

            panel.add(new JLabel("shape domain"));

            if (granulesModel.getShapeModel() != null) {
                JComboBox<ComboBoxItem> comboBox = new JComboBox<>();
                comboBox.setPreferredSize(new Dimension(100, 20));
                Vector<ComboBoxItem> dataSource = granulesModel.getShapeModel()
                                                               .getShapeDomainInfos()
                                                               .stream()
                                                               .map(info -> new ComboBoxItem(info.getOrder(), info.getName()))
                                                               .collect(Collectors.toCollection(Vector::new));
                comboBox.setModel(new DefaultComboBoxModel<>(dataSource));
                Optional<ComboBoxItem> selectedComboBoxItem =
                        dataSource
                                .stream()
                                .filter(item -> granulesModel.getDomainNumber() == (int) item.getKey())
                                .findFirst();

                selectedComboBoxItem.ifPresent(comboBox::setSelectedItem);
                comboBox.addActionListener(actionEvent -> {
                    ComboBoxItem selectedItem = (ComboBoxItem) comboBox.getSelectedItem();
                    if (selectedItem != null) {
                        granulesModel.setDomainNumber((int) selectedItem.getKey());
                        granulesModel.setDomainDisplayString(selectedItem.getDescription());
                    }

                });
                panel.add(surroundWithPanel(comboBox));
            } else {
                panel.add(new JLabel(""));
            }

            panel.add(new JLabel("refractive index"));

            JComplexNumberInput complexNumberInput = new JComplexNumberInput();


            complexNumberInput.setRealPart(granulesModel.getRealX());
            complexNumberInput.setImagPart(granulesModel.getImagX());

            complexNumberInput.addRealPartDocumentListener(
                    ListenerHelper.getSimpleDocumentListener(() -> {
                        if (!StringHelper.isEmpty(complexNumberInput.getRealPartText())) {
                            granulesModel.setRealX(complexNumberInput.getRealPart());
                        }
                    })
            );

            complexNumberInput.addImagPartDocumentListener(
                    ListenerHelper.getSimpleDocumentListener(() -> {
                        if (!StringHelper.isEmpty(complexNumberInput.getImagPartText())) {
                            granulesModel.setImagX(complexNumberInput.getImagPart());
                        }
                    })
            );

            panel.add(complexNumberInput);

            panel.add(new JLabel("save granules"));

            JCheckBox checkBox = new JCheckBox();
            checkBox.setSelected(granulesModel.isSave());

            checkBox.addItemListener(e -> {
                granulesModel.setSave(checkBox.isSelected());
            });

            panel.add(checkBox);

            getDialogContentPanel().add(panel);

        }
    }


    protected void processDiameter(JNumericField numericField, GranulesModel model) {
        if (StringHelper.isEmpty(numericField.getText())) return;
        double value = numericField.getDouble();
        model.setDiameter(value);
    }

    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {

    }
}
