package adda.item.root.projectTree;

import adda.base.models.ModelBase;

public class ProjectTreeNewItemModel  extends ModelBase {

    public static final String DIRECTORY_MODEL_FIELD_NAME = "directory";
    public static final String DISPLAY_NAME_MODEL_FIELD_NAME = "displayName";
    public static final String FOLDER_MODEL_FIELD_NAME = "folderName";


    String directory;
    String displayName;
    String folderName;

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        if(this.folderName == null || !this.folderName.equals(folderName)) {
            this.folderName = folderName;
            notifyObservers(FOLDER_MODEL_FIELD_NAME, folderName);
        }
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        if(this.displayName == null || !this.displayName.equals(displayName)) {
            this.displayName = displayName;
            notifyObservers(DISPLAY_NAME_MODEL_FIELD_NAME, displayName);
        }
    }


    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        if(this.directory == null || !this.directory.equals(directory)) {
            this.directory = directory;
            notifyObservers(DIRECTORY_MODEL_FIELD_NAME, directory);
        }
    }






}
