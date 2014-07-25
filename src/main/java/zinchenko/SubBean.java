package zinchenko;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by zinchenko on 29.06.14.
 */
@Entity
@Table(name = "sub_bean")
public class SubBean {

    @Id
    @Column(name = "sub_bean_id")
//    @SequenceGenerator(name = "sequence_sub_bean", sequenceName = "sequence_sub_bean")
//    @GeneratedValue(generator = "sequence_sub_bean", strategy = GenerationType.SEQUENCE)
    @GenericGenerator(name="sequence_sub_bean" , strategy="increment")
    @GeneratedValue(generator="sequence_sub_bean")
    private Long id;

    @Column(name = "value")
    private String value;

    @Override
    public String toString() {
        return "SubBean{" +
                "id=" + id +
                ", value='" + value + '\'' +
                '}';
    }

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
