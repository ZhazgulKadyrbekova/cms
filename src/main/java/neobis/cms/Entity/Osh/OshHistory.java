package neobis.cms.Entity.Osh;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import neobis.cms.Entity.Base;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "History")
public class OshHistory extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private long ID;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "client_phone")
    private String clientPhone;

    @Column(name = "action")
    private String action;

    @Column(name = "old_data")
    private String oldData;

    @Column(name = "new_data")
    private String newData;
}
