package zinchenko;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * Created by zinchenko on 16.06.14.
 */
@Entity
@Table
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Bean {

    @Id
    @Column(name = "bean_id")
    @GeneratedValue(generator = "bean_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "bean_seq", sequenceName = "bean_seq")
    private Long id;

    private String value;

    public Bean() {
    }

    public Bean(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Bean{" +
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
