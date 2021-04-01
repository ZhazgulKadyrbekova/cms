package neobis.cms.Service.Osh;

import neobis.cms.Dto.ClientDTO;
import neobis.cms.Dto.PaymentDTO;
import neobis.cms.Dto.ResponseMessage;
import neobis.cms.Entity.Osh.OshClient;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface OshClientService {
    String getNewClients();
    OshClient create(OshClient client);
    OshClient parseJson(JSONObject data, OshClient client);
    List<OshClient> getClientsFromJson(JSONObject form);
    Page<OshClient> getAllClientsFromDB(Pageable pageable);
    void addClientsToDB();
    List<OshClient> getAllByStatus(long status);
    List<OshClient> getAllByName(String name);
    Set<OshClient> getWithPredicate(String field, List<Long> status, List<Long> course,
                                     List<Long> occupation_id, List<Long> utm_id);
    OshClient create(ClientDTO clientDTO, String userEmail);
    OshClient getClientByName(String name);
    OshClient getClientByID(long id);
    OshClient changeStatus(long id, long status, String username);
    OshClient updateClient(long id, ClientDTO clientDTO, String username);
    void changeCity(long id, String userEmail);
    List<Object> simpleSearch(String nameOrPhone);
    Set<OshClient> search(String nameOrPhone);
    OshClient addPayment(long clientID, PaymentDTO paymentDTO, String userEmail);
    OshClient editPayment(long clientID, PaymentDTO paymentDTO, long paymentID, String userEmail);
    ResponseMessage deletePayment(long clientID, long paymentID, String userEmail);
}
