package adda.item.tab.shape.orientation;

import adda.application.controls.CustomOkCancelModalDialog;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.item.tab.shape.selector.ShapeSelectorView;
import com.google.common.primitives.Ints;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.Optional;

public class OrientationDialog extends CustomOkCancelModalDialog {
    private JSpinner spinnerAlpha;
    private JSpinner spinnerBeta;
    private JSpinner spinnerGamma;
    private JPanel wrapperPanel;
    private JLabel imageLabel;

    public OrientationDialog(IModel model) {
        super(model, "Orientation");//todo localization

        if (!(model instanceof OrientationModel)) return;
        //todo localization
        //$$$setupUI$$$();

        OrientationModel orientationModel = (OrientationModel) model;


        SpinnerModel spinnerModel = new SpinnerNumberModel(0, 0, 360, 1);

        spinnerAlpha.setModel(new SpinnerNumberModel(0, 0, 360, 1));
        spinnerAlpha.setPreferredSize(new Dimension(50, 20));
        spinnerAlpha.setValue((int) orientationModel.getAlpha());
        spinnerAlpha.addChangeListener(e -> {
            if (spinnerAlpha.getValue() != null) {
                orientationModel.setAlpha(Optional.ofNullable(spinnerAlpha.getValue().toString()).map(Ints::tryParse)
                        .orElse(0));
            }
        });

        spinnerBeta.setModel(new SpinnerNumberModel(0, 0, 360, 1));
        spinnerBeta.setPreferredSize(new Dimension(50, 20));
        spinnerBeta.setValue((int) orientationModel.getBeta());
        spinnerBeta.addChangeListener(e -> {
            if (spinnerBeta.getValue() != null) {
                orientationModel.setBeta(Optional.ofNullable(spinnerBeta.getValue().toString()).map(Ints::tryParse)
                        .orElse(0));
            }
        });

        spinnerGamma.setModel(new SpinnerNumberModel(0, 0, 360, 1));
        spinnerGamma.setPreferredSize(new Dimension(50, 20));
        spinnerGamma.setValue((int) orientationModel.getGamma());
        spinnerGamma.addChangeListener(e -> {
            if (spinnerGamma.getValue() != null) {
                orientationModel.setGamma(Optional.ofNullable(spinnerGamma.getValue().toString()).map(Ints::tryParse)
                        .orElse(0));
            }
        });

        String path = "image/dialog/orientation/abg.png";
        URL imgURL = OrientationDialog.class.getClassLoader().getResource(path);
        if (imgURL != null) {
            imageLabel.setIcon(new ImageIcon(imgURL));
        }

        getDialogContentPanel().add(wrapperPanel);

    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
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
        label1.setText("alpha:");
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
        spinnerAlpha = new JSpinner();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spinnerAlpha, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer2, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("beta:");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label2, gbc);
        spinnerBeta = new JSpinner();
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spinnerBeta, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer3, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("gamma:");
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label3, gbc);
        spinnerGamma = new JSpinner();
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spinnerGamma, gbc);
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

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return wrapperPanel;
    }

    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {

    }
}
