package adda.item.tab.shape.orientation.avarage.gamma;

import adda.application.controls.VerticalLayout;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.models.ModelBase;
import adda.base.views.ViewBase;

import javax.swing.*;
import java.awt.*;

public class GammaOrientationAverageView extends ViewBase {

    @Override
    protected void initLabel(IModel model) {

    }

    @Override
    protected JPanel getWrapperPanel() {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new FlowLayout(FlowLayout.RIGHT));
        return wrapper;
    }

    @Override
    protected void initPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        //panel.setLayout(new VerticalLayout());
        panel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        panel.setAlignmentY(Component.TOP_ALIGNMENT);
        this.panel = panel;
    }

    @Override
    protected void initFromModelInner(IModel model) {
        super.initFromModelInner(model);
        this.panel.setBorder(BorderFactory.createTitledBorder(model.getLabel()));


        //todo rework me!
        panel.add(Box.createVerticalGlue());
        panel.add(Box.createVerticalGlue());
        panel.add(Box.createVerticalGlue());
        panel.add(Box.createVerticalGlue());
        panel.add(Box.createVerticalGlue());
        panel.add(Box.createVerticalGlue());
        panel.add(Box.createVerticalGlue());
        panel.add(Box.createVerticalGlue());
        panel.add(Box.createVerticalGlue());


    }

    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
        super.modelPropertyChanged(sender, event);
        if (ModelBase.LABEL_FIELD_NAME.equals(event.getPropertyName())) {
            this.panel.setBorder(BorderFactory.createTitledBorder(sender.getLabel()));
        }
    }
}