package adda.item.tab.shape.orientation;

import adda.Context;
import adda.base.AddaOption;
import adda.base.IAddaOption;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.models.IModelObserver;
import adda.item.tab.TabEnumModel;
import adda.item.tab.base.beam.BeamEnum;
import adda.item.tab.internals.initialField.InitialFieldEnum;
import adda.item.tab.output.beam.BeamSaveModel;
import adda.item.tab.output.internalField.InternalFieldSaveModel;
import adda.item.tab.output.polarization.PolarizationSaveModel;
import adda.item.tab.shape.orientation.avarage.OrientationAverageBox;
import adda.item.tab.shape.orientation.avarage.OrientationAverageModel;
import adda.item.tab.shape.orientation.avarage.alpha.AlphaOrientationAverageModel;
import adda.item.tab.shape.surface.SurfaceModel;
import adda.utils.StringHelper;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrientationModel extends TabEnumModel<OrientationEnum> implements IModelObserver {

    public static final String ORIENTATION_AVERAGE_MODEL_FIELD_NAME = "orientationAverageModel";
    public static final String ALPHA_FIELD_NAME = "alpha";
    public static final String BETA_FIELD_NAME = "beta";
    public static final String GAMMA_FIELD_NAME = "gamma";
    public static final String ORIENT = "orient";
    public static final String ORIENT_COMMAND_LINE = "orient";
    public static final String AVG_COMMAND_LINE = "avg";
    public static final String DELIMITER = " ";
    public static final String PATH_TO_RUN_FIELD_NAME = "pathToRun";
    double alpha = 0;
    double beta = 0;
    double gamma = 0;


    transient OrientationAverageBox orientationAverageBox;


    public OrientationModel() {
        this.setLabel("Particle orientation");//todo localization
        setEnumValue(OrientationEnum.Fixed);
        setDefaultEnumValue(OrientationEnum.Fixed);
        setOrientationAverageBox(new OrientationAverageBox());
    }

    public OrientationAverageBox getOrientationAverageBox() {
        if (!orientationAverageBox.isInitialized()) {
            orientationAverageBox.init();
        }
        return orientationAverageBox;
    }

    protected void setOrientationAverageBox(OrientationAverageBox orientationAverageBox) {
        if (this.orientationAverageBox == null || !this.orientationAverageBox.equals(orientationAverageBox)) {
            if (orientationAverageBox != null) {
                if (!orientationAverageBox.isInitialized()) {
                    orientationAverageBox.init();
                }
            }
            if (this.orientationAverageBox != null && this.orientationAverageBox.getModel() != null) {
                this.orientationAverageBox.getModel().removeObserver(this);
            }
            this.orientationAverageBox = orientationAverageBox;
            if (this.orientationAverageBox != null) {
                this.orientationAverageBox.getModel().addObserver(this);
            }
            notifyObservers(ORIENTATION_AVERAGE_MODEL_FIELD_NAME, this.orientationAverageBox);
        }
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        if (this.alpha != alpha) {
            this.alpha = alpha;
            notifyObservers(ALPHA_FIELD_NAME, alpha);
        }
    }

    public double getBeta() {
        return beta;
    }

    public void setBeta(double beta) {
        if (this.beta != beta) {
            this.beta = beta;
            notifyObservers(BETA_FIELD_NAME, beta);
        }
    }

    public double getGamma() {
        return gamma;
    }

    public void setGamma(double gamma) {
        if (this.gamma != gamma) {
            this.gamma = gamma;
            notifyObservers(GAMMA_FIELD_NAME, gamma);
        }
    }

    private static final String[] paramNames = new String[]{ALPHA_FIELD_NAME, BETA_FIELD_NAME, GAMMA_FIELD_NAME};


    public List<String> getOrientParamsList() {
        return Arrays.asList(Integer.toString((int) alpha), Integer.toString((int) beta), Integer.toString((int) gamma));
    }

    transient String pathToRun = "";

    public String getPathToRun() {
        return pathToRun;
    }

    public void setPathToRun(String pathToRun) {
        this.pathToRun = pathToRun;
        notifyObservers(PATH_TO_RUN_FIELD_NAME, gamma);
    }

    public String getAvgPath() {
        String file = "";
        if (orientationAverageBox.getModel() != null && orientationAverageBox.getModel() instanceof OrientationAverageModel) {
            file = ((OrientationAverageModel) orientationAverageBox.getModel()).getAverageFile();
            //file = StringHelper.isEmpty(file) ? " avg_params.dat" : (" " + file);
            file = StringHelper.isEmpty(file) ? (pathToRun + (StringHelper.isEmpty(pathToRun) ? "" : File.separator) + "avg_params.dat") : file;
        }
        return file;
    }

    @Override
    protected List<IAddaOption> getAddaOptionsInner() {

        if (OrientationEnum.Average.equals(enumValue)) {
            String commandValue = AVG_COMMAND_LINE;
            String file = getAvgPath();


            return Arrays.asList(new AddaOption(ORIENT, commandValue + " " + file, StringHelper.toDisplayString(enumValue) + " " +file));
        }

        List<String> list = getOrientParamsList();

        StringBuilder stringBuilder = new StringBuilder(StringHelper.toDisplayString(enumValue));
        stringBuilder.append(DELIMITER);

        final int count = Math.min(list.size(), paramNames.length);

        for (int i = 0; i < count; i++) {
            stringBuilder.append(paramNames[i]);
            stringBuilder.append(": ");
            stringBuilder.append(list.get(i));
            if (i < count - 1) {
                stringBuilder.append("; ");
            }
        }

        return Arrays.asList(new AddaOption(ORIENT_COMMAND_LINE, getOrientParamsList().stream().collect(Collectors.joining(DELIMITER)), stringBuilder.toString()));
    }

    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
        if (sender instanceof OrientationAverageModel) {
            notifyObservers(ORIENTATION_AVERAGE_MODEL_FIELD_NAME, this.orientationAverageBox);
        }
    }

    @Override
    public void copyProperties(IModel model) {
        super.copyProperties(model);
        setUnderCopy(true);
        if (model instanceof OrientationModel) {
            OrientationModel orientationModel = (OrientationModel) model;

            OrientationAverageModel cloneOrientationAvgModel = (OrientationAverageModel) orientationModel.getOrientationAverageBox().getModel();
            OrientationAverageModel thisOrientationAvgModel = (OrientationAverageModel) getOrientationAverageBox().getModel();

            cloneOrientationAvgModel.getGammaModel().copyProperties(thisOrientationAvgModel.getGammaModel());
            cloneOrientationAvgModel.getBetaModel().copyProperties(thisOrientationAvgModel.getBetaModel());
            cloneOrientationAvgModel.getAlphaModel().copyProperties(thisOrientationAvgModel.getAlphaModel());
            cloneOrientationAvgModel.setAverageFile(thisOrientationAvgModel.getAverageFile());
        }
        setUnderCopy(false);
    }

    public String getAvgConfig() {
        StringBuilder config = new StringBuilder();
        if (orientationAverageBox.getModel() != null && orientationAverageBox.getModel() instanceof OrientationAverageModel) {
            OrientationAverageModel thisOrientationAvgModel = (OrientationAverageModel) getOrientationAverageBox().getModel();

            Map<String, AlphaOrientationAverageModel> map = new LinkedHashMap<String, AlphaOrientationAverageModel>() {{
               put("alpha", thisOrientationAvgModel.getAlphaModel());
               put("beta", thisOrientationAvgModel.getBetaModel());
               put("gamma", thisOrientationAvgModel.getGammaModel());
            }};

            map.forEach((key, value) -> {
                config.append(key);
                config.append(":\n");
                config.append("min");
                config.append("=");
                config.append((int) Math.round(value.getMin()));
                config.append("\n");
                config.append("max");
                config.append("=");
                config.append((int) Math.round(value.getMin()));
                config.append("\n");
                config.append("Jmin");
                config.append("=");
                config.append((int) Math.round(value.getJmin()));
                config.append("\n");
                config.append("Jmax");
                config.append("=");
                config.append((int) Math.round(value.getJmax()));
                config.append("\n");
                config.append("eps");
                config.append("=");
                config.append(value.getEps());
                config.append("\n");
                config.append("equiv");
                config.append("=");
                config.append(value.isEquivalent());
                config.append("\n");
                config.append("periodic");
                config.append("=");
                config.append(value.isPeriodic());
                config.append("\n");
                config.append("\n");
            });
        }
        return config.toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean validate() {

        boolean isValid = true;
        String error = "";
        if (enumValue == OrientationEnum.Average) {
            BeamSaveModel beamSaveModel = (BeamSaveModel) Context.getInstance().getChildModelFromSelectedBox(BeamSaveModel.class);
            InternalFieldSaveModel internalFieldSaveModel = (InternalFieldSaveModel) Context.getInstance().getChildModelFromSelectedBox(InternalFieldSaveModel.class);
            PolarizationSaveModel polarizationSaveModel = (PolarizationSaveModel) Context.getInstance().getChildModelFromSelectedBox(PolarizationSaveModel.class);
            error = "<html>";
            if (beamSaveModel != null && beamSaveModel.getFlag()) {
                isValid = false;
                error += "<br>" + StringHelper.toDisplayString("Beam saving does`t compatible<br>with orientation average");
            }
            if (internalFieldSaveModel != null && internalFieldSaveModel.getFlag()) {
                isValid = false;
                error += "<br>" + StringHelper.toDisplayString("Internal field saving does`t compatible<br>with orientation average");
            }
            if (polarizationSaveModel != null && polarizationSaveModel.getFlag()) {
                isValid = false;
                error += "<br>" + StringHelper.toDisplayString("Dipole polarization saving does`t compatible with<br>orientation average");
            }
            error += "</html>";
        }

        error = error.replaceAll("<html></html>", "");
        error = error.replaceFirst("<br>", "");
        validationErrors.put(ENUM_VALUE_FIELD_NAME, error);
        return isValid;
    }
}