package neobis.cms.Service.Bishkek;

import neobis.cms.Dto.ClientDTO;
import neobis.cms.Entity.Bishkek.BishClient;
import neobis.cms.Entity.Bishkek.BishStatuses;
import neobis.cms.Entity.Bishkek.History;
import neobis.cms.Exception.ResourceNotFoundException;
import neobis.cms.Repo.Bishkek.BishClientRepo;
import neobis.cms.Repo.Bishkek.BishStatusesRepo;
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
import java.util.*;

@Service
public class BishClientServiceImpl implements BishClientService {

    @Autowired
    private BishClientRepo clientRepo;

    @Autowired
    private BishStatusesRepo statusesRepo;

    @Autowired
    private BishCoursesService coursesService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private UserService userService;

    private final String dataResourceUrl
            = "https://neolabs.dev/mod/api/?api_key=e539509b630b27e47ac594d0dbba4e69&method=getLeads";

    @Override
    public LocalDateTime getDateOfLastClient(List<BishClient> clients) {
        if (clients.isEmpty())
            return null;
        BishClient client = clients.get(0);
        return client.getDateCreated();
    }

    @Override
    public String getNewClients(LocalDateTime dateTime) {
        RestTemplate restTemplate = new RestTemplate();
        org.springframework.http.HttpHeaders httpHeaders = new HttpHeaders();
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
        org.springframework.http.HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, MediaType.APPLICATION_OCTET_STREAM));
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(dataResourceUrl);
        HttpEntity<String> httpEntity = new HttpEntity<>("", httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, httpEntity, String.class);
        return response.getBody();
    }

    @Override
    public BishClient create(BishClient client) {
//        client.setStatus("New");
        client.setCity("Bishkek");
        return clientRepo.save(client);
    }

    @Override
    public BishClient parseJson(JSONObject data, BishClient client) {
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
    public List<BishClient> getClientsFromJson(JSONObject form) {
        List <BishClient> clients = new ArrayList<>();
        if (form != null) {
            JSONObject data = (JSONObject) form.get("data");
            if (data.get("leads") instanceof JSONObject) {
                JSONObject leads = data.getJSONObject("leads");
                Iterator<String> it = leads.keys();
                while (it.hasNext()) {
                    String key = it.next();
                    try {
                        if (leads.get(key) instanceof JSONObject) {
                            clients.add(parseJson(leads.getJSONObject(key), new BishClient()));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();

                    }
                }
            } else if (data.get("leads") instanceof JSONArray) {
                JSONArray array = data.getJSONArray("leads");
                int size = array.length();
                for (int i = 0; i < size; i++) {
                    parseJson(array.getJSONObject(i), new BishClient());
                }
            }


        } else {
            return null;
        }
        return clients;
    }

    @Override
    public List<BishClient> getAllClientsFromDB() {
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
        List<BishClient> clients = this.getClientsFromJson(data);
        for (BishClient client : clients)
            this.create(client);
    }

    @Override
    public List<BishClient> getAllByStatus(String status) {
        BishStatuses bishStatuses = statusesRepo.findByNameContainingIgnoringCase(status);
        return clientRepo.findAllByDeletedAndStatusOrderByDateCreatedDesc(false, bishStatuses);
    }

    @Override
    public BishClient create(ClientDTO clientDTO) {
        BishClient client = new BishClient();
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
        client.setCity("BISHKEK");
        client.setTimer(LocalDateTime.now().plusHours(24L));
        client.setPrepayment(clientDTO.getPrepayment());
        client.setLeavingReason(clientDTO.getLeavingReason());
        return clientRepo.save(client);
    }

    @Override
    public BishClient getClientByName(String name) {
        return clientRepo.findByNameContainingIgnoringCaseAndDeleted(name, false);
    }

    @Override
    public BishClient getClientById(long id) {
        return clientRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " has not found"));
    }

    @Override
    public BishClient changeStatus(long id, long status, String username) {
        BishClient client = clientRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " has not found"));
        History history = new History();
        history.setAction("change status");
        if (client.getStatus() == null)
            history.setOldData(null);
        else
            history.setOldData(client.getStatus().getName());
        history.setUser(userService.findByEmail(username));

        BishStatuses statuses = statusesRepo.findById(status)
                .orElseThrow(() -> new ResourceNotFoundException("Status with id " + id + " has not found"));
        history.setNewData(statuses.getName());

        client.setStatus(statuses);
        client.setTimer(LocalDateTime.now().plusHours(24L));
        client = clientRepo.save(client);

        historyService.create(history);
        return client;
    }

    @Override
    public BishClient updateClient(long id, ClientDTO clientDTO, String username) {
        BishClient client = clientRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " has not found"));
        History history = new History();
        history.setAction("update client info");
        if (client.getStatus() == null)
            history.setOldData(null);
        else
            history.setOldData(client.getStatus().getName());
        history.setUser(userService.findByEmail(username));

        BishStatuses statuses = statusesRepo.findById(clientDTO.getStatus())
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
//        client.setCity("BISHKEK");
        if (clientDTO.getTimer() == null)
            client.setTimer(LocalDateTime.now().plusHours(24L));
        else    client.setTimer(clientDTO.getTimer());
        client.setPrepayment(clientDTO.getPrepayment());
        client.setLeavingReason(clientDTO.getLeavingReason());
        client = clientRepo.save(client);

        if (!history.getNewData().equals(history.getOldData()))
            historyService.create(history);
        return client;
    }
}
