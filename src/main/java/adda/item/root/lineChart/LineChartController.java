package adda.item.root.lineChart;

import adda.base.controllers.ControllerBase;

public class LineChartController extends ControllerBase {
    protected void createAndBindListenersFromView() {
        LineChartModel lineChartModel = (LineChartModel) model;
        LineChartView lineChartView = (LineChartView) view;

        lineChartView.getLogCheckBox().addActionListener(e ->
            lineChartModel.setLog(lineChartView.getLogCheckBox().isSelected())
        );

        lineChartView.getOxComboBox().addActionListener(e ->
            lineChartModel.setOxIndex(lineChartView.getOxComboBox().getSelectedIndex())
        );

        lineChartView.getOyComboBox().addActionListener(e ->
                lineChartModel.setOyIndex(lineChartView.getOyComboBox().getSelectedIndex())
        );
    }
}