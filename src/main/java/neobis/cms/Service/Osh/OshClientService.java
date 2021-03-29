package neobis.cms.Service.Osh;

import neobis.cms.Dto.ClientDTO;
import neobis.cms.Entity.Osh.OshClient;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OshClientService {
    String getNewClients();
    OshClient create(OshClient client);
    OshClient parseJson(JSONObject data, OshClient client);
    List<OshClient> getClientsFromJson(JSONObject form);
    Page<OshClient> getAllClientsFromDB(Pageable pageable);
    void addClientsToDB();
    List<OshClient> getAllByStatus(long status);
    List<OshClient> getAllByName(String name);
    Page<OshClient> getWithPredicate(Pageable pageable, List<Long> status, List<Long> course,
                                     List<Long> occupation_id, List<Long> utm_id);
    OshClient create(ClientDTO clientDTO, String userEmail);
    OshClient getClientByName(String name);
    OshClient getClientByID(long id);
    OshClient changeStatus(long id, long status, String username);
    OshClient updateClient(long id, ClientDTO clientDTO, String username);
    void changeCity(long id, String userEmail);
    List<Object> simpleSearch(String nameOrPhone);
}
