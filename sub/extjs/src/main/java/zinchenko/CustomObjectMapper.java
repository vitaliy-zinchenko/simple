package zinchenko;

import org.bson.types.ObjectId;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;

/**
 * Created by zinchenko on 03.01.15.
 */
public class CustomObjectMapper extends ObjectMapper {

    public CustomObjectMapper() {
        SimpleModule module = new SimpleModule("ObjectIdmodule", Version.unknownVersion());
        module.addSerializer(ObjectId.class, new ObjectIdJsonSerializer());
        this.registerModule(module);
    }
}
