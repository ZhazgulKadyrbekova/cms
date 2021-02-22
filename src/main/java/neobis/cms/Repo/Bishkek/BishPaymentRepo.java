package neobis.cms.Repo.Bishkek;

import neobis.cms.Entity.Bishkek.BishPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BishPaymentRepo extends JpaRepository<BishPayment, Long> {
}
