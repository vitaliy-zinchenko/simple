package zinchenko;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by zinchenko on 29.06.14.
 *
 * task-1
 *
 */
@Entity
@Table(name = "sub_bean")
public class SubBean {

    /**
     * task 1
     */
    @Id
    @Column(name = "sub_bean_id")
    private Long id;

    @Column(name = "value")
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
