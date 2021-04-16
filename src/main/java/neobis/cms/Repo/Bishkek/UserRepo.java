package neobis.cms.Repo.Bishkek;

import neobis.cms.Entity.Bishkek.BishPosition;
import neobis.cms.Entity.Bishkek.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findByEmailIgnoringCaseAndActive(String email, boolean isActive);
    User findByEmailIgnoringCase(String email);
    User findByActivationCode(String activationCode);
    List<User> findAllByConfirmed(boolean isConfirmed);
    List<User> findAllByNameContainingIgnoringCaseAndActiveAndConfirmed(String name, boolean active, boolean confirmed);
    List<User> findAllBySurnameContainingIgnoringCaseAndActiveAndConfirmed(String surname, boolean active, boolean confirmed);
    List<User> findAllByPhoneNoContainingAndActiveAndConfirmed(String phoneNo, boolean active, boolean confirmed);
    List<User> findAllByCityContainingIgnoringCase(String city);
    List<User> findAllByPositionAndCityContainingIgnoringCaseAndActiveAndConfirmed(BishPosition position,
                                                                                   String city, boolean active, boolean confirmed);
    List<User> findAllByEmailContainingIgnoringCaseAndActiveAndConfirmed(String email, boolean active, boolean confirmed);
    List<User> findAllByPosition(BishPosition position);
    void deleteByEmail(String email);
}
