package mongo;


import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * Created by zinchenko on 12.11.14.
 */
@Entity(value = "morphia_bean", noClassnameStored = true)
public class MorphiaBean {

    @Id
    private ObjectId id;

    private String name;

    private String type;

    public MorphiaBean() {
    }

    public MorphiaBean(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
