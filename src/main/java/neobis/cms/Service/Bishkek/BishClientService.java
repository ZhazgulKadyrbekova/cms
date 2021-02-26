package neobis.cms.Service.Bishkek;

import neobis.cms.Dto.ClientDTO;
import neobis.cms.Entity.Bishkek.BishClient;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.List;

public interface BishClientService {
    LocalDateTime getDateOfLastClient(List<BishClient> clients);
    String getNewClients(LocalDateTime dateTime);
    String getNewClients();
    BishClient create(BishClient client);
    BishClient parseJson(JSONObject data, BishClient client);
    List<BishClient> getClientsFromJson(JSONObject form);
    List<BishClient> getAllClientsFromDB();
    void addClientsToDB();
    List<BishClient> getAllByStatus(String status);
    BishClient create(ClientDTO clientDTO);
    BishClient getClientByName(String name);
    BishClient getClientById(long id);
}
