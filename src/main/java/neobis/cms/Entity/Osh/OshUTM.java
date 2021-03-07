package neobis.cms.Entity.Osh;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import neobis.cms.Entity.Base;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "UTM")
public class OshUTM extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "utm_id")
    private long ID;

    @Column(name = "name")
    private String name;

}
