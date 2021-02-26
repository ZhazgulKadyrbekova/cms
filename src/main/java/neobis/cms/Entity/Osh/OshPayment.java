package neobis.cms.Entity.Osh;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "Payment")
public class OshPayment extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private long ID;

    @Column(name = "month")
    private String month;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "done")
    private boolean done;

    @Column(name = "method")
    private String method;

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "client_id")
    private OshClient client;
}