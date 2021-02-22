package neobis.cms.Entity.Bishkek;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "students")
public class BishStudent extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "student_id")
    private long ID;

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "client_id")
    private BishClient client;

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "group_id")
    private BishGroups group;

    @ManyToOne
    @JoinColumn(name = "payment_id", referencedColumnName = "payment_id")
    private BishPayment payment;

    @Column(name = "prepayment")
    private BigDecimal prepayment;

}
