package adda.item.tab.shape.dipoleShape;

import adda.base.models.IModel;
import adda.base.views.ViewDialogBase;

import javax.swing.*;

public class DipoleShapeView extends ViewDialogBase {
    @Override
    protected boolean isShowAdditionalPanel(IModel model) {
        if (model instanceof DipoleShapeModel) {
            return DipoleShapeEnum.Rect.equals(((DipoleShapeModel) model).getEnumValue());
        }
        return false;
    }

    @Override
    protected JPanel getOverview(IModel model) {
        JPanel panel = new JPanel();
        if (model instanceof DipoleShapeModel) {
            DipoleShapeModel dipoleShapeModel = (DipoleShapeModel) model;

            panel.add(new JLabel(String.format("<x>:%s, <y>:%s, <z>:%s", dipoleShapeModel.getScaleX(), dipoleShapeModel.getScaleY(), dipoleShapeModel.getScaleZ())));//todo localization

        }
        return panel;
    }


}