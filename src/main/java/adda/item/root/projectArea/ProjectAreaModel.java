package adda.item.root.projectArea;

import adda.base.models.ModelBase;

public class ProjectAreaModel extends ModelBase {

    private static final String IS_ACTIVE_FIELD_NAME = "isActive";
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
    
}
