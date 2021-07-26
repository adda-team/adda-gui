package adda.utils;

import java.io.File;

import net.lingala.zip4j.*;
import net.lingala.zip4j.exception.ZipException;

import javax.swing.filechooser.FileSystemView;

public final class OsUtils
{
    private static String OS = null;
    public static String getOsName()
    {
        if(OS == null) { OS = System.getProperty("os.name"); }
        return OS;
    }
    public static boolean isWindows()
    {
        return getOsName().toLowerCase().startsWith("windows");
    }

    public static boolean isLinux() {
        return getOsName().toLowerCase().startsWith("linux");
    }
    public static boolean isMac() {
        return getOsName().toLowerCase().startsWith("mac");
    }

    public static boolean createFolder(String path) {
        File file = new File(path);
        //deleteFolder(file);
        boolean isCreated = true;
        if (!file.exists()) {
            if (!file.mkdir()) {
                //throw new FileNotFoundException("Directory " + firstProjectDir + "cannot be created");
                isCreated = false;
            }
        }

        return isCreated;
    }


    public static void unzip(String targetZipFilePath, String destinationFolderPath) throws ZipException {
            new ZipFile(targetZipFilePath).extractAll(destinationFolderPath);
    }

    public static String getDefaultDirectory() {
        String defaultDir = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
        String addaGuiPostfix = "AddaGui";

        String defaulAddaGuiPath = defaultDir + File.separator + addaGuiPostfix;

        if (createFolder(defaulAddaGuiPath)) {
            return defaulAddaGuiPath;
        }

        return System.getProperty("user.dir");

    }
}
