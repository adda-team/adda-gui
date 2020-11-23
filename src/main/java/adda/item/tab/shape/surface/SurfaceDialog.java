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

        final Dimension labelPreferredSize = new Dimension(155, 20);
        final Dimension fieldPreferredSize = new Dimension(150, 20);

        JPanel rootPanel = new JPanel(new GridLayout(4, 1));
        //rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
        //rootPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));


        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        final JLabel label = new JLabel(StringHelper.toDisplayString("Distance from particle center"));

        label.setPreferredSize(labelPreferredSize);
        label.setVerticalAlignment(JLabel.TOP);
        label.setHorizontalAlignment(JLabel.LEFT);
        panel.add(label);

        JNumericField distanceField = new JNumericField();
        distanceField.setMaxLength(20);
        distanceField.setPrecision(10);
        distanceField.setAllowNegative(false);

        distanceField.setPreferredSize(new Dimension(80, 20));
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
        JPanel wrapper = surroundWithPanel(distanceField);


        wrapper.add(new JLabel(surfaceModel.getMeasure()));
        panel.add(wrapper);

        rootPanel.add(panel);

        panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        final JLabel surfaceTypeLabel = new JLabel(StringHelper.toDisplayString("Surface type"));
        surfaceTypeLabel.setPreferredSize(labelPreferredSize);
        surfaceTypeLabel.setVerticalAlignment(JLabel.TOP);
        surfaceTypeLabel.setHorizontalAlignment(JLabel.LEFT);
        panel.add(surfaceTypeLabel);

        JComboBox<ComboBoxItem> comboBox = new JComboBox<>();
        comboBox.setPreferredSize(fieldPreferredSize);

        Vector<ComboBoxItem> dataSource = new Vector<>(Arrays.asList(
                new ComboBoxItem(false, StringHelper.toDisplayString("Set refractive index")),
                new ComboBoxItem(true, StringHelper.toDisplayString("Perfectly reflection")))
        );
        comboBox.setModel(new DefaultComboBoxModel<>(dataSource));

        comboBox.setSelectedIndex(surfaceModel.isInfinite() ? 1 : 0);


        panel.add(surroundWithPanel(comboBox));

        rootPanel.add(panel);


        JPanel refractiveIndexPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        final JLabel refractiveIndexLabel = new JLabel(StringHelper.toDisplayString("Refractive index"));
        refractiveIndexLabel.setPreferredSize(labelPreferredSize);
        refractiveIndexLabel.setVerticalAlignment(JLabel.TOP);
        refractiveIndexLabel.setHorizontalAlignment(JLabel.LEFT);
        refractiveIndexPanel.add(refractiveIndexLabel);

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

        refractiveIndexPanel.add(complexNumberInput);

        rootPanel.add(refractiveIndexPanel);
        refractiveIndexPanel.setVisible(!surfaceModel.isInfinite());


        JPanel intSurfPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        final JLabel intSurfLabel = new JLabel(StringHelper.toDisplayString("Reflection term"));
        intSurfLabel.setPreferredSize(labelPreferredSize);
        intSurfLabel.setVerticalAlignment(JLabel.TOP);
        intSurfLabel.setHorizontalAlignment(JLabel.LEFT);
        intSurfPanel.add(intSurfLabel);

        JComboBox<ComboBoxItem> intSurfComboBox = new JComboBox<>();
        intSurfComboBox.setPreferredSize(fieldPreferredSize);

        Vector<ComboBoxItem> intSurfDataSource = new Vector<>(
                Arrays.asList(
                    new ComboBoxItem(SurfaceEnum.som, StringHelper.toDisplayString(SurfaceEnum.som)),
                    new ComboBoxItem(SurfaceEnum.img, StringHelper.toDisplayString(SurfaceEnum.img))
                )
        );

        intSurfComboBox.setModel(new DefaultComboBoxModel<>(intSurfDataSource));

        intSurfComboBox.setSelectedIndex(surfaceModel.getInteractionSurf() ==  SurfaceEnum.img ? 1 : 0);

        intSurfComboBox.addActionListener(actionEvent -> {
            ComboBoxItem selectedItem = (ComboBoxItem) intSurfComboBox.getSelectedItem();
            if (selectedItem != null) {
                final SurfaceEnum surfaceEnum = (SurfaceEnum) selectedItem.getKey();
                surfaceModel.setInteractionSurf(surfaceEnum);
            }

        });

        intSurfPanel.add(surroundWithPanel(intSurfComboBox));

        rootPanel.add(intSurfPanel);
        intSurfPanel.setVisible(!surfaceModel.isInfinite());









        comboBox.addActionListener(actionEvent -> {
            ComboBoxItem selectedItem = (ComboBoxItem) comboBox.getSelectedItem();
            if (selectedItem != null) {
                final boolean isInfinite = (boolean) selectedItem.getKey();
                surfaceModel.setInfinite(isInfinite);

                refractiveIndexPanel.setVisible(!isInfinite);
                intSurfPanel.setVisible(!isInfinite);
            }

        });


        getDialogContentPanel().add(rootPanel);

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
