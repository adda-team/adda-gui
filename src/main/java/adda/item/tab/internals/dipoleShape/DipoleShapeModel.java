package adda.item.tab.internals.dipoleShape;

import adda.Context;
import adda.base.AddaOption;
import adda.base.IAddaOption;
import adda.base.boxes.IBox;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.models.IModelObserver;
import adda.base.models.ModelBase;
import adda.item.root.projectArea.ProjectAreaBox;
import adda.item.root.projectArea.ProjectAreaModel;
import adda.item.tab.TabEnumModel;
import adda.item.tab.base.refractiveIndex.RefractiveIndexModel;
import adda.item.tab.base.refractiveIndexAggregator.RefractiveIndexAggregatorModel;
import adda.item.tab.internals.formulation.InteractionEnum;
import adda.item.tab.internals.formulation.PolarizationEnum;
import adda.item.tab.internals.iterativeSolver.IterativeSolverEnum;
import adda.utils.StringHelper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DipoleShapeModel extends TabEnumModel<DipoleShapeEnum> implements IModelObserver {

    public static final String SCALE_X_FIELD_NAME = "scaleX";
    public static final String SCALE_Y_FIELD_NAME = "scaleY";
    public static final String SCALE_Z_FIELD_NAME = "scaleZ";
    public static final String DELIMITER = " ";
    public static final String FORMAT = "<x>:%s, <y>:%s, <z>:%s";

    protected double scaleZ = 1;
    protected double scaleY = 1;
    protected double scaleX = 1;


    public DipoleShapeModel() {
        this.setLabel("Dipole shape");//todo localization
        setEnumValue(DipoleShapeEnum.Cubic);
        setDefaultEnumValue(DipoleShapeEnum.Cubic);
    }


    public double getScaleX() {
        return scaleX;
    }

    public void setScaleX(double scaleX) {
        if (this.scaleX != scaleX) {
            this.scaleX = scaleX;
            notifyObservers(SCALE_X_FIELD_NAME, scaleX);
        }
    }


    public double getScaleY() {
        return scaleY;
    }

    public void setScaleY(double scaleY) {
        if (this.scaleY != scaleY) {
            this.scaleY = scaleY;
            notifyObservers(SCALE_Y_FIELD_NAME, scaleY);
        }
    }


    public double getScaleZ() {
        return scaleZ;
    }

    public void setScaleZ(double scaleZ) {
        if (this.scaleZ != scaleZ) {
            this.scaleZ = scaleZ;
            notifyObservers(SCALE_Z_FIELD_NAME, scaleZ);
        }
    }


    public List<String> getParamsList() {
        return Arrays.asList(StringHelper.toDisplayString(scaleX), StringHelper.toDisplayString(scaleY), StringHelper.toDisplayString(scaleZ));
    }


    @Override
    protected List<IAddaOption> getAddaOptionsInner() {
        return Arrays.asList(new AddaOption("rect_dip", String.join(DELIMITER, getParamsList()), String.format(FORMAT, getScaleX(), getScaleY(), getScaleZ())));
    }

    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {

    }


    @Override
    public boolean validate() {
        boolean isValid = true;
        if (enumValue == DipoleShapeEnum.Rect) {
            IBox box = Context.getInstance().getWorkspaceModel().getFocusedBox();
            if (box instanceof ProjectAreaBox) {
                RefractiveIndexAggregatorModel refractiveIndexAggregatorModel =
                        (RefractiveIndexAggregatorModel) Context.getInstance().getChildModelFromSelectedBox(RefractiveIndexAggregatorModel.class);


                int max = refractiveIndexAggregatorModel.getShapeModel().getShapeDomainInfos().size();

                for (int i = 0; i < max; i++) {
                    if (((RefractiveIndexModel) refractiveIndexAggregatorModel.getShapeBoxes().get(i).getModel()).isAnisotrop()) {
                        isValid = false;
                        final String error = StringHelper.toDisplayString("<html>Anisotropy refractive index <br>does`t compatible with non cubic dipoles</html>");
                        setErrors(error);
                        break;
                    } else {
                        setErrors("");
                    }
                }
            }
        } else {
            setErrors("");
        }


        return isValid;
    }

    private void setErrors(String error) {
        validationErrors.put(ENUM_VALUE_FIELD_NAME, error);
        validationErrors.put(SCALE_X_FIELD_NAME, error);
        validationErrors.put(SCALE_Y_FIELD_NAME, error);
        validationErrors.put(SCALE_Z_FIELD_NAME, error);
    }
}