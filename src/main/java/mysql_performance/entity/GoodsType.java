package mysql_performance.entity;

import javax.persistence.*;

/**
 * Created by zinchenko on 18.09.14.
 */
@Entity
@Table(name = "goods_type")
public class GoodsType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
