package adda.item.tab.shape.orientation.avarage;

import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.views.ViewBase;
import adda.utils.StringHelper;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class OrientationAverageView extends ViewBase {

    JTextArea fileNameArea;

    JButton fileOpenButton;
    JButton clearButton;

    public JButton getClearFileButton() {
        return clearButton;
    }

    public JButton getFileOpenButton() {
        return fileOpenButton;
    }

    @Override
    protected void initPanel() {
        this.panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    }

    @Override
    protected void initLabel(IModel model) {

    }

    @Override
    protected void initFromModelInner(IModel model) {

        if (model instanceof OrientationAverageModel) {
            OrientationAverageModel averageModel = (OrientationAverageModel) model;
            IconFontSwing.register(FontAwesome.getIconFont());

            panel.add(new JLabel("Use file"));//todo localization

            Icon fileOpenIcon = IconFontSwing.buildIcon(FontAwesome.FOLDER_OPEN, 17, Color.DARK_GRAY);
            JButton fileOpenButton = new JButton(fileOpenIcon);
            fileOpenButton.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 5));
            fileOpenButton.setBorderPainted(false);
            fileOpenButton.setOpaque(false);
            fileOpenButton.setContentAreaFilled(false);
            this.fileOpenButton = fileOpenButton;
            panel.add(fileOpenButton);

            JTextArea textArea = new JTextArea();
            textArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
            textArea.setText(averageModel.getAverageFile());
            textArea.setWrapStyleWord(false);
            textArea.setLineWrap(false);
            textArea.setAlignmentX(Component.CENTER_ALIGNMENT);
            textArea.setOpaque(false);
            textArea.setEditable(false);
            textArea.setFocusable(true);
            textArea.setBackground(UIManager.getColor("Label.background"));
            textArea.setFont(UIManager.getFont("Label.font"));
            textArea.setBorder(UIManager.getBorder("Label.border"));
            textArea.setAlignmentY(Component.CENTER_ALIGNMENT);

            JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setMaximumSize(new Dimension(220, 25));
            scrollPane.setPreferredSize(new Dimension(220, 25));
            scrollPane.setBorder(BorderFactory.createEmptyBorder());



            scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 5));
            scrollPane.getHorizontalScrollBar().setUI(new BasicScrollBarUI()
            {
                @Override
                protected JButton createDecreaseButton(int orientation) {
                    return createZeroButton();
                }

                @Override
                protected JButton createIncreaseButton(int orientation) {
                    return createZeroButton();
                }

                private JButton createZeroButton() {
                    JButton jbutton = new JButton();
                    jbutton.setPreferredSize(new Dimension(0, 0));
                    jbutton.setMinimumSize(new Dimension(0, 0));
                    jbutton.setMaximumSize(new Dimension(0, 0));
                    return jbutton;
                }
            });



            panel.add(scrollPane);
            fileNameArea = textArea;

            Icon clearIcon = IconFontSwing.buildIcon(FontAwesome.TIMES_CIRCLE, 15, Color.DARK_GRAY);
            JButton clearButton = new JButton(clearIcon);
            clearButton.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 5));
            clearButton.setBorderPainted(false);
            clearButton.setContentAreaFilled(false);
            clearButton.setOpaque(false);
            this.clearButton = clearButton;
            this.clearButton.setVisible(!StringHelper.isEmpty(averageModel.getAverageFile()));
            panel.add(clearButton);

        }
    }

    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
        if (sender instanceof OrientationAverageModel) {
            final String averageFile = ((OrientationAverageModel) sender).getAverageFile();
            fileNameArea.setText(averageFile);
            clearButton.setVisible(!StringHelper.isEmpty(averageFile));
        }
    }
}