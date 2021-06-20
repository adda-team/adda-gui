package adda.item.root.lineChart;

import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.views.ViewBase;
import adda.utils.StringHelper;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.chart.util.LogFormat;
import org.jfree.data.Range;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.text.AttributedString;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

public class LineChartView extends ViewBase {

    JLabel displayNameLabel;
    JTextPane descriptionTextPane;

    JComboBox<String> oxComboBox;
    JComboBox<String> oyComboBox;

    JCheckBox logCheckBox;

    JPanel chartWrapperPanel;



    public JLabel getDisplayNameLabel() {
        return displayNameLabel;
    }

    public JTextPane getDescriptionTextPane() {
        return descriptionTextPane;
    }

    public JComboBox<String> getOxComboBox() {
        return oxComboBox;
    }

    public JComboBox<String> getOyComboBox() {
        return oyComboBox;
    }

    public JCheckBox getLogCheckBox() {
        return logCheckBox;
    }

    public JPanel getChartWrapperPanel() {
        return chartWrapperPanel;
    }

    protected void initPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        this.panel = panel;
    }
    public void initFromModel(IModel model) {
        initPanel();

        LineChartModel lineChartModel = (LineChartModel) model;
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        displayNameLabel = new JLabel();
        displayNameLabel.setFont(new Font(displayNameLabel.getName(), Font.BOLD, 20));
        displayNameLabel.setText(lineChartModel.getDisplayName());
        displayNameLabel.setHorizontalAlignment(JLabel.CENTER);

        topPanel.add(displayNameLabel, BorderLayout.NORTH);

        descriptionTextPane = new JTextPane ();

        descriptionTextPane.setText(lineChartModel.getDescription());
        //descriptionTextArea.setWrapStyleWord(true);
        descriptionTextPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);;
        //descriptionTextArea.setLineWrap(true);
        descriptionTextPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionTextPane.setOpaque(false);
        descriptionTextPane.setEditable(false);
        descriptionTextPane.setFocusable(true);
        descriptionTextPane.setBackground(UIManager.getColor("Label.background"));
        descriptionTextPane.setFont(UIManager.getFont("Label.font"));
        descriptionTextPane.setBorder(UIManager.getBorder("Label.border"));
        descriptionTextPane.setAlignmentY(Component.CENTER_ALIGNMENT);
        descriptionTextPane.setBorder(BorderFactory.createEmptyBorder(0,50,0,50));
        StyledDocument style = descriptionTextPane.getStyledDocument();
        SimpleAttributeSet align= new SimpleAttributeSet();
        StyleConstants.setAlignment(align, StyleConstants.ALIGN_CENTER);
        style.setParagraphAttributes(0, style.getLength(), align, false);

        topPanel.add(descriptionTextPane, BorderLayout.SOUTH);

        JPanel managePanel = new JPanel();
        managePanel.setLayout(new FlowLayout());
        managePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        oxComboBox = new JComboBox<String>();
        if (null != lineChartModel.getHeaders()) {
            oxComboBox.setModel(new DefaultComboBoxModel<>(lineChartModel.getHeaders()));
            oxComboBox.setSelectedIndex(lineChartModel.getOxIndex());
        }



        oyComboBox = new JComboBox<String>();
        if (null != lineChartModel.getHeaders()) {
            oyComboBox.setModel(new DefaultComboBoxModel<>(lineChartModel.getHeaders()));
            oyComboBox.setSelectedIndex(lineChartModel.getOyIndex());
        }


        logCheckBox = new JCheckBox();
        logCheckBox.setText(StringHelper.toDisplayString("log scale"));
        logCheckBox.setSelected(lineChartModel.isLog());



        managePanel.add(new JLabel("OX:"));
        managePanel.add(oxComboBox);
        managePanel.add(new JLabel("OY:"));
        managePanel.add(oyComboBox);
        managePanel.add(logCheckBox);

        topPanel.add(managePanel);

        panel.add(topPanel, BorderLayout.NORTH);
        chartWrapperPanel = new JPanel();
        panel.add(chartWrapperPanel);
        refreshChart(model);


    }

    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
        LineChartModel lineChartModel = (LineChartModel) sender;
        boolean needToRefreshChart = true;
        if (LineChartModel.HEADERS_FIELD_NAME.equals(event.getPropertyName())) {
            oxComboBox.setModel(new DefaultComboBoxModel<>(lineChartModel.getHeaders()));
            oxComboBox.setSelectedIndex(lineChartModel.getOxIndex());

            oyComboBox.setModel(new DefaultComboBoxModel<>(lineChartModel.getHeaders()));
            oyComboBox.setSelectedIndex(lineChartModel.getOyIndex());
        }

        if (LineChartModel.OX_INDEX_FIELD_NAME.equals(event.getPropertyName())) {
            oxComboBox.setSelectedIndex(lineChartModel.getOxIndex());
        }

        if (LineChartModel.OY_INDEX_FIELD_NAME.equals(event.getPropertyName())) {
            oyComboBox.setSelectedIndex(lineChartModel.getOyIndex());
        }

        if (LineChartModel.IS_LOG_FIELD_NAME.equals(event.getPropertyName())) {
            logCheckBox.setSelected(lineChartModel.isLog());
        }

        if (LineChartModel.DISPLAY_NAME_FIELD_NAME.equals(event.getPropertyName())) {
            displayNameLabel.setText(lineChartModel.getDisplayName());
            needToRefreshChart = false;
        }

        if (LineChartModel.DESCRIPTION_FIELD_NAME.equals(event.getPropertyName())) {
            descriptionTextPane.setText(lineChartModel.getDescription());
            needToRefreshChart = false;
        }

        if (needToRefreshChart) {
            refreshChart(sender);
        }

    }

    public void refreshChart(IModel model) {
        LineChartModel lineChartModel = (LineChartModel) model;
        final int oxIndex = lineChartModel.getOxIndex();
        final int oyIndex = lineChartModel.getOyIndex();
        if (null != lineChartModel.getHeaders()
                && null != lineChartModel.getData()
                && oxIndex > -1
                && oyIndex > -1
        ) {
            XYSeries series = new XYSeries(lineChartModel.getHeaders()[oyIndex]);
            double maxY = Double.NEGATIVE_INFINITY;
            double minY = Double.POSITIVE_INFINITY;
            for (int i = 0; i < lineChartModel.getData()[0].length; i++) {
                final double x = lineChartModel.getData()[oxIndex][i];
                final double y = lineChartModel.getData()[oyIndex][i];
                series.add(x, y);
                if (y > maxY) {
                    maxY = y;
                }
                if (y < minY) {
                    minY = y;
                }
            }
            NumberAxis xAxis = new NumberAxis(lineChartModel.getHeaders()[oxIndex]);
            xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

            ValueAxis yAxis;
            if (lineChartModel.isLog()) {
                if (minY > 0) {
                    LogAxis logAxis = new APPHLogAxis(lineChartModel.getHeaders()[oyIndex]);
                    //LogAxis logAxis = new LogAxis(lineChartModel.getHeaders()[oyIndex]);
                    //logAxis.setRange(Math.pow(10, Math.floor(Math.log10(minY))) * 0.95, Math.pow(10, Math.ceil(Math.log10(maxY))) * 1.05);
                    //logAxis.setBase(10);
                    //logAxis.setTickUnit(new LogTickU(10));
                    logAxis.setMinorTickMarksVisible(false);
                    yAxis = logAxis;
                } else {
                    LogAxis logAxis = new LogAxis(lineChartModel.getHeaders()[oyIndex]);
                    logAxis.setMinorTickMarksVisible(false);
                    yAxis = logAxis;
                }
            } else {
                yAxis = new NumberAxis(lineChartModel.getHeaders()[oyIndex]);
            }




//            yAxis.setBase(10);
//            yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            XYPlot plot = new XYPlot(new XYSeriesCollection(series),
                    xAxis, yAxis, new XYLineAndShapeRenderer(true, false));
            JFreeChart chart = new JFreeChart(null, null, plot, false);
            chartWrapperPanel.removeAll();
            final ChartPanel cp = new ChartPanel(chart);
            cp.setPreferredSize(new java.awt.Dimension(chartWrapperPanel.getWidth(), chartWrapperPanel.getHeight()));
            cp.setSize(new java.awt.Dimension(chartWrapperPanel.getWidth(), Math.max(chartWrapperPanel.getHeight(), 350)));
            cp.setMaximumDrawHeight(350);
            chartWrapperPanel.add(cp);
            chartWrapperPanel.revalidate();
            chartWrapperPanel.repaint();
        }
    }

    public class APPHLogAxis extends LogAxis {
        private static final long serialVersionUID = 1L;

        public APPHLogAxis(String title) {
            super(title);
            final DecimalFormatSymbols newSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
            newSymbols.setExponentSeparator("E");
            newSymbols.setDecimalSeparator('.');
            final DecimalFormat decForm = new DecimalFormat("0.#E0#");
            decForm.setDecimalFormatSymbols(newSymbols);
            setNumberFormatOverride(decForm);
        }

        protected java.util.List refreshTicksVertical(Graphics2D g2, Rectangle2D dataArea, RectangleEdge edge) {
            Range range = getRange();
            java.util.List ticks = new ArrayList<NumberTick>();
            double start = Math.floor(calculateLog(getLowerBound()));
            double end = Math.ceil(calculateLog(getUpperBound()));
            for(int i = (int)start; i < end; i++) {
                double v = Math.pow(this.getBase(), i);
                for(double j = 1; j <= this.getBase(); j++) {
                    String l = getNumberFormatOverride().format(j * v);
                    if(j != this.getBase()) l = "";
                    if(range.contains(j * v)) {
                        ticks.add(new NumberTick(j * v, l, TextAnchor.BASELINE_RIGHT, TextAnchor.BASELINE_RIGHT, 0.0));
                    }
                }
//                AttributedString l = createTickLabel(v);
//                if (range.contains(v)) {
//                    ticks.add(new NumberTick(v, l.toString(), TextAnchor.TOP_CENTER, TextAnchor.CENTER, 0.0));
//                }
            }
            return ticks;
        }
    }
}