package adda.item.root.projectArea;

import adda.Context;
import adda.base.boxes.IBox;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.models.IModelObserver;
import adda.base.models.ModelBase;
import adda.item.tab.base.propagation.PropagationModel;
import adda.item.tab.base.refractiveIndex.RefractiveIndexModel;
import adda.item.tab.base.refractiveIndexAggregator.RefractiveIndexAggregatorModel;
import adda.item.tab.options.OptionsModel;
import adda.item.tab.shape.granules.GranulesModel;
import adda.item.tab.shape.orientation.OrientationEnum;
import adda.item.tab.shape.orientation.OrientationModel;
import adda.item.tab.shape.orientation.avarage.OrientationAverageModel;
import adda.item.tab.shape.selector.ShapeSelectorModel;
import adda.item.tab.shape.selector.params.ModelShapeParam;
import adda.item.tab.shape.selector.params.bicoated.BicoatedModel;
import adda.settings.AppSetting;
import adda.settings.SettingsManager;
import adda.utils.OsUtils;
import adda.utils.OutputDisplayer;
import adda.utils.StringHelper;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class ProjectAreaModel extends ModelBase implements IModelObserver {

    public static final String IS_ACTIVE_FIELD_NAME = "isActive";
    public static final String IS_RUNNING_FIELD_NAME = "isRunning";
    public static final String IS_LOADING_FIELD_NAME = "isLoading";
    public static final String IS_SUCCESSFULLY_FINISHED_FIELD_NAME = "isSuccessfullyFinished";

    protected boolean isRunning;
    protected boolean isActive;
    protected boolean isSuccessfullyFinished;
    protected boolean isLoading;
    protected volatile boolean isChangedNestedModelList;

    private final Timer timer = new Timer(1500, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (Context.getInstance().isGlobalBlockDialogs()) {
                Context.getInstance().setGlobalBlockDialogs(false);
            }
            Context.getInstance().setGlobalBlockDialogs(false);
            if (isChangedNestedModelList) {
                saveNestedModelList();
                isChangedNestedModelList = false;
            }

        }
    });

    protected String pathToState;

    protected List<IModel> nestedModelList = Collections.emptyList();

    public List<IModel> getNestedModelList() {
        return nestedModelList;
    }

    public void setNestedModelList(List<IModel> nestedModelList) {
        timer.stop();
        if (this.nestedModelList != null) {
            for (IModel model : this.nestedModelList) {
                model.removeObserver(this);
            }
        }

        this.nestedModelList = nestedModelList;


        //loadNestedModelList();

//        if (this.nestedModelList != null) {
//            for (IModel model : this.nestedModelList) {
//                model.addObserver(this);
//            }
//        }
//
//        timer.setRepeats(true);
//        timer.start();

    }

    public void loadNestedModelList() {
        //this.nestedModelList = nestedModelList;
        setLoading(true);
        Context.getInstance().getMainForm().setLoadingVisible(true);
        Context.getInstance().setGlobalBlockDialogs(true);
        Thread t = new Thread(() -> {
            ObjectInputStream objectInputStream = null;
            try {
                if (StringHelper.isEmpty(pathToState)) {
                    return;
                }

                final String name = pathToState + "/adda_gui_state.data";

                if (!(new File(name)).exists()) {
                    return;
                }

                objectInputStream = new ObjectInputStream(
                        new FileInputStream(name));
                List<IModel> loadedList = (List<IModel>) objectInputStream.readObject();
                List<RefractiveIndexModel> refractiveIndexModelList = (List<RefractiveIndexModel>) objectInputStream.readObject();
                OrientationAverageModel savedOrientationAverageModel = (OrientationAverageModel) objectInputStream.readObject();
                ModelShapeParam shapeParams = (ModelShapeParam) objectInputStream.readObject();
                objectInputStream.close();

                Map<Class, IModel> map = loadedList.stream().collect(Collectors.toMap(item -> item.getClass(), item -> item));

                for (IModel model : nestedModelList) {

                    if (RefractiveIndexAggregatorModel.class.equals(model.getClass())) {
                        //using SwingUtilities.invokeLater is required since RefractiveIndexAggregatorModel constructor has SwingUtilities.invokeLater
                        SwingUtilities.invokeLater(() -> {
                            RefractiveIndexAggregatorModel refractiveIndexAggregatorModel = (RefractiveIndexAggregatorModel) model;
                            final int[] index = {0};
                            refractiveIndexAggregatorModel.getShapeBoxes().forEach(box -> {
                                box.getModel().copyProperties(refractiveIndexModelList.get(index[0]++));
                            });
                            refractiveIndexAggregatorModel.getGranulBox().getModel().copyProperties(refractiveIndexModelList.get(index[0]++));
                        });
                        continue;
                    }


                    if (map.containsKey(model.getClass())) {

                        final IModel savedModel = map.get(model.getClass());
                        //savedModel.setUnderCopy(true);
                        if (OrientationModel.class.equals(model.getClass())) {
                            final OrientationModel savedOrientationModel = (OrientationModel) savedModel;
                            final OrientationModel orientationModel = (OrientationModel) model;
                            orientationModel.setUnderCopy(true);
                            OrientationAverageModel origOrientationAvgModel = (OrientationAverageModel) orientationModel.getOrientationAverageBox().getModel();

                            origOrientationAvgModel.getGammaModel().copyProperties(savedOrientationAverageModel.getGammaModel());
                            origOrientationAvgModel.getBetaModel().copyProperties(savedOrientationAverageModel.getBetaModel());
                            origOrientationAvgModel.getAlphaModel().copyProperties(savedOrientationAverageModel.getAlphaModel());
                            origOrientationAvgModel.setAverageFile(savedOrientationAverageModel.getAverageFile());

                            orientationModel.setGamma(savedOrientationModel.getGamma());
                            orientationModel.setBeta(savedOrientationModel.getBeta());
                            orientationModel.setAlpha(savedOrientationModel.getAlpha());
                            orientationModel.setEnumValue(savedOrientationModel.getEnumValue());
                            orientationModel.setUnderCopy(false);
                            continue;
                        }
                        if (ShapeSelectorModel.class.equals(model.getClass())) {
                            model.copyProperties(savedModel);
                            final ShapeSelectorModel shapeSelectorModel = (ShapeSelectorModel) model;
                            if (shapeSelectorModel.getParamsBox() != null) {
                                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                                    public void run() {
                                        shapeSelectorModel.getParamsBox().init();
                                        shapeSelectorModel.getParamsBox().getModel().copyProperties(shapeParams);
                                    }
                                });
                            }
                            continue;
                        }
                        if (GranulesModel.class.equals(model.getClass())) {

                            GranulesModel granulesModel = (GranulesModel) model;
                            GranulesModel savedGranulesModel = (GranulesModel) savedModel;

                            granulesModel.setDiameter(savedGranulesModel.getDiameter());
                            granulesModel.setDomainNumber(savedGranulesModel.getDomainNumber());
                            granulesModel.setFraction(savedGranulesModel.getFraction());
                            granulesModel.setSave(savedGranulesModel.isSave());
                            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    try {
                                        if (savedGranulesModel.isUseGranul()) {
                                            granulesModel.setUnderCopy(true);
                                            granulesModel.setUseGranul(true);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    } finally {
                                        granulesModel.setUnderCopy(false);
                                    }
                                }
                            });
                            continue;
                        }
                        model.copyProperties(savedModel);
                    }
                }
            } catch (Exception ignore) {
                int e = 2;
            } finally {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (nestedModelList != null) {
                            for (IModel model : nestedModelList) {
                                model.addObserver(ProjectAreaModel.this);
                            }
                        }

                        setLoading(false);
                        Context.getInstance().setGlobalBlockDialogs(false);
                        Context.getInstance().getMainForm().setLoadingVisible(false);
                        timer.setRepeats(true);
                        timer.start();

                        nestedModelList.forEach(IModel::forceVerify);
                    }
                });
            }


        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getPathToState() {
        return pathToState;
    }

    public void setPathToState(String pathToState) {
        this.pathToState = pathToState;
    }

    public void saveNestedModelList() {
        if (StringHelper.isEmpty(pathToState) || nestedModelList == null || nestedModelList.size() < 1) {
            return;
        }

        final String name = pathToState + "/adda_gui_state.data";

//        if (!(new File(name)).exists()) {
//            return;
//        }
        Thread t = new Thread(() -> {
            ObjectOutputStream objectOutputStream = null;
            try {

                ShapeSelectorModel shapeSelectorModel = (ShapeSelectorModel) nestedModelList.stream()
                        .filter(entity -> entity instanceof ShapeSelectorModel)
                        .findFirst()
                        .get();

                RefractiveIndexAggregatorModel refractiveIndexAggregatorModel = (RefractiveIndexAggregatorModel) nestedModelList.stream()
                        .filter(entity -> entity instanceof RefractiveIndexAggregatorModel)
                        .findFirst()
                        .get();

                List<RefractiveIndexModel> refractiveIndexModelList = new ArrayList<>();
                refractiveIndexAggregatorModel
                        .getShapeBoxes()
                        .forEach(box -> {
                            refractiveIndexModelList.add((RefractiveIndexModel) box.getModel());
                        });
                refractiveIndexModelList.add((RefractiveIndexModel) refractiveIndexAggregatorModel.getGranulBox().getModel());


                OrientationModel orientationModel = (OrientationModel) nestedModelList.stream()
                        .filter(entity -> entity instanceof OrientationModel)
                        .findFirst()
                        .get();

                File f = new File(name);
                if (!f.exists()) {
                    if (!f.createNewFile()) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                JOptionPane.showMessageDialog(null, "Cannot create state file: " + name);
                            }
                        });
                        return;
                    }
                }

                objectOutputStream = new ObjectOutputStream(
                        new FileOutputStream(name));
                objectOutputStream.writeObject(nestedModelList);
                objectOutputStream.writeObject(refractiveIndexModelList);
                objectOutputStream.writeObject(orientationModel.getOrientationAverageBox().getModel());
                final IBox paramsBox = shapeSelectorModel.getParamsBox();
                objectOutputStream.writeObject(paramsBox != null ? paramsBox.getModel() : new BicoatedModel());

                objectOutputStream.close();
            } catch (IOException e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JOptionPane.showMessageDialog(null, "Cannot create state file: " + name + ", " +
                                e.getMessage());
                    }
                });
                e.printStackTrace();
            }
        });
        t.start();
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        if (isLoading != loading) {
            this.isLoading = loading;
            notifyObservers(IS_LOADING_FIELD_NAME, isLoading);
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        if (isActive != active) {
            this.isActive = active;
            notifyObservers(IS_ACTIVE_FIELD_NAME, isActive);

            if (this.isActive) {
                timer.setRepeats(true);
                timer.start();
            } else {
                timer.setRepeats(false);
                timer.stop();
            }
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        if (isRunning != running) {
            this.isRunning = running;
            notifyObservers(IS_RUNNING_FIELD_NAME, isRunning);
        }
    }

    public boolean isSuccessfullyFinished() {
        return isSuccessfullyFinished;
    }

    public void setSuccessfullyFinished(boolean successfullyFinished) {
        if (isSuccessfullyFinished != successfullyFinished) {
            this.isSuccessfullyFinished = successfullyFinished;
            notifyObservers(IS_SUCCESSFULLY_FINISHED_FIELD_NAME, isRunning);
        }
    }

    private OutputDisplayer outputDisplayer;

    public void setOutputDisplayer(OutputDisplayer outputDisplayer) {
        this.outputDisplayer = outputDisplayer;
    }

    private OptionsModel optionsModel;

    public void setOptionsModel(OptionsModel optionsModel) {
        this.optionsModel = optionsModel;
    }

    public void start() {
        if (optionsModel == null || outputDisplayer == null) {
            return;
        }

        final AppSetting appSetting = SettingsManager.getSettings().getAppSetting();

        setRunning(true);

        OrientationModel orientationModel = (OrientationModel) nestedModelList.stream()
                .filter(model -> model != null && model.getClass().equals(OrientationModel.class))
                .findFirst()
                .get();

        if (OrientationEnum.Average.equals(orientationModel.getEnumValue())) {
            orientationModel.setPathToRun(pathToState);
            try {
                Files.write(Paths.get(orientationModel.getAvgPath()), orientationModel.getAvgConfig().getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "File " + orientationModel.getAvgPath() + "cannot be created");
                return;
            }
        }


        try {
            Process addaProcess;
            List<String> args = new ArrayList<String>();
            args.add(appSetting.getAddaExecSeq());
            final List<String> params = Arrays.asList(optionsModel.getActualCommandLine().split(" "));
            args.addAll(params);
            args.add("-dir");
            args.add(pathToState);
            args.add("-so_buf");
            args.add("line");
            if (OsUtils.isMac()) {
//                StringBuilder builder = new StringBuilder();
//                builder.append(appSetting.getAddaExecSeq());
//                builder.append(" ");
//                builder.append(optionsModel.getActualCommandLine());
//                builder.append(" -so_buf line");
//                builder.append(" -dir ");
//                builder.append("\"");
//                builder.append(pathToState);
//                builder.append("\"");
//                String command = builder.toString();
//              System.out.println(args);
                addaProcess = Runtime.getRuntime().exec(args.stream().toArray(String[]::new));
            } else {
                ProcessBuilder builder = new ProcessBuilder(args);
                builder.redirectErrorStream(true);
                addaProcess = builder.start();
            }
            outputDisplayer.commence(addaProcess, () -> javax.swing.SwingUtilities.invokeLater(() -> setSuccessfullyFinished(true)));
        } catch (IOException e) {
            setRunning(false);
            JOptionPane.showMessageDialog(null, e.getMessage());
        }


    }

    public void stop() {
        setRunning(false);
        if (outputDisplayer == null) {
            return;
        }
        outputDisplayer.cancel();
    }

    @Override
    public void modelPropertyChanged(IModel sender, IModelPropertyChangeEvent event) {
        isChangedNestedModelList = true;
    }
}
