package adda.item.root.projectArea;

import adda.Context;
import adda.base.events.IModelPropertyChangeEvent;
import adda.base.models.IModel;
import adda.base.models.IModelObserver;
import adda.base.models.ModelBase;
import adda.item.tab.base.refractiveIndexAggregator.RefractiveIndexAggregatorModel;
import adda.item.tab.options.OptionsModel;
import adda.item.tab.shape.orientation.OrientationModel;
import adda.utils.OutputDisplayer;
import adda.utils.StringHelper;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ProjectAreaModel extends ModelBase implements IModelObserver {

    public static final String IS_ACTIVE_FIELD_NAME = "isActive";
    public static final String IS_RUNNING_FIELD_NAME = "isRunning";
    public static final String IS_LOADING_FIELD_NAME = "isLoading";

    protected boolean isRunning;
    protected boolean isActive;
    protected boolean isLoading;
    protected volatile boolean isChangedNestedModelList;

    private final Timer timer = new Timer(1500, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
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
            for (IModel model: this.nestedModelList) {
                model.removeObserver(this);
            }
        }

        this.nestedModelList = nestedModelList;

        if (this.nestedModelList != null) {
            for (IModel model: this.nestedModelList) {
                model.addObserver(this);
            }
        }

        loadNestedModelList();
        timer.setRepeats(true);
        timer.start();

    }

    public void loadNestedModelList() {
        //this.nestedModelList = nestedModelList;

        if (StringHelper.isEmpty(pathToState)) {
            return;
        }


        final String name = pathToState + "/adda_gui_state.data";

        if (!(new File(name)).exists()) {
            return;
        }
        setLoading(true);
        Thread t = new Thread(() -> {
            ObjectInputStream objectInputStream = null;
            try {

                objectInputStream = new ObjectInputStream(
                        new FileInputStream(name));
                List<IModel> loadedList = (List<IModel>) objectInputStream.readObject();
                objectInputStream.close();

                Map<Class, IModel> map = loadedList.stream().collect(Collectors.toMap(item -> item.getClass(), item -> item));

                for (IModel model : nestedModelList) {

                    if (RefractiveIndexAggregatorModel.class.equals(model.getClass())) continue;
                    if (OrientationModel.class.equals(model.getClass())) continue;

                    if (map.containsKey(model.getClass())) {
                        model.copyProperties(map.get(model.getClass()));
                    }
                }
            }
            catch (IOException | ClassNotFoundException ignore) {}
            finally {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        setLoading(false);
                    }
                });
            }


        });
        t.start();
    }

    public String getPathToState() {
        return pathToState;
    }

    public void setPathToState(String pathToState) {
        this.pathToState = pathToState;
    }

    public void saveNestedModelList() {
        if (StringHelper.isEmpty(pathToState)) {
            return;
        }

        final String name = pathToState + "/adda_gui_state.data";

//        if (!(new File(name)).exists()) {
//            return;
//        }
        Thread t = new Thread(() -> {
            ObjectOutputStream objectOutputStream = null;
            try {
                objectOutputStream = new ObjectOutputStream(
                        new FileOutputStream(name));
                objectOutputStream.writeObject(nestedModelList);
                objectOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t.start();
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        if(isLoading != loading) {
            this.isLoading = loading;
            notifyObservers(IS_LOADING_FIELD_NAME, isLoading);
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        if(isActive != active) {
            this.isActive = active;
            notifyObservers(IS_ACTIVE_FIELD_NAME, isActive);

            if(this.isActive) {
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
        if(isRunning != running) {
            this.isRunning = running;
            notifyObservers(IS_RUNNING_FIELD_NAME, isRunning);
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
        setRunning(true);
        List<String> args = new ArrayList<String>();
        String path = System.getProperty("user.dir");
        String addaPath = path + "/win64/adda.exe";
        args.add(addaPath);
        args.addAll(Arrays.asList(optionsModel.getActualCommandLine().split(" ")));
        args.add("-dir");
        Date now = new Date();
        SimpleDateFormat pattern = new SimpleDateFormat("dd-MM-yyyy_HH_mm_ss");
        args.add(Context.getInstance().getProjectTreeModel().getSelectedPath().getFolder() + "/run_"+pattern.format(now));


        ProcessBuilder builder = new ProcessBuilder(args);
        builder.redirectErrorStream(true);

        try {
            Process addaProcess = builder.start();
            outputDisplayer.commence(addaProcess);
        } catch (IOException e) {
            setRunning(false);
            JOptionPane.showMessageDialog(null, e.getMessage());
            //e.printStackTrace();
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
