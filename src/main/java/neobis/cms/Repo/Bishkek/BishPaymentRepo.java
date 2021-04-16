package neobis.cms.Repo.Bishkek;

import neobis.cms.Entity.Bishkek.BishMethod;
import neobis.cms.Entity.Bishkek.BishPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BishPaymentRepo extends JpaRepository<BishPayment, Long> {
    List<BishPayment> findAllByMethod(BishMethod method);
}
