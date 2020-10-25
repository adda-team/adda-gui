package adda.item.tab.output.actualDirectory;

import adda.base.annotation.Viewable;
import adda.base.models.ModelBase;

public class ActualDirectoryModel extends ModelBase {


    @Viewable
    String dir;

    public ActualDirectoryModel() {
        this.setLabel("Directory");//todo localization
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        if(this.dir == null || !this.dir.equals(dir)) {
            this.dir = dir;
            notifyObservers("dir", dir);
        }
    }
}