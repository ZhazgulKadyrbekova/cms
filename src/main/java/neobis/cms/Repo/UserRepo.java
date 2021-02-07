package neobis.cms.Repo;

import neobis.cms.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findByEmailAndIsActive(String email, boolean isActive);
    User findByEmailIgnoringCase(String email);
    User findByActivationCode(String activationCode);
}
