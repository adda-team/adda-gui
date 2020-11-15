package adda.item.tab.base.refractiveIndex;

import adda.application.controls.CustomOkCancelModalDialog;
import adda.application.controls.JComplexNumberInput;
import adda.base.controllers.ControllerBase;
import adda.base.controllers.ControllerDialogBase;
import adda.base.models.IModel;
import adda.item.tab.base.propagation.PropagationDialog;
import adda.item.tab.base.propagation.PropagationModel;
import adda.utils.ListenerHelper;
import adda.utils.StringHelper;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class RefractiveIndexController extends ControllerDialogBase {

    @Override
    protected void createAndBindListenersFromView() {
        super.createAndBindListenersFromView();
        if (view instanceof RefractiveIndexView && model instanceof RefractiveIndexModel) {
            RefractiveIndexModel refractiveIndexModel = (RefractiveIndexModel) model;
            JComplexNumberInput complexNumberInput = ((RefractiveIndexView) view).getComplexNumberInput();

            complexNumberInput.addRealPartDocumentListener(
                    ListenerHelper.getSimpleDocumentListener(() -> {
                        if (!StringHelper.isEmpty(complexNumberInput.getRealPartText())) {
                            refractiveIndexModel.setRealX(complexNumberInput.getRealPart());
                        }
                         complexNumberInput.verify();
                    })
            );

            complexNumberInput.addImagPartDocumentListener(
                    ListenerHelper.getSimpleDocumentListener(() -> {
                        if (!StringHelper.isEmpty(complexNumberInput.getImagPartText())) {
                            refractiveIndexModel.setImagX(complexNumberInput.getImagPart());
                        }
                        complexNumberInput.verify();
                    })
            );

            //complexNumberInput

        }
    }



    @Override
    protected boolean needOpenDialog(String fieldName, Object fieldValue) {
        return "isAnisotrop".equals(fieldName) && (boolean) fieldValue;
    }

    @Override
    protected CustomOkCancelModalDialog getDialog(IModel dialogModel) {
        if (dialogModel instanceof RefractiveIndexModel) {
            return new RefractiveIndexDialog(dialogModel);
        }
        return null;
    }

}