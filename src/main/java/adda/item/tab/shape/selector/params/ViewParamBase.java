package adda.item.tab.shape.selector.params;

import adda.application.controls.VerticalLayout;
import adda.base.models.IModel;
import adda.base.views.ViewBase;

import javax.swing.*;
import java.awt.*;

public class ViewParamBase extends ViewBase {
    @Override
    protected void initPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        this.panel = panel;
    }
    @Override
    protected void initLabel(IModel model) {

    }

    @Override
    protected void initFromModelInner(IModel model) {
        super.initFromModelInner(model);

        //todo rework me!
        panel.add(Box.createVerticalGlue());
        panel.add(Box.createVerticalGlue());
        panel.add(Box.createVerticalGlue());
        panel.add(Box.createVerticalGlue());
        panel.add(Box.createVerticalGlue());
        panel.add(Box.createVerticalGlue());
    }
}