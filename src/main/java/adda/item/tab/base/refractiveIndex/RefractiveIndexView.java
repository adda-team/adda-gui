package adda.item.tab.base.refractiveIndex;

import adda.application.controls.JComplexNumberInput;
import adda.application.controls.VerticalLayout;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.views.ViewBase;
import adda.base.views.ViewDialogBase;
import adda.item.tab.internals.formulation.FormulationEnum;
import adda.item.tab.internals.formulation.FormulationModel;
import adda.item.tab.internals.formulation.InteractionEnum;
import adda.item.tab.internals.formulation.PolarizationEnum;
import adda.utils.StringHelper;
import net.java.balloontip.BalloonTip;
import net.java.balloontip.styles.RoundedBalloonStyle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RefractiveIndexView extends ViewDialogBase {


    protected JComplexNumberInput complexNumberInput;

    public JComplexNumberInput getComplexNumberInput() {
        return complexNumberInput;
    }

    @Override
    protected void initLabel(IModel model) {

    }

    @Override
    protected void initPanel() {
        super.initPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        //panel.setLayout(new VerticalLayout(1));
        panel.setMinimumSize(new Dimension(220, 10));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

    }


    @Override
    protected void initFromModelInner(IModel model) {
        super.initFromModelInner(model);
        if (components.containsKey(RefractiveIndexModel.IS_ANISOTROP_FIELD_NAME) && model instanceof RefractiveIndexModel) {

            final RefractiveIndexModel refractiveIndexModel = (RefractiveIndexModel) model;
            final JCheckBox checkbox = (JCheckBox) components.get(RefractiveIndexModel.IS_ANISOTROP_FIELD_NAME);

            setAnisotropVisibility(refractiveIndexModel, checkbox);


            checkbox.addMouseListener(new MouseAdapter() {

                volatile BalloonTip balloonTip;

                @Override
                public void mouseEntered(MouseEvent me) {
                    if (!refractiveIndexModel.isEnabledAnisotrop()
                            && (balloonTip == null || !balloonTip.isVisible())
                            && refractiveIndexModel.getValidationErrors().containsKey(RefractiveIndexModel.IS_ENABLED_ANISOTROP_FIELD_NAME)
                            && !StringHelper.isEmpty(refractiveIndexModel.getValidationErrors().get(RefractiveIndexModel.IS_ENABLED_ANISOTROP_FIELD_NAME))
                    ) {
                        RoundedBalloonStyle style = new RoundedBalloonStyle(5, 5, Color.WHITE, Color.black);
                        balloonTip = new BalloonTip(
                                checkbox,
                                new JLabel(refractiveIndexModel.getValidationErrors().get(RefractiveIndexModel.IS_ENABLED_ANISOTROP_FIELD_NAME)),
                                style,
                                BalloonTip.Orientation.RIGHT_ABOVE,
                                BalloonTip.AttachLocation.NORTHWEST,
                                30, 10,
                                false
                        );
//                    Icon clearIcon = IconFontSwing.buildIcon(FontAwesome.TIMES_CIRCLE, 12, Color.DARK_GRAY);
//                    JButton button = new JButton();
//                    button.setBorder(BorderFactory.createEmptyBorder(5, 6, 2, 5));
//                    button.setBorderPainted(false);
//                    button.setFocusPainted(false);
//                    button.setOpaque(false);
//                    button.setContentAreaFilled(false);
//                    button.setIcon(clearIcon);
//                    balloonTip.setCloseButton(button, true);
                    }

                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (balloonTip != null) {
                        balloonTip.closeBalloon();
                        balloonTip = null;
                    }
                }
            });

            complexNumberInput = new JComplexNumberInput();
            complexNumberInput.setRealPart(refractiveIndexModel.getRealX());
            complexNumberInput.setImagPart(refractiveIndexModel.getImagX());

            complexNumberInput.setVisible(!refractiveIndexModel.isAnisotrop());
            complexNumberInput.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(complexNumberInput);
            setBorder(model);



        }

    }

    private void setAnisotropVisibility(RefractiveIndexModel refractiveIndexModel, JCheckBox checkbox) {
        if (!refractiveIndexModel.isEnabledAnisotrop()) {
            if (refractiveIndexModel.getValidationErrors().containsKey(RefractiveIndexModel.IS_ENABLED_ANISOTROP_FIELD_NAME)
                && !StringHelper.isEmpty(refractiveIndexModel.getValidationErrors().get(RefractiveIndexModel.IS_ENABLED_ANISOTROP_FIELD_NAME))
            ) {
                checkbox.setEnabled(refractiveIndexModel.isEnabledAnisotrop());
            } else {
                checkbox.setVisible(refractiveIndexModel.isEnabledAnisotrop());
            }
        } else {
            checkbox.setEnabled(refractiveIndexModel.isEnabledAnisotrop());
            checkbox.setVisible(refractiveIndexModel.isEnabledAnisotrop());
        }
    }

    private void setBorder(IModel model) {
        if (outerPanel != null) {
            outerPanel.setBorder(BorderFactory.createTitledBorder(model.getLabel()));
        }
    }

    @Override
    protected boolean isShowAdditionalPanel(IModel model) {
        if (model instanceof RefractiveIndexModel) {
            RefractiveIndexModel refractiveIndexModel = (RefractiveIndexModel) model;
            return refractiveIndexModel.isAnisotrop();
        }
        return false;
    }

    @Override
    protected JPanel getOverview(IModel model) {
        JPanel panel = new JPanel();
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (model instanceof RefractiveIndexModel) {
            RefractiveIndexModel refractiveIndexModel = (RefractiveIndexModel) model;
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.add(new JLabel(String.format("OX: %s + i%s", StringHelper.toDisplayString(refractiveIndexModel.getRealX()), StringHelper.toDisplayString(refractiveIndexModel.getImagX()))));//todo localization
            panel.add(new JLabel(String.format("OY: %s + i%s", StringHelper.toDisplayString(refractiveIndexModel.getRealY()), StringHelper.toDisplayString(refractiveIndexModel.getImagY()))));//todo localization
            panel.add(new JLabel(String.format("OZ: %s + i%s", StringHelper.toDisplayString(refractiveIndexModel.getRealZ()), StringHelper.toDisplayString(refractiveIndexModel.getImagZ()))));//todo localization
        }
        return panel;
    }

    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
        super.modelPropertyChanged(sender, event);

        if ("label".equals(event.getPropertyName())) {
            setBorder(sender);
            return;
        }

        if (sender instanceof RefractiveIndexModel) {

            RefractiveIndexModel refractiveIndexModel = (RefractiveIndexModel) sender;
            if (RefractiveIndexModel.IS_ENABLED_ANISOTROP_FIELD_NAME.equals(event.getPropertyName()) && components.containsKey(RefractiveIndexModel.IS_ANISOTROP_FIELD_NAME)) {
                setAnisotropVisibility(refractiveIndexModel, (JCheckBox)  components.get(RefractiveIndexModel.IS_ANISOTROP_FIELD_NAME));
//                components.get(RefractiveIndexModel.IS_ANISOTROP_FIELD_NAME).setVisible(refractiveIndexModel.isEnabledAnisotrop());
            }

            complexNumberInput.setVisible(!refractiveIndexModel.isAnisotrop());

            if (complexNumberInput.getRealPart() != refractiveIndexModel.getRealX()) {
                complexNumberInput.setRealPart(refractiveIndexModel.getRealX());
            }

            if (complexNumberInput.getImagPart() != refractiveIndexModel.getImagX()) {
                complexNumberInput.setImagPart(refractiveIndexModel.getImagX());
            }


        }


    }
}