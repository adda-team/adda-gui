package adda.item.tab.base.refractiveIndexAggregator;

import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.models.ModelBase;
import adda.base.views.ViewBase;
import adda.item.tab.shape.selector.ShapeDomainInfo;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RefractiveIndexAggregatorView extends ViewBase {

//    @Override
//    protected void initPanel() {
//        JPanel panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//        this.panel = panel;
//    }

    private List<JPanel> shapePanels = new ArrayList<>();
    private JPanel granulPanel;
    private JPanel surfacePanel;
    int maxAvailableDomains = 2;

    @Override
    protected void initFromModelInner(IModel model) {
//        super.initFromModelInner(model);
        if(model instanceof RefractiveIndexAggregatorModel) {
            RefractiveIndexAggregatorModel refractiveIndexAggregatorModel = (RefractiveIndexAggregatorModel) model;

            JPanel boxPanel = new JPanel();
            boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));

            int maxAvailableDomains = Math.min(this.maxAvailableDomains, refractiveIndexAggregatorModel.getShapeBoxes().size());
            int maxIndexToShow = refractiveIndexAggregatorModel.getShapeModel() != null ? refractiveIndexAggregatorModel.getShapeModel().getShapeDomainInfos().size() : -1;
            for (int i = 0; i < maxAvailableDomains; i++) {
                final JPanel wrapper = new JPanel(new FlowLayout());
                wrapper.setVisible(refractiveIndexAggregatorModel.isShowShape() && i < maxIndexToShow);

                refractiveIndexAggregatorModel.getShapeBoxes().get(i).init();

                wrapper.add(refractiveIndexAggregatorModel.getShapeBoxes().get(i).getLayout());

                shapePanels.add(wrapper);
                boxPanel.add(wrapper);
            }

            granulPanel = new JPanel(new FlowLayout());
            granulPanel.setVisible(refractiveIndexAggregatorModel.isShowGranul());
            refractiveIndexAggregatorModel.getGranulBox().init();
            granulPanel.add(refractiveIndexAggregatorModel.getGranulBox().getLayout());
            boxPanel.add(granulPanel);

            surfacePanel = new JPanel(new FlowLayout());
            surfacePanel.setVisible(refractiveIndexAggregatorModel.isShowSurface());
            refractiveIndexAggregatorModel.getSurfaceBox().init();
            surfacePanel.add(refractiveIndexAggregatorModel.getSurfaceBox().getLayout());
            boxPanel.add(surfacePanel);
            panel.add(boxPanel);

        }

    }

    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
//        super.modelPropertyChanged(sender, event);
        if(sender instanceof RefractiveIndexAggregatorModel) {
            RefractiveIndexAggregatorModel refractiveIndexAggregatorModel = (RefractiveIndexAggregatorModel) sender;
            granulPanel.setVisible(refractiveIndexAggregatorModel.isShowGranul());
            surfacePanel.setVisible(refractiveIndexAggregatorModel.isShowSurface());

            int maxIndexToShow = refractiveIndexAggregatorModel.getShapeModel() != null ? refractiveIndexAggregatorModel.getShapeModel().getShapeDomainInfos().size() : -1;

            for (int i = 0; i < shapePanels.size(); i++) {
                shapePanels.get(i).setVisible(refractiveIndexAggregatorModel.isShowShape() && i < maxIndexToShow);
                if (i < maxIndexToShow) {
                    ShapeDomainInfo info = refractiveIndexAggregatorModel.getShapeModel().getShapeDomainInfos().get(i);
                    ((ModelBase) refractiveIndexAggregatorModel.getShapeBoxes().get(i).getModel()).setLabel(info.getName());
                }
            }
        }
    }
}