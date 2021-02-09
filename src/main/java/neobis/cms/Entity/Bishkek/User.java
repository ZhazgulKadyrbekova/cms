package neobis.cms.Entity.Bishkek;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import neobis.cms.Entity.Base;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data

@Entity
@Table(name = "users")
public class User extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long ID;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone_no")
    private String phoneNo;

    @Column(name = "name")
    private String name;

    @Column(name = "position")
    private String position;

    @Column(name = "city")
    private String city;

    @Column(name = "activation_code")
    private String activationCode;

    @Column(name = "is_active")
    private boolean isActive;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @ManyToOne//(cascade = {CascadeType.ALL})
    @JoinColumn(name = "role_id")
    private Role role;
}
