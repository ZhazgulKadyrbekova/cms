package neobis.cms.Repo.Bishkek;

import neobis.cms.Entity.Bishkek.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
    Role findByNameContainingIgnoringCase(String name);
}
