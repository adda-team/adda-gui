package adda.item.tab.shape.dipoleShape;

import adda.application.controls.CustomOkCancelModalDialog;
import adda.application.controls.JNumericField;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.item.tab.shape.orientation.OrientationDialog;
import adda.utils.ListenerHelper;
import adda.utils.StringHelper;
import net.java.balloontip.BalloonTip;
import net.java.balloontip.styles.RoundedBalloonStyle;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class DipoleShapeDialog extends CustomOkCancelModalDialog {

    private JNumericField numericFieldX;
    private JNumericField numericFieldY;
    private JNumericField numericFieldZ;
    private JPanel wrapperPanel;
    private JLabel imageLabel;
    
    public DipoleShapeDialog(IModel model) {
        super(model, "Proportions of the sides of the rectangle");//todo localization
        setupLayout();
        if (!(model instanceof  DipoleShapeModel)) return ;

        DipoleShapeModel dipoleShapeModel = (DipoleShapeModel) model;

        numericFieldX.setMaxLength(20);
        numericFieldX.setPrecision(10);
        numericFieldX.setAllowNegative(false);
        numericFieldX.setDouble(dipoleShapeModel.getScaleX());
        numericFieldX.getDocument().addDocumentListener(ListenerHelper.getSimpleDocumentListener(() -> {
            if (!StringHelper.isEmpty(numericFieldX.getText())) {
                dipoleShapeModel.setScaleX(numericFieldX.getDouble());
            }
        }));
        numericFieldX.addFocusListener(ListenerHelper.getSimpleFocusListener(() -> {
            if (StringHelper.isEmpty(numericFieldX.getText())) {
                numericFieldX.setText("1.0");
                dipoleShapeModel.setScaleX(1.0);
            }
        }));

        numericFieldY.setMaxLength(20);
        numericFieldY.setPrecision(10);
        numericFieldY.setAllowNegative(false);
        numericFieldY.setDouble(dipoleShapeModel.getScaleY());
        numericFieldY.getDocument().addDocumentListener(ListenerHelper.getSimpleDocumentListener(() -> {
            if (!StringHelper.isEmpty(numericFieldY.getText())) {
                dipoleShapeModel.setScaleY(numericFieldY.getDouble());
            }
        }));
        numericFieldY.addFocusListener(ListenerHelper.getSimpleFocusListener(() -> {
            if (StringHelper.isEmpty(numericFieldY.getText())) {
                numericFieldY.setText("1.0");
                dipoleShapeModel.setScaleY(1.0);
            }
        }));


        numericFieldZ.setMaxLength(20);
        numericFieldZ.setPrecision(10);
        numericFieldZ.setAllowNegative(false);
        numericFieldZ.setDouble(dipoleShapeModel.getScaleZ());
        numericFieldZ.getDocument().addDocumentListener(ListenerHelper.getSimpleDocumentListener(() -> {
            if (!StringHelper.isEmpty(numericFieldZ.getText())) {
                dipoleShapeModel.setScaleZ(numericFieldZ.getDouble());
            }
        }));
        numericFieldZ.addFocusListener(ListenerHelper.getSimpleFocusListener(() -> {
            if (StringHelper.isEmpty(numericFieldZ.getText())) {
                numericFieldZ.setText("1.0");
                dipoleShapeModel.setScaleZ(1.0);
            }
        }));

        String path = "image/dialog/dipole/shape.png";
        URL imgURL = OrientationDialog.class.getClassLoader().getResource(path);
        if (imgURL != null) {
            imageLabel.setIcon(new ImageIcon(imgURL));
        }

        getDialogContentPanel().add(wrapperPanel);

        if (!model.validate()) {
            buttonOK.setEnabled(false);
            RoundedBalloonStyle style = new RoundedBalloonStyle(5, 5, Color.WHITE, Color.black);
            BalloonTip balloonTip = new BalloonTip(
                    buttonOK,
                    new JLabel(model.getValidationErrors().get(DipoleShapeModel.ENUM_VALUE_FIELD_NAME)),
                    style,
                    BalloonTip.Orientation.RIGHT_ABOVE,
                    BalloonTip.AttachLocation.NORTHWEST,
                    30, 10,
                    false
            );
        }
        
    }
    
    private void setupLayout() {
        wrapperPanel = new JPanel();
        wrapperPanel.setLayout(new GridBagLayout());
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 8;
        gbc.gridheight = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipady = 1;
        wrapperPanel.add(panel1, gbc);
        final JLabel label1 = new JLabel();
        label1.setText("<x>:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label1, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer1, gbc);
        numericFieldX = new JNumericField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(numericFieldX, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer2, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("<y>:");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label2, gbc);
        numericFieldY = new JNumericField();
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(numericFieldY, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer3, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("<z>:");
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label3, gbc);
        numericFieldZ = new JNumericField();
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(numericFieldZ, gbc);
        imageLabel = new JLabel();
        imageLabel.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        wrapperPanel.add(imageLabel, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        wrapperPanel.add(spacer4, gbc);
    }

    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
        
    }
}
