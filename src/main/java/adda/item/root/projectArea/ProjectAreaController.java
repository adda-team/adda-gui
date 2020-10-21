package adda.item.root.projectArea;

import adda.base.controllers.ControllerBase;
import adda.item.tab.shape.orientation.avarage.OrientationAverageView;
import adda.utils.OutputDisplayer;

public class ProjectAreaController extends ControllerBase {

    protected void createAndBindListenersFromView() {
        if (view instanceof ProjectAreaView) {
            ProjectAreaView projectAreaView = (ProjectAreaView) view;
            ProjectAreaModel projectAreaModel = (ProjectAreaModel) model;
            projectAreaModel.setOutputDisplayer(new OutputDisplayer(projectAreaView.textArea));
            projectAreaView.closeButton.addActionListener(e -> projectAreaModel.stop());
        }
    }
}
