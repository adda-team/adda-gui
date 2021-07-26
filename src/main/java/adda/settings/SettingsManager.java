package adda.settings;

import adda.application.SettingDialog;
import adda.settings.formatters.json.JsonFormatter;
import adda.settings.formatters.xml.XmlFormatter;
import adda.settings.serializer.AddaSerializer;
import adda.utils.OsUtils;

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
        String settingsDir = getSettingsPath();
        Setting setting;
        try {
            String xml = new String(Files.readAllBytes(Paths.get(settingsDir)), StandardCharsets.UTF_8);
            AddaSerializer serializer = new AddaSerializer(new XmlFormatter());
            setting = serializer.deserialize(xml, Setting.class);
        } catch (IOException e) {
            setting = recreateSettings();
        }

        return setting;
    }

    public static boolean isSettingExist() {
        File f = new File(getSettingsPath());
        return f.exists() && !f.isDirectory();
    }

    public static Setting recreateSettings() {
        String userDir = OsUtils.getDefaultDirectory();
        String firstProjectDir = userDir + File.separator + "adda-project";

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
        appSetting.setDefaultProjectName("ADDA Project");
        appSetting.setDefaultProjectPath(userDir);
        appSetting.setLanguage("EN");
        appSetting.setFontSize(12);
        appSetting.setAddaExecSeq("");
        appSetting.setAddaExecMpi("");
        appSetting.setAddaExecGpu("");
        appSetting.setGitPath("https://api.github.com/repos/adda-team/adda/releases/latest");
        if (OsUtils.isWindows()) {
            String binPath = System.getProperty("user.dir") + "\\bin\\adda.exe";
            appSetting.setAddaExecSeq(binPath);
        }

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
            saveSettings(setting, getSettingsPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return setting;

    }
    public static void saveSettings(Setting setting) {
        String settingsDir = getSettingsPath();
        try {
            saveSettings(setting, settingsDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getSettingsPath() {
        return OsUtils.getDefaultDirectory() + File.separator + "setting.xml";
    }

    public static void saveSettings(Setting setting, String path) throws IOException {
        AddaSerializer serializer = new AddaSerializer(new XmlFormatter());

        String xml = serializer.serialize(setting);
        BufferedWriter output = null;
        try {
            File file = new File(path);
            output = new BufferedWriter(new FileWriter(file));
            output.write(xml);
        } catch ( IOException e ) {
            e.printStackTrace();
        } finally {
            if ( output != null ) {
                output.close();
            }
        }
    }

    public static void openSettingsDialog() {
        SettingDialog dialog = new SettingDialog(getSettings().getAppSetting());
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
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
