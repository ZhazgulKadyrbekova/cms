package neobis.cms.Repo.Osh;

import neobis.cms.Entity.Osh.OshMethod;
import neobis.cms.Entity.Osh.OshPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OshPaymentRepo extends JpaRepository<OshPayment, Long> {
    List<OshPayment> findAllByMethod(OshMethod method);
}
