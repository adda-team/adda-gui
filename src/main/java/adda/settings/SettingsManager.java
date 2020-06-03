package adda.settings;

import adda.settings.formatters.json.JsonFormatter;
import adda.settings.serializer.AddaSerializer;

public class SettingsManager {
    //todo read from file
    //todo observe when settings file changed
    public static Setting getSettings() {
        String xml = "<Setting>\n" +
                "  <appSetting>\n" +
                "    <language>ru</language>\n" +
                "    <defaultProjectName>qwe</defaultProjectName>\n" +
                "  </appSetting>\n" +
                "  <projects>\n" +
                "    <ProjectSetting>\n" +
                "      <path>test_path_1</path>\n" +
                "      <name>proj1</name>\n" +
                "    </ProjectSetting>\n" +
                "    <ProjectSetting>\n" +
                "      <path>test_path_2</path>\n" +
                "      <name>proj2</name>\n" +
                "    </ProjectSetting>\n" +
                "  </projects>\n" +
                "</Setting>";
        String json = "{\n" +
                "    \"Setting\" : \n" +
                "    {\n" +
                "        \"appSetting\" : \n" +
                "        {\n" +
                "            \"language\" : \"ru\",\n" +
                "            \"defaultProjectName\" : \"qwe\"\n" +
                "        },\n" +
                "        \"projects\" : \n" +
                "                [                \n" +
                "                    {\n" +
                "                        \"path\" : \"test_path_1\",\n" +
                "                        \"name\" : \"proj1\"\n" +
                "                    },\n" +
                "                \n" +
                "                    {\n" +
                "                        \"path\" : \"test_path_2\",\n" +
                "                        \"name\" : \"proj2\"\n" +
                "                    }\n" +
                "                ]\n" +
                "\n" +
                "    }\n" +
                "}";

        AddaSerializer serializer = new AddaSerializer(new JsonFormatter());

        return serializer.deserialize(json, Setting.class);
    }
}
