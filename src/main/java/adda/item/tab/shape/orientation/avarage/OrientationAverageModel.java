package adda.item.tab.shape.orientation.avarage;

import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.models.IModelObserver;
import adda.base.models.ModelBase;
import adda.item.tab.shape.orientation.avarage.alpha.AlphaOrientationAverageModel;
import adda.item.tab.shape.orientation.avarage.gamma.GammaOrientationAverageModel;
import adda.utils.StringHelper;

public class OrientationAverageModel extends ModelBase implements IModelObserver {

    public static final String AVERAGE_FILE_FIELD_NAME = "averageFile";
    public static final String ALPHA_MODEL_FIELD_NAME = "alphaModel";
    public static final String BETA_MODEL_FIELD_NAME = "gammaModel";
    public static final String GAMMA_MODEL_FIELD_NAME = "gammaModel";
    AlphaOrientationAverageModel alphaModel;
    AlphaOrientationAverageModel betaModel;
    AlphaOrientationAverageModel gammaModel;

    boolean useExistFile;

    String averageFile;

    public String getAverageFile() {
        return averageFile;
    }

    public void setAverageFile(String averageFile) {
        if(this.averageFile == null || !this.averageFile.equals(averageFile)) {

            useExistFile = !StringHelper.isEmpty(this.averageFile);

            //todo validate and export to alphaModel gammaModel

            this.averageFile = averageFile;
            notifyObservers(AVERAGE_FILE_FIELD_NAME, averageFile);
        }
    }

    public AlphaOrientationAverageModel getAlphaModel() {
        return alphaModel;
    }

    public void setAlphaModel(AlphaOrientationAverageModel alphaModel) {

        if (this.alphaModel == null || !this.alphaModel.equals(alphaModel)) {
            if (this.alphaModel != null) {
                this.alphaModel.removeObserver(this);
            }
            this.alphaModel = alphaModel;
            if (this.alphaModel != null) {
                this.alphaModel.addObserver(this);
            }
            alphaModel.setLabel("alpha");
            notifyObservers(ALPHA_MODEL_FIELD_NAME, this.alphaModel);
        }
    }

    public AlphaOrientationAverageModel getBetaModel() {
        return betaModel;
    }

    public void setBetaModel(AlphaOrientationAverageModel betaModel) {

        if (this.betaModel == null || !this.betaModel.equals(betaModel)) {
            if (this.betaModel != null) {
                this.betaModel.removeObserver(this);
            }
            this.betaModel = betaModel;
            if (this.betaModel != null) {
                this.betaModel.addObserver(this);
            }
            betaModel.setLabel("beta");
            notifyObservers(BETA_MODEL_FIELD_NAME, this.betaModel);
        }
    }

    public AlphaOrientationAverageModel getGammaModel() {
        return gammaModel;
    }

    public void setGammaModel(AlphaOrientationAverageModel gammaModel) {

        if (this.gammaModel == null || !this.gammaModel.equals(gammaModel)) {
            if (this.gammaModel != null) {
                this.gammaModel.removeObserver(this);
            }
            this.gammaModel = gammaModel;
            if (this.gammaModel != null) {
                this.gammaModel.addObserver(this);
            }
            gammaModel.setLabel("gamma");
            notifyObservers(GAMMA_MODEL_FIELD_NAME, this.gammaModel);
        }
    }

    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
        if (sender instanceof AlphaOrientationAverageModel && sender.equals(alphaModel)) {
            notifyObservers(ALPHA_MODEL_FIELD_NAME, this.alphaModel);
        }
        if (sender instanceof AlphaOrientationAverageModel && sender.equals(betaModel)) {
            notifyObservers(BETA_MODEL_FIELD_NAME, this.betaModel);
        }
        if (sender instanceof AlphaOrientationAverageModel && sender.equals(gammaModel)) {
            notifyObservers(GAMMA_MODEL_FIELD_NAME, this.gammaModel);
        }
    }
}