package adda.settings.serializer;

import java.util.Map;

public interface ISerializer {
    public String serialize(Object obj);

    public <T> T deserialize(String text, Class<T> clazz);

    public Map<String, Object> deserialize(String text);
}
