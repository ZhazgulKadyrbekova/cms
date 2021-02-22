package neobis.cms.Repo.Bishkek;

import neobis.cms.Entity.Bishkek.BishGroups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BishGroupsRepo extends JpaRepository<BishGroups, Long> {
}
