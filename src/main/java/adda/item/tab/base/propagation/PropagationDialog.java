package adda.item.tab.base.propagation;

import adda.application.controls.CustomOkCancelModalDialog;
import adda.application.controls.JNumericField;
import adda.application.controls.MatrixBorder;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.utils.ListenerHelper;
import adda.utils.StringHelper;

import javax.swing.*;
import java.awt.*;

public class PropagationDialog extends CustomOkCancelModalDialog {
    public PropagationDialog(IModel model) {
        super(model, StringHelper.toDisplayString("Propagation Configuration"));

        if (!(model instanceof PropagationModel)) return;

        PropagationModel propagationModel = (PropagationModel) model;

        JPanel panel = new JPanel();
//        JPanel panel = new JPanel(new GridLayout(3, 0));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setMaximumSize(new Dimension(130, Integer.MAX_VALUE));
        panel.setBorder(new MatrixBorder(Color.DARK_GRAY, 15, 10));
        
        JPanel panelX = new JPanel();
        panelX.setMaximumSize(new Dimension(130, Integer.MAX_VALUE));

        panelX.add(new JLabel("x "));
        JNumericField numericFieldX = new JNumericField();
        numericFieldX.setMaxLength(20);
        numericFieldX.setPrecision(10);
        numericFieldX.setAllowNegative(false);
        numericFieldX.setDouble(propagationModel.getX());
        numericFieldX.getDocument().addDocumentListener(ListenerHelper.getSimpleDocumentListener(() -> {
            if (!StringHelper.isEmpty(numericFieldX.getText())) {
                propagationModel.setX(numericFieldX.getDouble());
            }
        }));
        numericFieldX.addFocusListener(ListenerHelper.getSimpleFocusListener(() -> {
            if (StringHelper.isEmpty(numericFieldX.getText())) {
                numericFieldX.setText("1.0");
                propagationModel.setX(1.0);
            }
        }));
        panelX.add(numericFieldX);
        panel.add(panelX);


        JPanel panelY = new JPanel();
        panelY.setMaximumSize(new Dimension(130, Integer.MAX_VALUE));
        panelY.add(new JLabel("y "));

        JNumericField numericFieldY = new JNumericField();
        numericFieldY.setMaxLength(20);
        numericFieldY.setPrecision(10);
        numericFieldY.setAllowNegative(false);
        numericFieldY.setDouble(propagationModel.getY());
        numericFieldY.getDocument().addDocumentListener(ListenerHelper.getSimpleDocumentListener(() -> {
            if (!StringHelper.isEmpty(numericFieldY.getText())) {
                propagationModel.setY(numericFieldY.getDouble());
            }
        }));
        numericFieldY.addFocusListener(ListenerHelper.getSimpleFocusListener(() -> {
            if (StringHelper.isEmpty(numericFieldY.getText())) {
                numericFieldY.setText("1.0");
                propagationModel.setY(1.0);
            }
        }));
        panelY.add(numericFieldY);
        panel.add(panelY);


        JPanel panelZ = new JPanel();
        panelZ.setMaximumSize(new Dimension(130, Integer.MAX_VALUE));
        panelZ.add(new JLabel("z "));
        JNumericField numericFieldZ = new JNumericField();
        numericFieldZ.setMaxLength(20);
        numericFieldZ.setPrecision(10);
        numericFieldZ.setAllowNegative(false);
        numericFieldZ.setDouble(propagationModel.getZ());
        numericFieldZ.getDocument().addDocumentListener(ListenerHelper.getSimpleDocumentListener(() -> {
            if (!StringHelper.isEmpty(numericFieldZ.getText())) {
                propagationModel.setZ(numericFieldZ.getDouble());
            }
        }));
        numericFieldZ.addFocusListener(ListenerHelper.getSimpleFocusListener(() -> {
            if (StringHelper.isEmpty(numericFieldZ.getText())) {
                numericFieldZ.setText("1.0");
                propagationModel.setZ(1.0);
            }
        }));
        panelZ.add(numericFieldZ);
        panel.add(panelZ);

        getDialogContentPanel().add(panel);

    }



    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {

    }
}
