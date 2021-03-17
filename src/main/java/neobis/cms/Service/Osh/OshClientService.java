package neobis.cms.Service.Osh;

import neobis.cms.Dto.ClientDTO;
import neobis.cms.Entity.Osh.OshClient;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface OshClientService {
    LocalDateTime getDateOfLastClient(List<OshClient> clients);
    String getNewClients(LocalDateTime dateTime);
    String getNewClients();
    OshClient create(OshClient client);
    OshClient parseJson(JSONObject data, OshClient client);
    List<OshClient> getClientsFromJson(JSONObject form);
    Page<OshClient> getAllClientsFromDB(Pageable pageable);
    void addClientsToDB();
    List<OshClient> getAllByStatus(long status);
    List<OshClient> getAllByName(String name);
    Page<OshClient> getWithPredicate(Pageable pageable, List<Long> status, List<Long> course, List<Long> occupation_id);
    OshClient create(ClientDTO clientDTO, String userEmail);
    OshClient getClientByName(String name);
    OshClient getClientByID(long id);
    OshClient changeStatus(long id, long status, String username);
    OshClient updateClient(long id, ClientDTO clientDTO, String username);
    void changeCity(long id, String userEmail);
    Page<OshClient> search(Pageable pageable, String nameOrPhone);
}
