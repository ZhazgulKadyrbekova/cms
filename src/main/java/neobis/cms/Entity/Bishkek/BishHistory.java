package neobis.cms.Entity.Bishkek;

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
public class BishHistory extends Base {

    @Column(name = "user_full_name")
    private String fullName;

    @Column(name = "client_phone")
    private String clientPhone;

    @Column(name = "action")
    private String action;

    @Column(name = "old_data")
    private String oldData;

    @Column(name = "new_data")
    private String newData;
}
