package adda.item.tab.base.refractiveIndex;

import adda.application.controls.JComplexNumberInput;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.views.ViewBase;
import adda.base.views.ViewDialogBase;
import adda.item.tab.internals.formulation.FormulationModel;
import adda.utils.StringHelper;

import javax.swing.*;
import java.awt.*;

public class RefractiveIndexView extends ViewDialogBase {


    protected JComplexNumberInput complexNumberInput;

    public JComplexNumberInput getComplexNumberInput() {
        return complexNumberInput;
    }

    @Override
    protected void initLabel(IModel model) {

    }

    @Override
    protected void initPanel() {
        super.initPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setMinimumSize(new Dimension(220, 10));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

    }


    @Override
    protected void initFromModelInner(IModel model) {
        super.initFromModelInner(model);
        if (components.containsKey(RefractiveIndexModel.IS_ANISOTROP_FIELD_NAME) && model instanceof RefractiveIndexModel) {

            final RefractiveIndexModel refractiveIndexModel = (RefractiveIndexModel) model;
            components.get(RefractiveIndexModel.IS_ANISOTROP_FIELD_NAME).setVisible(refractiveIndexModel.isEnabledAnisotrop());
//            components.get(RefractiveIndexModel.IS_ANISOTROP_FIELD_NAME).setEnabled(refractiveIndexModel.isEnabledAnisotrop());
            complexNumberInput = new JComplexNumberInput();
            complexNumberInput.setRealPart(refractiveIndexModel.getRealX());
            complexNumberInput.setImagPart(refractiveIndexModel.getImagX());

            complexNumberInput.setVisible(!refractiveIndexModel.isAnisotrop());

            panel.add(complexNumberInput);
            setBorder(model);
        }

    }

    private void setBorder(IModel model) {
        if (outerPanel != null) {
            outerPanel.setBorder(BorderFactory.createTitledBorder(model.getLabel()));
        }
    }

    @Override
    protected boolean isShowAdditionalPanel(IModel model) {
        if (model instanceof RefractiveIndexModel) {
            RefractiveIndexModel refractiveIndexModel = (RefractiveIndexModel) model;
            return refractiveIndexModel.isAnisotrop();
        }
        return false;
    }

    @Override
    protected JPanel getOverview(IModel model) {
        JPanel panel = new JPanel();
        if (model instanceof RefractiveIndexModel) {
            RefractiveIndexModel refractiveIndexModel = (RefractiveIndexModel) model;
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.add(new JLabel(String.format("OX: %s + i%s", StringHelper.toDisplayString(refractiveIndexModel.getRealX()), StringHelper.toDisplayString(refractiveIndexModel.getImagX()))));//todo localization
            panel.add(new JLabel(String.format("OY: %s + i%s", StringHelper.toDisplayString(refractiveIndexModel.getRealY()), StringHelper.toDisplayString(refractiveIndexModel.getImagY()))));//todo localization
            panel.add(new JLabel(String.format("OZ: %s + i%s", StringHelper.toDisplayString(refractiveIndexModel.getRealZ()), StringHelper.toDisplayString(refractiveIndexModel.getImagZ()))));//todo localization
        }
        return panel;
    }

    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
        super.modelPropertyChanged(sender, event);

        if ("label".equals(event.getPropertyName())) {
            setBorder(sender);
            return;
        }

        if (sender instanceof RefractiveIndexModel) {

            RefractiveIndexModel refractiveIndexModel = (RefractiveIndexModel) sender;
            if (RefractiveIndexModel.IS_ENABLED_ANISOTROP_FIELD_NAME.equals(event.getPropertyName()) && components.containsKey(RefractiveIndexModel.IS_ANISOTROP_FIELD_NAME)) {
                components.get(RefractiveIndexModel.IS_ANISOTROP_FIELD_NAME).setVisible(refractiveIndexModel.isEnabledAnisotrop());
//                components.get("isAnisotrop").setEnabled(refractiveIndexModel.isEnabledAnisotrop());
            }

            complexNumberInput.setVisible(!refractiveIndexModel.isAnisotrop());

            if (complexNumberInput.getRealPart() != refractiveIndexModel.getRealX()) {
                complexNumberInput.setRealPart(refractiveIndexModel.getRealX());
            }

            if (complexNumberInput.getImagPart() != refractiveIndexModel.getImagX()) {
                complexNumberInput.setImagPart(refractiveIndexModel.getImagX());
            }


        }
    }
}