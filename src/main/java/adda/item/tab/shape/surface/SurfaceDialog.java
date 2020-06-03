package adda.item.tab.shape.surface;

import adda.application.controls.ComboBoxItem;
import adda.application.controls.CustomOkCancelModalDialog;
import adda.application.controls.JComplexNumberInput;
import adda.application.controls.JNumericField;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.item.tab.shape.granules.GranulesModel;
import adda.utils.ListenerHelper;
import adda.utils.StringHelper;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;
import java.util.stream.Collectors;

public class SurfaceDialog extends CustomOkCancelModalDialog {
    public SurfaceDialog(IModel model) {
        super(model, StringHelper.toDisplayString("Surface"));

        if (!(model instanceof  SurfaceModel))  return;

        SurfaceModel surfaceModel =  (SurfaceModel) model;

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        final JLabel label = new JLabel(StringHelper.toDisplayString("Distance from particle center") + ", " + surfaceModel.getMeasure());
        label.setVerticalAlignment(JLabel.TOP);
        label.setHorizontalAlignment(JLabel.RIGHT);
        panel.add(label);


        JNumericField distanceField = new JNumericField();
        distanceField.setMaxLength(20);
        distanceField.setPrecision(10);
        distanceField.setAllowNegative(false);
        distanceField.setPreferredSize(new Dimension(100, 20));
        distanceField.setDouble(surfaceModel.getDistance());
        distanceField.getDocument().addDocumentListener(ListenerHelper.getSimpleDocumentListener(() -> {
            processDistance(distanceField, surfaceModel);
        }));

        distanceField.addFocusListener(ListenerHelper.getSimpleFocusListener(() -> {
            if (StringHelper.isEmpty(distanceField.getText())) {
                distanceField.setText("1.0");
                processDistance(distanceField, surfaceModel);
            }
        }));

        panel.add(surroundWithPanel(distanceField));

        final JLabel refractiveIndexLabel = new JLabel(StringHelper.toDisplayString("Refractive index "));
        refractiveIndexLabel.setVerticalAlignment(JLabel.TOP);
        refractiveIndexLabel.setHorizontalAlignment(JLabel.RIGHT);
        panel.add(refractiveIndexLabel);


        JPanel refractiveIndexPanel = new JPanel();
        refractiveIndexPanel.setLayout(new BoxLayout(refractiveIndexPanel, BoxLayout.Y_AXIS));


        JComplexNumberInput complexNumberInput = new JComplexNumberInput();


        complexNumberInput.setRealPart(surfaceModel.getRealPart());
        complexNumberInput.setImagPart(surfaceModel.getImagPart());

        complexNumberInput.addRealPartDocumentListener(
                ListenerHelper.getSimpleDocumentListener(() -> {
                    if (!StringHelper.isEmpty(complexNumberInput.getRealPartText())) {
                        surfaceModel.setRealPart(complexNumberInput.getRealPart());
                    }
                })
        );

        complexNumberInput.addImagPartDocumentListener(
                ListenerHelper.getSimpleDocumentListener(() -> {
                    if (!StringHelper.isEmpty(complexNumberInput.getImagPartText())) {
                        surfaceModel.setImagPart(complexNumberInput.getImagPart());
                    }
                })
        );
        complexNumberInput.setEnabled(!surfaceModel.isInfinite());
        
        
        JComboBox<ComboBoxItem> comboBox = new JComboBox<>();
        comboBox.setPreferredSize(new Dimension(100, 20));

        Vector<ComboBoxItem> dataSource = new Vector<>(Arrays.asList(
                new ComboBoxItem(false, StringHelper.toDisplayString("Usual")),
                new ComboBoxItem(true, StringHelper.toDisplayString("Infinite")))
        );
        comboBox.setModel(new DefaultComboBoxModel<>(dataSource));

        comboBox.setSelectedIndex(surfaceModel.isInfinite() ? 1 : 0);

        comboBox.addActionListener(actionEvent -> {
            ComboBoxItem selectedItem = (ComboBoxItem) comboBox.getSelectedItem();
            if (selectedItem != null) {
                final boolean isInfinite = (boolean) selectedItem.getKey();
                surfaceModel.setInfinite(isInfinite);
                complexNumberInput.setEnabled(!isInfinite);
            }

        });
        refractiveIndexPanel.add(comboBox);
        refractiveIndexPanel.add(complexNumberInput);

        panel.add(refractiveIndexPanel);

        getDialogContentPanel().add(panel);

    }

    protected void processDistance(JNumericField numericField, SurfaceModel model) {
        if (StringHelper.isEmpty(numericField.getText())) return;
        double value = numericField.getDouble();
        model.setDistance(value);
    }


    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {

    }
}
