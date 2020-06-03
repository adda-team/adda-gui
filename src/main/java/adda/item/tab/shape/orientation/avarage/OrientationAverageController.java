package adda.item.tab.shape.orientation.avarage;

import adda.Context;
import adda.base.controllers.ControllerBase;
import adda.base.models.IModel;
import adda.base.views.ViewDialogBase;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class OrientationAverageController extends ControllerBase {

    @Override
    protected void createAndBindListenersFromView() {
        super.createAndBindListenersFromView();

        if (view instanceof OrientationAverageView) {
            OrientationAverageView orientationAverageView = (OrientationAverageView) view;
            if (orientationAverageView.getFileOpenButton() != null) {
                orientationAverageView.getFileOpenButton().addActionListener(e -> {

                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("ADDA orientation average");//todo localization
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("ADDA config", "dat");
                    fileChooser.setFileFilter(filter);

                    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    int result = fileChooser.showOpenDialog(Context.getInstance().getMainFrame());
                    if (result == JFileChooser.APPROVE_OPTION) {
                        ((OrientationAverageModel)model).setAverageFile(fileChooser.getSelectedFile().getAbsolutePath());
                    }


                });
            }

            if (orientationAverageView.getClearFileButton() != null) {
                orientationAverageView.getClearFileButton().addActionListener(e -> {
                    ((OrientationAverageModel)model).setAverageFile("");
                });
            }
        }
    }
}