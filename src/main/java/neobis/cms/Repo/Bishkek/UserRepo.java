package neobis.cms.Repo.Bishkek;

import neobis.cms.Entity.Bishkek.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findByEmailAndIsActive(String email, boolean isActive);
    User findByEmailIgnoringCase(String email);
    User findByActivationCode(String activationCode);
}
