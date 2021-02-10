package neobis.cms.Repo.Bishkek;

import neobis.cms.Entity.Bishkek.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findByEmailAndIsActive(String email, boolean isActive);
    User findByEmailIgnoringCase(String email);
    User findByActivationCode(String activationCode);
    List<User> findAllByIsConfirmed(boolean isConfirmed);
    void deleteByEmail(String email);
}
