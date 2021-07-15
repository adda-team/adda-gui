package adda.utils;

import java.io.File;

import net.lingala.zip4j.*;
import net.lingala.zip4j.exception.ZipException;

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
        return getOsName().startsWith("Windows");
    }

    public static boolean isUnix() {
        return getOsName().startsWith("Linux");
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
}
