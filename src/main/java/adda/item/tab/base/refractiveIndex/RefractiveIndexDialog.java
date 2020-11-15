package adda.item.tab.base.refractiveIndex;

import adda.application.controls.CustomOkCancelModalDialog;
import adda.application.controls.JComplexNumberInput;
import adda.application.controls.MatrixBorder;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.utils.ListenerHelper;
import adda.utils.StringHelper;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.function.Function;

public class RefractiveIndexDialog extends CustomOkCancelModalDialog {
    public RefractiveIndexDialog(IModel model) {
        super(model, "Refractive index");
        if (model instanceof RefractiveIndexModel) {
            final RefractiveIndexModel refractiveIndexModel = (RefractiveIndexModel) model;

            JPanel panel = new JPanel(new GridLayout(3, 3));
            panel.setBorder(new MatrixBorder(Color.DARK_GRAY, 15, 10));


            JComplexNumberInput ox = createComplexNumberInput(realPart -> {
                refractiveIndexModel.setRealX(realPart);
                return null;
            }, imagPart -> {
                refractiveIndexModel.setImagX(imagPart);
                return null;
            });
            ox.setRealPart(refractiveIndexModel.getRealX());
            ox.setImagPart(refractiveIndexModel.getImagX());
            panel.add(ox);
            panel.add(getZeroLabel());
            panel.add(getZeroLabel());


            JComplexNumberInput oy = createComplexNumberInput(realPart -> {
                refractiveIndexModel.setRealY(realPart);
                return null;
            }, imagPart -> {
                refractiveIndexModel.setImagY(imagPart);
                return null;
            });
            oy.setRealPart(refractiveIndexModel.getRealY());
            oy.setImagPart(refractiveIndexModel.getImagY());
            panel.add(getZeroLabel());
            panel.add(oy);
            panel.add(getZeroLabel());


            JComplexNumberInput oz = createComplexNumberInput(realPart -> {
                refractiveIndexModel.setRealZ(realPart);
                return null;
            }, imagPart -> {
                refractiveIndexModel.setImagZ(imagPart);
                return null;
            });
            oz.setRealPart(refractiveIndexModel.getRealZ());
            oz.setImagPart(refractiveIndexModel.getImagZ());
            panel.add(getZeroLabel());
            panel.add(getZeroLabel());
            panel.add(oz);

            getDialogContentPanel().add(panel);


        }
    }

    private JLabel getZeroLabel() {
        return new JLabel("0", SwingConstants.CENTER);
    }

    private JComplexNumberInput createComplexNumberInput(Function<Double, Void> realFunc, Function<Double, Void> imagFunc) {
        JComplexNumberInput complexNumberInput = new JComplexNumberInput();
        complexNumberInput.addRealPartDocumentListener(
                ListenerHelper.getSimpleDocumentListener(() -> {
                    if (!StringHelper.isEmpty(complexNumberInput.getRealPartText())) {
                        realFunc.apply(complexNumberInput.getRealPart());
                    }
                    buttonOK.setEnabled(complexNumberInput.verify());
                })
        );

        complexNumberInput.addImagPartDocumentListener(
                ListenerHelper.getSimpleDocumentListener(() -> {
                    if (!StringHelper.isEmpty(complexNumberInput.getImagPartText())) {
                        imagFunc.apply(complexNumberInput.getImagPart());
                    }
                    buttonOK.setEnabled(complexNumberInput.verify());
                })
        );
        return complexNumberInput;
    }


    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {

    }
}
