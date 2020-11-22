package adda.item.tab.base.beam;

import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.views.ViewBase;

import javax.swing.*;
import java.awt.*;

public class BeamView extends ViewBase {

    private JPanel outerPanel;

    @Override
    public JComponent getRootComponent() {
        return this.outerPanel;
    }

    protected void initPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        //panel.setLayout(new VerticalLayout());
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setAlignmentY(Component.TOP_ALIGNMENT);
        this.panel = panel;
    }

    JLabel widthLabel = new JLabel();
    JLabel xLabel = new JLabel();
    JLabel yLabel = new JLabel();
    JLabel zLabel = new JLabel();

    @Override
    public void initFromModel(IModel model) {
//        outerPanel = getWrapperPanel();
        outerPanel = new JPanel(new BorderLayout());

        initPanel();

        final JLabel label = createLabel(model);
        label.setAlignmentY(Component.TOP_ALIGNMENT);
        JPanel wrapper = new JPanel();
        BoxLayout layout = new BoxLayout(wrapper, BoxLayout.LINE_AXIS);
        wrapper.setLayout(layout);
        //wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.add(label);
        wrapper.setBorder(BorderFactory.createEmptyBorder(5,5,0,0));


        outerPanel.add(wrapper, BorderLayout.WEST);
        components.put("label", label);

        initFromModelInner(model);

        JPanel p  = getWrapperPanel();
        p.add(panel);
        outerPanel.add(p);

        setMeasureLabels((BeamModel) model);

        wrappers.get(BeamModel.WIDTH_FIELD_NAME + ViewBase.WRAPPER_POSTFIX).add(widthLabel);
        wrappers.get(BeamModel.X_FIELD_NAME + ViewBase.WRAPPER_POSTFIX).add(xLabel);
        wrappers.get(BeamModel.Y_FIELD_NAME + ViewBase.WRAPPER_POSTFIX).add(yLabel);
        wrappers.get(BeamModel.Z_FIELD_NAME + ViewBase.WRAPPER_POSTFIX).add(zLabel);

    }


    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
        super.modelPropertyChanged(sender, event);
        if (sender instanceof BeamModel && BeamModel.MEASURE_FIELD_NAME.equals(event.getPropertyName())) {
            setMeasureLabels((BeamModel) sender);
        }
    }


    private void setMeasureLabels(BeamModel model) {
        String measure = model.getMeasure();

        widthLabel.setText(measure);
        xLabel.setText(measure);
        yLabel.setText(measure);
        zLabel.setText(measure);
    }
}