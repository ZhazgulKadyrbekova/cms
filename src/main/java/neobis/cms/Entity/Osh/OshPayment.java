package neobis.cms.Entity.Osh;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import neobis.cms.Entity.Base;

import javax.persistence.*;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "Payment")
public class OshPayment extends Base {

    @Column(name = "month")
    private String month;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "done")
    private boolean done;

    @ManyToOne
    @JoinColumn(name = "method_id")
    private OshMethod method;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private OshCourses course;

    @Override
    public String toString() {
        return "month=" + month + ", price=" + price.toString() + ", done=" + done + ", method=" + method.getName();
    }
}
