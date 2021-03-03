package neobis.cms.Service.Osh;

import neobis.cms.Dto.ClientDTO;
import neobis.cms.Entity.Bishkek.History;
import neobis.cms.Entity.Osh.OshClient;
import neobis.cms.Entity.Osh.OshStatuses;
import neobis.cms.Exception.ResourceNotFoundException;
import neobis.cms.Repo.Osh.OshStatusesRepo;
import neobis.cms.Repo.Osh.OshClientRepo;
import neobis.cms.Service.Bishkek.HistoryService;
import neobis.cms.Service.Bishkek.UserService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Service
public class OshClientServiceImpl implements OshClientService {

    @Autowired
    private OshClientRepo clientRepo;

    @Autowired
    private OshStatusesRepo statusesRepo;

    @Autowired
    private OshCoursesService coursesService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private UserService userService;

    private final String dataResourceUrl
            = "https://neolabs.dev/mod/api/?api_key=e539509b630b27e47ac594d0dbba4e69&method=getLeads";

    @Override
    public LocalDateTime getDateOfLastClient(List<OshClient> clients) {
        if (clients.isEmpty())
            return null;
        OshClient client = clients.get(0);
        return client.getDateCreated();
    }

    @Override
    public String getNewClients(LocalDateTime dateTime) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, MediaType.APPLICATION_OCTET_STREAM));
        long epoch = dateTime.atZone(ZoneId.systemDefault()).toEpochSecond();// + 1L;
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(dataResourceUrl + "&date_from=" + epoch);
        HttpEntity<String> httpEntity = new HttpEntity<>("", httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, httpEntity, String.class);
        return response.getBody();
    }

    @Override
    public String getNewClients() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, MediaType.APPLICATION_OCTET_STREAM));
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(dataResourceUrl);
        HttpEntity<String> httpEntity = new HttpEntity<>("", httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, httpEntity, String.class);
        return response.getBody();
    }

    @Override
    public OshClient create(OshClient client) {
//        client.setStatus("New");
        client.setCity("Bishkek");
        return clientRepo.save(client);
    }

    @Override
    public OshClient parseJson(JSONObject data, OshClient client) {
        if (data != null) {
            Iterator<String> it = data.keys();
            while (it.hasNext()) {
                String key = it.next();
                try {
                    if (data.get(key) instanceof JSONArray) {
                        JSONArray arry = data.getJSONArray(key);
                        int size = arry.length();
                        for (int i = 0; i < size; i++) {
                            parseJson(arry.getJSONObject(i), client);
                        }
                    } else if (data.get(key) instanceof JSONObject) {
                        parseJson(data.getJSONObject(key), client);
                    } else {
                        boolean target;
                        boolean occupation;
                        boolean experience;
                        switch (key) {
                            case "phone" :
                                client.setPhoneNo(data.getString(key));
                                break;
                            case "url" :
                                if (!data.getString(key).equals("neolabs.dev/"))
                                    client.setUtm(data.getString(key));
                                break;
                            case "value" :
                                target = (data.getString(key).equals("Освоить профессию программиста") ||
                                        data.getString(key).equals("Подготовиться к университету") ||
                                        data.getString(key).equals("Участвовать в олимпиадах") ||
                                        data.getString(key).equals("Повышение квалификации"));
                                occupation = (data.getString(key).equals("Студент") || data.getString(key).equals("Школьник") ||
                                        data.getString(key).equals("Временно безработный") || data.getString(key).equals("Работающий") ||
                                        data.getString(key).equals("Предприниматель"));
                                experience = data.getString(key).equals("Да");
                                if (target) {
                                    client.setTarget(data.getString(key));
                                }
                                if (occupation) {
                                    client.setOccupation(data.getString(key));
                                }
                                if (experience) {
                                    client.setExperience(experience);
                                }
                                break;
                            case "form_name" :
                                client.setFormName(data.getString(key));
                                break;
                            case "time" :
                                long time = Long.parseLong(data.getString(key));
                                LocalDateTime date =
                                        LocalDateTime.ofInstant(Instant.ofEpochSecond(time), ZoneId.systemDefault());
                                client.setDateCreated(date);
                                break;
                        }

                    }
                } catch (Throwable e) {
                    e.printStackTrace();

                }
            }
        }
        return client;
    }

    @Override
    public List<OshClient> getClientsFromJson(JSONObject form) {
        List <OshClient> clients = new ArrayList<>();
        if (form != null) {
            JSONObject data = (JSONObject) form.get("data");
            if (data.get("leads") instanceof JSONObject) {
                JSONObject leads = data.getJSONObject("leads");
                Iterator<String> it = leads.keys();
                while (it.hasNext()) {
                    String key = it.next();
                    try {
                        if (leads.get(key) instanceof JSONObject) {
                            clients.add(parseJson(leads.getJSONObject(key), new OshClient()));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();

                    }
                }
            } else if (data.get("leads") instanceof JSONArray) {
                JSONArray array = data.getJSONArray("leads");
                int size = array.length();
                for (int i = 0; i < size; i++) {
                    parseJson(array.getJSONObject(i), new OshClient());
                }
            }


        } else {
            return null;
        }
        return clients;
    }

    @Override
    public List<OshClient> getAllClientsFromDB() {
        return clientRepo.findAllByDeletedOrderByDateCreatedDesc(false);
    }

    @Override
    public void addClientsToDB() {
        LocalDateTime dateTime = this.getDateOfLastClient(clientRepo.findAllByDeletedOrderByDateCreatedDesc(false));
        JSONObject data;
        if (dateTime == null)
            data = new JSONObject(this.getNewClients());
        else
            data = new JSONObject(this.getNewClients(dateTime));
        List<OshClient> clients = this.getClientsFromJson(data);
        for (OshClient client : clients)
            this.create(client);
    }

    @Override
    public List<OshClient> getAllByStatus(String status) {
        OshStatuses oshStatuses = statusesRepo.findByNameContainingIgnoringCase(status);
        return clientRepo.findAllByDeletedAndStatusOrderByDateCreatedDesc(false, oshStatuses);
    }

    @Override
    public OshClient create(ClientDTO clientDTO) {
        OshClient client = new OshClient();
        client.setDateCreated(LocalDateTime.now());
        client.setPhoneNo(clientDTO.getPhoneNo());
        client.setName(clientDTO.getName());
        client.setEmail(clientDTO.getEmail());
        client.setStatus(statusesRepo.findById(clientDTO.getStatus())
                .orElseThrow(() -> new ResourceNotFoundException("Status with id " + clientDTO.getStatus() + " has not found")));
        client.setOccupation(clientDTO.getOccupation());
        client.setTarget(clientDTO.getTarget());
        client.setExperience(clientDTO.isExperience());
        client.setLaptop(clientDTO.isLaptop());
        client.setCourse(coursesService.findCourseById(clientDTO.getCourse()));
        client.setDescription(clientDTO.getDescription());
        client.setCity("OSH");
        client.setTimer(LocalDateTime.now().plusHours(24L));
        client.setPrepayment(clientDTO.getPrepayment());
        client.setLeavingReason(clientDTO.getLeavingReason());
        return clientRepo.save(client);
    }

    @Override
    public OshClient getClientByName(String name) {
        return clientRepo.findByNameContainingIgnoringCaseAndDeleted(name, false);
    }

    @Override
    public OshClient getClientByID(long id) {
        return clientRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " has not found"));
    }

    @Override
    public OshClient changeStatus(long id, long status, String username) {
        OshClient client = clientRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " has not found"));
        History history = new History();
        history.setAction("change status");
        history.setOldData(client.getStatus().getName());
        history.setUser(userService.findByEmail(username));

        OshStatuses statuses = statusesRepo.findById(status)
                .orElseThrow(() -> new ResourceNotFoundException("Status with id " + id + " has not found"));
        history.setNewData(statuses.getName());

        client.setStatus(statuses);
        client = clientRepo.save(client);

        historyService.create(history);
        return client;
    }

    @Override
    public OshClient updateClient(long id, ClientDTO clientDTO, String username) {
        OshClient client = clientRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " has not found"));
        History history = new History();
        history.setAction("update client info");
        history.setOldData(client.getStatus().getName());
        history.setUser(userService.findByEmail(username));

        OshStatuses statuses = statusesRepo.findById(clientDTO.getStatus())
                .orElseThrow(() -> new ResourceNotFoundException("Status with id " + id + " has not found"));
        history.setNewData(statuses.getName());

        client.setStatus(statuses);
        client.setPhoneNo(clientDTO.getPhoneNo());
        client.setName(clientDTO.getName());
        client.setEmail(clientDTO.getEmail());
        client.setOccupation(clientDTO.getOccupation());
        client.setTarget(clientDTO.getTarget());
        client.setExperience(clientDTO.isExperience());
        client.setLaptop(clientDTO.isLaptop());
        client.setCourse(coursesService.findCourseById(clientDTO.getCourse()));
        client.setDescription(clientDTO.getDescription());
//        client.setCity("OSH");
        if (clientDTO.getTimer() == null)
            client.setTimer(LocalDateTime.now().plusHours(24L));
        client.setTimer(clientDTO.getTimer());
        client.setPrepayment(clientDTO.getPrepayment());
        client.setLeavingReason(clientDTO.getLeavingReason());
        client = clientRepo.save(client);

        if (!history.getNewData().equals(history.getOldData()))
            historyService.create(history);
        return client;
    }
}
