package neobis.cms.Service.Osh;

import neobis.cms.Dto.ClientDTO;
import neobis.cms.Entity.Osh.OshClient;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.List;

public interface OshClientService {
    LocalDateTime getDateOfLastClient(List<OshClient> clients);
    String getNewClients(LocalDateTime dateTime);
    String getNewClients();
    OshClient create(OshClient client);
    OshClient parseJson(JSONObject data, OshClient client);
    List<OshClient> getClientsFromJson(JSONObject form);
    List<OshClient> getAllClientsFromDB();
    void addClientsToDB();
    List<OshClient> getAllByStatus(String status);
    OshClient create(ClientDTO clientDTO);
    OshClient getClientByName(String name);
}
