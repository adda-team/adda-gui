package adda.settings.serializer;

import adda.settings.formatters.json.JsonFormatter;
import adda.settings.Setting;
import adda.settings.AppSetting;
import adda.settings.ProjectSetting;
//import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

class AddaSerializerTest {

    //@Test
    public void testSerializeObject() {
        ProjectSetting projectSetting = new ProjectSetting();
        projectSetting.setName("name");
        projectSetting.setPath("path");
        AddaSerializer serializer = new AddaSerializer(new JsonFormatter());
        String output = serializer.serialize(projectSetting);
    }

    //@Test
    public void testSerializePrimitiveList() {
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            stringList.add("string_" + i);
        }

        AddaSerializer serializer = new AddaSerializer(new JsonFormatter());
        String output = serializer.serialize(stringList);
    }

    //@Test
    public void testSerializeList() {
        List<ProjectSetting> projectSettingList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ProjectSetting projectSetting = new ProjectSetting();
            projectSetting.setName("name_" + i);
            projectSetting.setPath("path_" + i);
            projectSettingList.add(projectSetting);
        }

        AddaSerializer serializer = new AddaSerializer(new JsonFormatter());
        String output = serializer.serialize(projectSettingList);
    }

    //@Test
    public void testSerializePrimitiveMap() {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            map.put("key_" + i, "value_" + i);
        }

        AddaSerializer serializer = new AddaSerializer(new JsonFormatter());
        String output = serializer.serialize(map);
    }

    //@Test
    public void testSerializeMap() {
        Map<String, ProjectSetting> map = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            ProjectSetting projectSetting = new ProjectSetting();
            projectSetting.setName("name_" + i);
            projectSetting.setPath("path_" + i);
            map.put("key_" + i, projectSetting);
        }

        AddaSerializer serializer = new AddaSerializer(new JsonFormatter());
        String output = serializer.serialize(map);
    }

    //@Test
    public void testSerialize() {
        Map<String, Object> defaultValues = new HashMap<String, Object>();
        for (int i = 0; i < 5; i++) {
            Object obj = "Value_" + i;
            defaultValues.put("Key_" + i, obj);
        }

        AppSetting appSetting = new AppSetting();
        appSetting.setLanguage("ru");

        appSetting.setDefaultProjectName("projectName");

        List<ProjectSetting> projectSettingList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ProjectSetting projectSetting = new ProjectSetting();
            projectSetting.setName("name_"+ i);
            projectSetting.setPath("path_"+ i);
            projectSettingList.add(projectSetting);
        }

        Setting setting = new Setting();
        setting.setAppSetting(appSetting);
        setting.setProjects(projectSettingList);

        AddaSerializer serializer = new AddaSerializer(new JsonFormatter());
        String output = serializer.serialize(setting);
    }
}