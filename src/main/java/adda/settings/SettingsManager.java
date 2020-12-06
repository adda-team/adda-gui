package adda.settings;

import adda.settings.formatters.json.JsonFormatter;
import adda.settings.formatters.xml.XmlFormatter;
import adda.settings.serializer.AddaSerializer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SettingsManager {
    //todo read from file
    //todo observe when settings file changed
    public static Setting getSettings() {
        String userDir = System.getProperty("user.dir");
        String settingsDir = userDir + "/setting.xml";
        Setting setting;
        try {
            String json = new String(Files.readAllBytes(Paths.get(settingsDir)), StandardCharsets.UTF_8);
            AddaSerializer serializer = new AddaSerializer(new XmlFormatter());
            setting = serializer.deserialize(json, Setting.class);
        } catch (IOException e) {
            setting = recreateSettings();
        }

        return setting;
    }

    public static Setting recreateSettings() {
        String userDir = System.getProperty("user.dir");
        String firstProjectDir = userDir + "/adda-project";

        File file = new File(firstProjectDir);
        //deleteFolder(file);
        boolean firstProjectIsCreated = true;
        if (!file.exists()) {
            if (!file.mkdir()) {
                //throw new FileNotFoundException("Directory " + firstProjectDir + "cannot be created");
                firstProjectIsCreated = false;
            }
        }

        Setting setting = new Setting();
        AppSetting appSetting = new AppSetting();
        appSetting.setDefaultProjectName("New Project");
        appSetting.setLanguage("EN");
        //appSetting.setDefaultAddaValues(new HashMap<>());
        setting.setAppSetting(appSetting);

        List<ProjectSetting> list = new ArrayList<>();
        if (firstProjectIsCreated) {
            ProjectSetting projectSetting = new ProjectSetting();
            projectSetting.setName("ADDA Project");
            projectSetting.setPath(firstProjectDir);
            list.add(projectSetting);
        }

        setting.setProjects(list);

        try {
            saveSettings(setting, userDir + "/setting.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return setting;

    }

    public static void saveSettings(Setting setting, String path) throws IOException {
        AddaSerializer serializer = new AddaSerializer(new XmlFormatter());

        String json = serializer.serialize(setting);
        BufferedWriter output = null;
        try {
            File file = new File(path);
            output = new BufferedWriter(new FileWriter(file));
            output.write(json);
        } catch ( IOException e ) {
            e.printStackTrace();
        } finally {
            if ( output != null ) {
                output.close();
            }
        }
    }

    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }


}
