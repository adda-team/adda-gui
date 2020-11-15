package adda.item.tab.shape.orientation;

import adda.application.controls.CustomOkCancelModalDialog;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.item.tab.internals.dipoleShape.DipoleShapeModel;
import net.java.balloontip.BalloonTip;
import net.java.balloontip.styles.RoundedBalloonStyle;

import javax.swing.*;
import java.awt.*;

public class OrientationAverageDialog  extends CustomOkCancelModalDialog {



    public OrientationAverageDialog(IModel model) {
        super(model, "Orientation average");//todo localization

        if (!(model instanceof OrientationModel)) return;
        //todo localization

        OrientationModel orientationModel = (OrientationModel) model;

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

        getDialogContentPanel().add(orientationModel.getOrientationAverageBox().getLayout());



    }

        @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {

    }
}
