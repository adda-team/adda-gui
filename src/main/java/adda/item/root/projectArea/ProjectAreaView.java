package adda.item.root.projectArea;

import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.views.ViewBase;
import adda.item.root.lineChart.LineChartBox;
import adda.item.root.lineChart.LineChartModel;
import adda.utils.StringHelper;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;

public class ProjectAreaView extends ViewBase {

    public JButton closeButton;
    public JTextArea textArea;
    public JTabbedPane tabbedPane;
    public JPanel resultPanel;

    @Override
    public void initFromModel(IModel model) {
//        initPanel();
//        initLabel(model);
//        initFromModelInner(model);
        JPanel popupPanel = new JPanel(new BorderLayout());
        popupPanel.setOpaque(false);

        int padding = 60;

        JPanel bufferPanel = createBufferPanel(padding);
        bufferPanel.add(createBufferTextarea());
        popupPanel.add(bufferPanel, BorderLayout.SOUTH);

        bufferPanel = createBufferPanel(padding);
        bufferPanel.add(createBufferTextarea());
        popupPanel.add(bufferPanel, BorderLayout.EAST);

        bufferPanel = createBufferPanel(padding);
        bufferPanel.add(createBufferTextarea());
        popupPanel.add(bufferPanel, BorderLayout.WEST);
        popupPanel.setBorder(new LineBorder(Color.gray));
        popupPanel.setVisible(false);

        JTextArea textArea = new JTextArea();
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        textArea.setOpaque(true);
        textArea.setEditable(false);

        textArea.setFocusable(true);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.white);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));

        JScrollPane scroll = new JScrollPane(textArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JPanel terminalPanel = new JPanel(new BorderLayout());
        terminalPanel.add(scroll, BorderLayout.CENTER);

        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(-855310));
        //tabbedPane.setBackground(new Color(255, 255, 255, 190));
        tabbedPane.addTab(StringHelper.toDisplayString("Execution output"), terminalPanel);

        resultPanel = new JPanel(new BorderLayout());

        tabbedPane.addTab(StringHelper.toDisplayString("Result"), resultPanel);
        tabbedPane.setEnabledAt(1, false);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(tabbedPane);
        //centerPanel.setBackground(new Color(255, 255, 255, 190));
        centerPanel.setBackground(new Color(-855310));

        popupPanel.add(centerPanel, BorderLayout.CENTER);

        bufferPanel = createBufferPanel(padding);
        //textarea stop event propagation to back panel
        //todo think about better workaround
        bufferPanel.add(createBufferTextarea());

        IconFontSwing.register(FontAwesome.getIconFont());
        JButton popupCloseButton = new JButton("Close");
        popupCloseButton.setIcon(IconFontSwing.buildIcon(FontAwesome.TIMES, 17, Color.DARK_GRAY));
        //popupCloseButton.setHorizontalAlignment(SwingConstants.RIGHT);
//        popupCloseButton.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 5));
//        popupCloseButton.setBorderPainted(false);
//        popupCloseButton.setOpaque(false);
        //popupCloseButton.setContentAreaFilled(false);
        popupCloseButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        //bottomPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        bottomPanel.add(popupCloseButton);
        bottomPanel.setBackground(new Color(255, 255, 255, 190));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15,0,0,20));

        JPanel overlayPanel = new JPanel() {
            @Override
            public boolean isOptimizedDrawingEnabled() {
                return false;
            }
        };
        overlayPanel.setLayout(new OverlayLayout(overlayPanel));
        overlayPanel.add(bottomPanel);
        overlayPanel.add(bufferPanel);
        overlayPanel.setBackground(new Color(255, 255, 255, 0));

        popupPanel.add(overlayPanel, BorderLayout.NORTH);
        //popupPanel.setBackground(new Color(255, 255, 255, 0));
//        popupCloseButton.addActionListener(e -> {
//            popupPanel.setVisible(false);
//        });
        this.textArea = textArea;
        closeButton = popupCloseButton;
        panel = popupPanel;
    }

    private static JPanel createBufferPanel(int minSize) {
        JPanel panel = new JPanel(new BorderLayout());
        //panel.add(new JLabel("test"));

        panel.setMinimumSize(new Dimension(minSize, minSize));
        panel.setPreferredSize(new Dimension(minSize, minSize));
        panel.setBackground(new Color(255, 255, 255, 190));

        return panel;
    }

    private static JTextArea createBufferTextarea() {
        JTextArea textArea = new JTextArea();
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        textArea.setOpaque(true);
        textArea.setEditable(false);

        textArea.setFocusable(false);
        textArea.setBackground(new Color(255, 255, 255, 190));
        textArea.setForeground(new Color(255, 255, 255, 190));
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        return textArea;
    }

    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
        if (ProjectAreaModel.IS_RUNNING_FIELD_NAME.equals(event.getPropertyName())) {
            panel.setVisible((Boolean) event.getPropertyValue());
        }
        if (ProjectAreaModel.IS_SUCCESSFULLY_FINISHED_FIELD_NAME.equals(event.getPropertyName())) {
            ProjectAreaModel projectAreaModel = (ProjectAreaModel) sender;
            final String pathname = projectAreaModel.getPathToState() + "/mueller";
            if ((new File(pathname).exists())) {
                LineChartBox lineChartBox = new LineChartBox();
                lineChartBox.init();
                final LineChartModel lineChartModel = (LineChartModel) lineChartBox.getModel();
                lineChartModel.setDisplayName("mueller");
                lineChartModel.setDescription(pathname);
                lineChartModel.loadFromFileAsync(pathname);
                lineChartBox.getLayout().setMinimumSize(new Dimension(resultPanel.getWidth(), 350));

                tabbedPane.setEnabledAt(1, true);
                tabbedPane.setSelectedIndex(1);

                resultPanel.removeAll();
                resultPanel.add(lineChartBox.getLayout(), BorderLayout.CENTER);

                panel.revalidate();
                panel.repaint();

                tabbedPane.revalidate();
                tabbedPane.repaint();

                resultPanel.revalidate();
                resultPanel.repaint();


            }

        }
    }

}
