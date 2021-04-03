package neobis.cms.Entity.Bishkek;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import neobis.cms.Entity.Base;

import javax.persistence.*;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data

@Entity
@Table(name = "users")
public class User extends Base {

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone_no")
    private String phoneNo;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "patronymic")
    private String patronymic;

    @ManyToOne
    @JoinColumn(name = "position_id")
    private BishPosition position;

    @Column(name = "city")
    private String city;

    @Column(name = "is_confirmed")
    private boolean confirmed;

    @Column(name = "activation_code")
    private String activationCode;

    @Column(name = "is_active")
    private boolean active;

    @JsonIgnore
    @Column(name = "password")
    @Size(min = 8)
    private String password;

    @ManyToOne//(cascade = {CascadeType.ALL})
    @JoinColumn(name = "role_id")
    private Role role;

}
