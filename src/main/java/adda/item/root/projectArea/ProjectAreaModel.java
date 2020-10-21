package adda.item.root.projectArea;

import adda.Main;
import adda.base.models.ModelBase;
import adda.item.tab.options.OptionsModel;
import adda.utils.OutputDisplayer;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProjectAreaModel extends ModelBase {

    public static final String IS_ACTIVE_FIELD_NAME = "isActive";
    public static final String IS_RUNNING_FIELD_NAME = "isRunning";

    protected boolean isRunning;
    protected boolean isActive;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        if(isActive != active) {
            this.isActive = active;
            notifyObservers(IS_ACTIVE_FIELD_NAME, isActive);
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
    
}
