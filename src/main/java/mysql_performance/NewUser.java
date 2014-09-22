package mysql_performance;

import javax.persistence.*;

/**
 * Created by zinchenko on 16.09.14.
 */
@Entity
@Table(name = "new_user")
public class NewUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String password;

    @OneToOne(mappedBy = "user")
    private NewProfile newProfile;

    @Override
    public String toString() {
        return "NewUser{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", newProfile=" + newProfile +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
