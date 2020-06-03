package adda.base.views;

import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;

import javax.swing.*;
import java.awt.*;

public abstract class ViewDialogBase extends ViewBase {

    protected JPanel outerPanel;
    protected JPanel additionalPanel;
    protected JPanel overviewPanel;

    protected JButton editButton;
    protected JButton clearButton;

    @Override
    public JComponent getRootComponent() {
        return this.outerPanel;
    }

    @Override
    protected void initPanel() {
        super.initPanel();
        JPanel outerPanel = new JPanel();
        outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.Y_AXIS));
        this.outerPanel = outerPanel;
        this.outerPanel.add(this.panel);

        JPanel additionalPanel = new JPanel();
        additionalPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        this.additionalPanel = additionalPanel;
        this.outerPanel.add(this.additionalPanel);

    }

    @Override
    public void initFromModel(IModel model) {
        initPanel();
        initLabel(model);
        initFromModelInner(model);
    }

    protected void initFromModelInner(IModel model) {
        super.initFromModelInner(model);
        IconFontSwing.register(FontAwesome.getIconFont());

        JPanel overviewPanel = new JPanel();
        overviewPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.overviewPanel = overviewPanel;
        additionalPanel.add(this.overviewPanel);

        Icon editIcon = IconFontSwing.buildIcon(FontAwesome.PENCIL_SQUARE_O, 17, Color.DARK_GRAY);
        JButton editButton = new JButton(editIcon);
        editButton.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 5));
        editButton.setBorderPainted(false);
        editButton.setOpaque(false);
        editButton.setContentAreaFilled(false);
        this.editButton = editButton;
        additionalPanel.add(editButton);

        Icon clearIcon = IconFontSwing.buildIcon(FontAwesome.TIMES_CIRCLE, 17, Color.DARK_GRAY);
        JButton clearButton = new JButton(clearIcon);
        clearButton.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 5));
        clearButton.setBorderPainted(false);
        clearButton.setOpaque(false);
        clearButton.setContentAreaFilled(false);
        this.clearButton = clearButton;
        additionalPanel.add(clearButton);

        showAdditionalInfo(model);


    }

    protected void showAdditionalInfo(IModel model) {

        boolean isShow = isShowAdditionalPanel(model);
        additionalPanel.setVisible(isShow);
        if (overviewPanel != null && isShow) {
            this.overviewPanel.removeAll();
            JPanel overview = getOverview(model);
            if (overview != null) {
                this.overviewPanel.add(overview);
                this.overviewPanel.repaint();
                this.overviewPanel.revalidate();
            }
        }
    }

    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
        super.modelPropertyChanged(sender, event);
        showAdditionalInfo(sender);
    }

    public JButton getEditButton() {
        return editButton;
    }

    public JButton getClearButton() {
        return clearButton;
    }

    protected abstract boolean isShowAdditionalPanel(IModel model);

    protected abstract JPanel getOverview(IModel model);



}