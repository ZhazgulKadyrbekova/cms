package neobis.cms.Service.Bishkek;

import neobis.cms.Dto.ClientDTO;
import neobis.cms.Entity.Bishkek.BishClient;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BishClientService {
    String getNewClients();
    BishClient create(BishClient client);
    BishClient parseJson(JSONObject data, BishClient client);
    List<BishClient> getClientsFromJson(JSONObject form);
    Page<BishClient> getAllClientsFromDB(Pageable pageable);
    void addClientsToDB();
    List<BishClient> getAllByStatus(long status);
    List<BishClient> getAllByName(String name);
    Page<BishClient> getWithPredicate(Pageable pageable, List<Long> status, List<Long> course, List<Long> occupation_id, List<Long> utm_id);
    BishClient create(ClientDTO clientDTO, String userEmail);
    BishClient getClientByName(String name);
    BishClient getClientById(long id);
    BishClient changeStatus(long id, long status, String username);
    BishClient updateClient(long id, ClientDTO clientDTO, String username);
    void changeCity(long id, String userEmail);
    List<Object> simpleSearch(String nameOrPhone);
    List<BishClient> advancedSearch(List<Long> status, List<Long> course, List<Long> occupation);
    List<BishClient> advancedStudentSearch(List<Long> course);
}
