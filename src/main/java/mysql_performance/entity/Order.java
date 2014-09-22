package mysql_performance.entity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by zinchenko on 18.09.14.
 */
@Entity
@Table(name = "order_goods")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Boolean payed;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems;

}
