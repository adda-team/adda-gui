package adda.item.tab.internals.formulation;

import adda.base.models.IModel;
import adda.base.views.ViewBase;
import adda.base.views.ViewDialogBase;
import adda.utils.StringHelper;

import javax.swing.*;

public class FormulationView extends ViewDialogBase {

    @Override
    protected boolean isShowAdditionalPanel(IModel model) {
        return ((FormulationModel)model).getEnumValue().equals(FormulationEnum.Custom);
    }

    @Override
    protected JPanel getOverview(IModel model) {
        JPanel panel = new JPanel();
        if (model instanceof FormulationModel) {
            FormulationModel formulationModel = (FormulationModel) model;
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.add(newLine("Polarization", StringHelper.toDisplayString(formulationModel.polarization)));//todo localization
            panel.add(newLine("Interaction", StringHelper.toDisplayString(formulationModel.interaction)));//todo localization
            panel.add(newLine("Scattering quantities", StringHelper.toDisplayString(formulationModel.scatteringQuantities)));//todo localization
        }
        return panel;
    }
    private JPanel newLine(String name, Object value) {
        JPanel line = new JPanel();
        line.add(new JLabel(name + ": "));
        line.add(new JLabel(value.toString()));
        return line;
    }


}