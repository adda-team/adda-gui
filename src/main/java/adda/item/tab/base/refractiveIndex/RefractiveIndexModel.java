package adda.item.tab.base.refractiveIndex;

import adda.base.IAddaOption;
import adda.base.annotation.Viewable;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.models.IModelObserver;
import adda.base.models.ModelBase;
import adda.base.models.ModelBaseAddaOptionsContainer;
import adda.utils.ReflectionHelper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RefractiveIndexModel extends ModelBaseAddaOptionsContainer implements IModelObserver {

    private static final double REAL_DEFAULT = 1.5;
    private static final double IMAG_DEFAULT = 0;
    public static final String REAL_X_FIELD_NAME = "realX";
    public static final String REAL_Y_FIELD_NAME = "realY";
    public static final String REAL_Z_FIELD_NAME = "realZ";
    public static final String IMAG_X_FIELD_NAME = "imagX";
    public static final String IS_ANISOTROP_FIELD_NAME = "isAnisotrop";
    public static final String IMAG_Y_FIELD_NAME = "imagY";
    public static final String IS_ENABLED_ANISOTROP_FIELD_NAME = "isEnabledAnisotrop";
    public static final String IMAG_Z_FIELD_NAME = "imagZ";

    protected boolean isEnabledAnisotrop = true;

    @Viewable("anisotropy")
    protected boolean isAnisotrop = false;




    protected double realX = REAL_DEFAULT;
    protected double imagX = IMAG_DEFAULT;

    protected double realY = REAL_DEFAULT;
    protected double imagY = IMAG_DEFAULT;

    protected double realZ = REAL_DEFAULT;
    protected double imagZ = IMAG_DEFAULT;

    public RefractiveIndexModel() {
        setLabel(" ");
    }

    @Override
    public void applyDefaultState() {
        setAnisotrop(false);
        setRealX(REAL_DEFAULT);
        setImagX(IMAG_DEFAULT);
        //prevent senseless events
        realY = REAL_DEFAULT;
        imagY = IMAG_DEFAULT;

        realZ = REAL_DEFAULT;
        imagZ = IMAG_DEFAULT;

    }

    @Override
    public boolean isDefaultState() {
        return !isAnisotrop()
                && realX == REAL_DEFAULT
                && imagX == IMAG_DEFAULT
                && realY == REAL_DEFAULT
                && imagY == IMAG_DEFAULT
                && realZ == REAL_DEFAULT
                && imagZ == IMAG_DEFAULT;
    }

    public double getRealX() {
        return realX;
    }

    public void setRealX(double realX) {
        if (this.realX != realX) {
            this.realX = realX;
            if (!isAnisotrop) {
//                setRealY(realX);
//                setRealZ(realX);
                realY = realX;
                realZ = realX;
            }
            notifyObservers(REAL_X_FIELD_NAME, realX);
        }
    }





    public double getRealY() {
        return realY;
    }

    public void setRealY(double realY) {
        if (this.realY != realY) {
            this.realY = realY;
            notifyObservers(REAL_Y_FIELD_NAME, realY);
        }
    }





    public double getRealZ() {
        return realZ;
    }

    public void setRealZ(double realZ) {
        if (this.realZ != realZ) {
            this.realZ = realZ;
            notifyObservers(REAL_Z_FIELD_NAME, realZ);
        }
    }



    public double getImagX() {
        return imagX;
    }

    public void setImagX(double imagX) {
        if (this.imagX != imagX) {
            this.imagX = imagX;
            if (!isAnisotrop) {
//                setImagY(imagX);
//                setImagZ(imagX);

                imagY = imagX;
                imagZ = imagX;
            }
            notifyObservers(IMAG_X_FIELD_NAME, imagX);
        }
    }



    public boolean isAnisotrop() {
        return isAnisotrop;
    }

    public void setAnisotrop(boolean isAnisotrop) {
        if (this.isAnisotrop != isAnisotrop) {
            this.isAnisotrop = isAnisotrop;
            notifyObservers(IS_ANISOTROP_FIELD_NAME, isAnisotrop);
        }
    }



    public double getImagY() {
        return imagY;
    }

    public void setImagY(double imagY) {
        if (this.imagY != imagY) {
            this.imagY = imagY;
            notifyObservers(IMAG_Y_FIELD_NAME, imagY);
        }
    }





    public boolean isEnabledAnisotrop() {
        return isEnabledAnisotrop;
    }

    public void setEnabledAnisotrop(boolean isEnabledAnisotrop) {
        if (this.isEnabledAnisotrop != isEnabledAnisotrop) {
            this.isEnabledAnisotrop = isEnabledAnisotrop;
            notifyObservers(IS_ENABLED_ANISOTROP_FIELD_NAME, isEnabledAnisotrop);
        }
    }



    public double getImagZ() {
        return imagZ;
    }

    public void setImagZ(double imagZ) {
        if (this.imagZ != imagZ) {
            this.imagZ = imagZ;
            notifyObservers(IMAG_Z_FIELD_NAME, imagZ);
        }
    }



    public List<String> getParamsList() {

        return Arrays.asList(Double.toString(realX), Double.toString(imagX), Double.toString(realY), Double.toString(imagY), Double.toString(realZ),  Double.toString(imagZ));
    }

    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
        if (sender instanceof RefractiveIndexModel) {

            //reflection is too slow
            //ReflectionHelper.setPropertyValue(this, event.getPropertyName(), event.getPropertyValue());

            RefractiveIndexModel refractiveIndexModel = (RefractiveIndexModel) sender;
            switch(event.getPropertyName())
            {
                case REAL_X_FIELD_NAME:
                    setRealX((double) event.getPropertyValue());
                    break;
                case REAL_Y_FIELD_NAME:
                    setRealY((double) event.getPropertyValue());
                    break;
                case REAL_Z_FIELD_NAME:
                    setRealZ((double) event.getPropertyValue());
                    break;
                case IMAG_X_FIELD_NAME:
                    setImagX((double) event.getPropertyValue());
                    break;
                case IMAG_Y_FIELD_NAME:
                    setImagY((double) event.getPropertyValue());
                    break;
                case IMAG_Z_FIELD_NAME:
                    setImagZ((double) event.getPropertyValue());
                    break;
                case IS_ENABLED_ANISOTROP_FIELD_NAME:
                    setEnabledAnisotrop((boolean) event.getPropertyValue());
                    break;
                case IS_ANISOTROP_FIELD_NAME:
                    setAnisotrop((boolean) event.getPropertyValue());
                    break;
            }
        }
    }

    @Override
    protected List<IAddaOption> getAddaOptionsInner() {
        return Collections.emptyList();
    }
}