package adda.item.root.shortcut;

import adda.base.models.ModelBase;
import adda.item.tab.base.propagation.PropagationEnum;
import adda.settings.AppSetting;
import adda.settings.SettingsManager;
import adda.utils.StringHelper;


public class ShortcutsModel  extends ModelBase {

    public ShortcutsModel() {
        setLabel("");
    }

    public void refreshAddaVersion() {
        final AppSetting appSetting = SettingsManager.getSettings().getAppSetting();
        String addaVersion = SettingsManager.getAddaVersion(appSetting.getAddaExecSeq());
        if (StringHelper.isEmpty(addaVersion)) {
            addaVersion = "Please use File->Settings to configure ADDA";
        }
        setLabel(addaVersion);
    }
}
