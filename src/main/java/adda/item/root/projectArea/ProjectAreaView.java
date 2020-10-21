package adda.item.root.projectArea;

import adda.application.controls.ComboBoxItem;
import adda.application.controls.JNumericField;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.views.ViewBase;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Map;

public class ProjectAreaView extends ViewBase {

    public JButton closeButton;
    public JTextArea textArea;

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
        popupPanel.add(bufferPanel, BorderLayout.NORTH);

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

        popupPanel.add(scroll, BorderLayout.CENTER);

        bufferPanel = createBufferPanel(padding);
        //textarea stop event propagation to back panel
        //todo think about better workaround
        bufferPanel.add(createBufferTextarea());

        JButton popupCloseButton = new JButton("Close");
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(popupCloseButton);
        bottomPanel.setBackground(new Color(255, 255, 255, 190));

        JPanel overlayPanel = new JPanel(){
            @Override
            public boolean isOptimizedDrawingEnabled() {
                return false;
            }
        };
        overlayPanel.setLayout(new OverlayLayout(overlayPanel));
        overlayPanel.add(bottomPanel);
        overlayPanel.add(bufferPanel);
        overlayPanel.setBackground(new Color(255, 255, 255, 0));

        popupPanel.add(overlayPanel, BorderLayout.SOUTH);

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
    }

}
