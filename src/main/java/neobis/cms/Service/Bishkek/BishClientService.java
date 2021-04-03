package neobis.cms.Service.Bishkek;

import neobis.cms.Dto.ClientDTO;
import neobis.cms.Dto.PaymentDTO;
import neobis.cms.Dto.ResponseMessage;
import neobis.cms.Entity.Bishkek.BishClient;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface BishClientService {
    String getNewClients();
    BishClient create(BishClient client);
    BishClient parseJson(JSONObject data, BishClient client);
    List<BishClient> getClientsFromJson(JSONObject form);
    Page<BishClient> getAllClientsFromDB(Pageable pageable);
    void addClientsToDB();
    List<BishClient> getAllByStatus(long status);
    List<BishClient> getAllByName(String name);
    Set<BishClient> getWithPredicate(String field, List<Long> status, List<Long> course, List<Long> occupation_id, List<Long> utm_id);
    BishClient create(ClientDTO clientDTO, String userEmail);
    BishClient getClientByName(String name);
    BishClient getClientById(long id);
    BishClient changeStatus(long id, long status, String username);
    BishClient updateClient(long id, ClientDTO clientDTO, String username);
    void changeCity(long id, String userEmail);
    Set<Object> simpleSearch(String nameOrPhone);
    Set<BishClient> search(String nameOrPhone);
//    List<BishClient> advancedSearch(List<Long> status, List<Long> course, List<Long> occupation);
//    List<BishClient> advancedStudentSearch(List<Long> course);
    BishClient addNewPayment(long clientID, PaymentDTO paymentDTO, String userEmail);
    BishClient editPayment(long clientID, PaymentDTO paymentDTO, long paymentID, String userEmail);
    ResponseMessage deletePayment(long clientID, long paymentID, String userEmail);
    ResponseMessage deleteClient(long clientID, String userEmail);
}
