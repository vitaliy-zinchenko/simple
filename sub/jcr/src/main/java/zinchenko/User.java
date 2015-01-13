package zinchenko;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.jcrom.annotations.JcrName;
import org.jcrom.annotations.JcrNode;
import org.jcrom.annotations.JcrPath;
import org.jcrom.annotations.JcrProperty;

/**
 * Created by zinchenko on 04.01.15.
 */
public class User {

    @JcrName
    private String name;

    @JcrPath
    private String path;

    @JcrProperty(name = "em")
    private String email;

    @JcrProperty(name = "address/street")
    private String street;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User == false) {
             return false;
           }
        if (this == obj) {
             return true;
           }
        User user = (User) obj;
        return new EqualsBuilder()
                .append(name, user.name)
                .append(path, user.path)
                .append(email, user.email)
                .isEquals();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
