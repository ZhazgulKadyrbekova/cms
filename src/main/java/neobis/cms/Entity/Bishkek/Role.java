package neobis.cms.Entity.Bishkek;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import neobis.cms.Entity.Base;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data

@Entity
@Table(name = "roles")
public class Role extends Base implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private long ID;

    @Column(name = "name")
    private String name;

    @Override
    public String getAuthority() {
        return getName();
    }

    public Role(String name) {
        this.name = name;
    }
}
