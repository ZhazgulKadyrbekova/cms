package neobis.cms.Repo.Osh;

import neobis.cms.Entity.Osh.OshPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OshPaymentRepo extends JpaRepository<OshPayment, Long> {
}
