package adda.application.controls;

import adda.utils.ReflectionHelper;
import adda.utils.StringHelper;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class JComplexNumberInput extends JPanel {

//    private JPanel panel;
    private JNumericField realPartNumericField;
    private JNumericField imagPartNumericField;

    private static final double REAL_PART_DEFAULT = 1.5;
    private static final double IMAG_PART_DEFAULT = 0.00001;

    public JComplexNumberInput() {
        setLayout(new FlowLayout(FlowLayout.LEFT));

        realPartNumericField = new JNumericField();
        realPartNumericField.setMaxLength(20);
        realPartNumericField.setPrecision(10);
        realPartNumericField.setAllowNegative(false);
        realPartNumericField.setPreferredSize(new Dimension(50, 20));
        realPartNumericField.setDouble(REAL_PART_DEFAULT);

        realPartNumericField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {

            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                if (StringHelper.isEmpty(realPartNumericField.getText())) {
                    realPartNumericField.setDouble(REAL_PART_DEFAULT);
                }
            }
        });

        super.add(realPartNumericField);

        JLabel label = new JLabel("+ i ");
//        Font newLabelFont=new Font(label.getFont().getName(),Font.ITALIC+Font.BOLD,label.getFont().getSize());
//        label.setFont(newLabelFont);

        super.add(label);

        imagPartNumericField = new JNumericField();
        imagPartNumericField.setMaxLength(20);
        imagPartNumericField.setPrecision(10);
        imagPartNumericField.setAllowNegative(false);
        imagPartNumericField.setPreferredSize(new Dimension(50, 20));
        imagPartNumericField.setDouble(IMAG_PART_DEFAULT);

        imagPartNumericField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {

            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                if (StringHelper.isEmpty(imagPartNumericField.getText())) {
                    imagPartNumericField.setDouble(IMAG_PART_DEFAULT);
                }
            }
        });

//        imagPartNumericField.addActionListener();
//        imagPartNumericField.addAncestorListener();
//        imagPartNumericField.addMouseListener();

        super.add(imagPartNumericField);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        realPartNumericField.setEnabled(enabled);
        imagPartNumericField.setEnabled(enabled);
    }

    public String getRealPartText() {
        return realPartNumericField.getText();
    }

    public String getImagPartText() {
        return imagPartNumericField.getText();
    }



    public double getRealPart() {
        return realPartNumericField.getDouble();
    }

    public double getImagPart() {
        return imagPartNumericField.getDouble();
    }

    public void setRealPart(double realPart) {
        realPartNumericField.setDouble(realPart);
    }

    public void setImagPart(double imagPart) {
        imagPartNumericField.setDouble(imagPart);
    }


    public void addRealPartDocumentListener(DocumentListener realPartListener) {
        realPartNumericField.getDocument().addDocumentListener(realPartListener);
    }

    public void addImagPartDocumentListener(DocumentListener imagPartListener) {
        imagPartNumericField.getDocument().addDocumentListener(imagPartListener);
    }
}
