package neobis.cms.Service.Bishkek;

import neobis.cms.Dto.ClientDTO;
import neobis.cms.Entity.Bishkek.*;
import neobis.cms.Entity.Osh.OshClient;
import neobis.cms.Entity.Osh.OshOccupation;
import neobis.cms.Entity.Osh.OshPayment;
import neobis.cms.Entity.Osh.OshUTM;
import neobis.cms.Exception.ResourceNotFoundException;
import neobis.cms.Repo.Bishkek.BishClientRepo;
import neobis.cms.Repo.Bishkek.BishOccupationRepo;
import neobis.cms.Repo.Bishkek.BishStatusesRepo;
import neobis.cms.Repo.Bishkek.BishUTMRepo;
import neobis.cms.Repo.Osh.OshClientRepo;
import neobis.cms.Repo.Osh.OshOccupationRepo;
import neobis.cms.Repo.Osh.OshStatusesRepo;
import neobis.cms.Repo.Osh.OshUTMRepo;
import neobis.cms.Search.GenericSpecification;
import neobis.cms.Search.SearchCriteria;
import neobis.cms.Search.SearchOperation;
import neobis.cms.Service.Osh.OshPaymentService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.annotation.Lazy;
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

    private final BishClientRepo bishClientRepo;
    private final OshClientRepo oshClientRepo;

    private final BishStatusesRepo bishStatusesRepo;
    private final OshStatusesRepo oshStatusesRepo;

    private final BishOccupationRepo bishOccupationRepo;
    private final OshOccupationRepo oshOccupationRepo;

    private final BishUTMRepo bishUTMRepo;
    private final OshUTMRepo oshUTMRepo;

    private final BishCoursesService coursesService;

    private  BishPaymentService bishPaymentService;
    private  OshPaymentService oshPaymentService;

    private final BishHistoryService bishHistoryService;

    private final UserService userService;

    private final String dataResourceUrl
            = "https://neolabs.dev/mod/api/?api_key=e539509b630b27e47ac594d0dbba4e69&method=getLeads";

    public BishClientServiceImpl(BishStatusesRepo statusesRepo, BishClientRepo bishClientRepo, OshClientRepo oshClientRepo, OshStatusesRepo oshStatusesRepo,
                                 BishOccupationRepo bishOccupationRepo, OshOccupationRepo oshOccupationRepo, BishUTMRepo bishUTMRepo, OshUTMRepo oshUTMRepo,
                                 BishCoursesService coursesService, @Lazy BishPaymentService bishPaymentService, @Lazy OshPaymentService oshPaymentService,
                                 BishHistoryService bishHistoryService, UserService userService) {
        this.bishStatusesRepo = statusesRepo;
        this.bishClientRepo = bishClientRepo;
        this.oshClientRepo = oshClientRepo;
        this.oshStatusesRepo = oshStatusesRepo;
        this.bishOccupationRepo = bishOccupationRepo;
        this.oshOccupationRepo = oshOccupationRepo;
        this.bishUTMRepo = bishUTMRepo;
        this.oshUTMRepo = oshUTMRepo;
        this.coursesService = coursesService;
        this.bishPaymentService = bishPaymentService;
        this.oshPaymentService = oshPaymentService;
        this.bishHistoryService = bishHistoryService;
        this.userService = userService;
    }

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
    public BishClient create(BishClient client) {
//        client.setStatus("New");
        client.setCity("Bishkek");
        return bishClientRepo.save(client);
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
                                if (!data.getString(key).equals("neolabs.dev/")) {
                                    String utmName = data.getString(key);
                                    BishUTM utm;
                                    if (utmName.contains("instagram")) {
                                        utm = bishUTMRepo.findByNameContainingIgnoringCase("instagram").orElse(null);
                                        if (utm == null) {
                                            oshUTMRepo.save(new OshUTM(0, "instagram"));
                                            utm = bishUTMRepo.save(new BishUTM(0, "instagram"));
                                        }
                                        client.setUtm(utm);
                                    }
                                    if (utmName.contains("facebook")) {
                                        utm = bishUTMRepo.findByNameContainingIgnoringCase("facebook").orElse(null);
                                        if (utm == null) {
                                            oshUTMRepo.save(new OshUTM(0, "facebook"));
                                            utm = bishUTMRepo.save(new BishUTM(0, "facebook"));
                                        }
                                        client.setUtm(utm);
                                    }
                                    if (utmName.contains("google")) {
                                        utm = bishUTMRepo.findByNameContainingIgnoringCase("google").orElse(null);
                                        if (utm == null) {
                                            oshUTMRepo.save(new OshUTM(0, "google"));
                                            utm = bishUTMRepo.save(new BishUTM(0, "google"));
                                        }
                                        client.setUtm(utm);
                                    }
                                    if (utmName.contains("neobis")) {
                                        utm = bishUTMRepo.findByNameContainingIgnoringCase("neobis").orElse(null);
                                        if (utm == null) {
                                            oshUTMRepo.save(new OshUTM(0, "neobis"));
                                            utm = bishUTMRepo.save(new BishUTM(0, "neobis"));
                                        }
                                        client.setUtm(utm);
                                    }

                                }
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
                                    String occupationName = data.getString(key);
                                    BishOccupation bishOccupation = bishOccupationRepo.findByNameContainingIgnoringCase(occupationName).orElse(null);
                                    if (bishOccupation == null) {
                                        oshOccupationRepo.save(new OshOccupation(0, occupationName));
                                        bishOccupation = bishOccupationRepo.save(new BishOccupation(0, occupationName));
                                    }
                                    client.setOccupation(bishOccupation);
                                }
                                client.setExperience(experience);
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
        return bishClientRepo.findAllByOrderByDateCreatedDesc();
    }

    @Override
    public void addClientsToDB() {
        LocalDateTime dateTime = this.getDateOfLastClient(bishClientRepo.findAllByOrderByDateCreatedDesc());
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
    public List<BishClient> getAllByStatus(long status) {
        BishStatuses bishStatuses = bishStatusesRepo.findById(status)
                .orElseThrow(() -> new ResourceNotFoundException("Status with id " + status + " has not found"));
        return bishClientRepo.findAllByStatusOrderByDateCreatedDesc(bishStatuses);
    }

    @Override
    public List<BishClient> getAllByName(String name) {
        List<BishClient> clients = bishClientRepo.findAllBySurnameContainingIgnoringCase(name);
        clients.addAll(bishClientRepo.findAllByNameContainingIgnoringCase(name));
        return clients;
    }

    @Override
    public List<BishClient> getWithPredicate(List<Long> status, List<Long> course, List<Long> occupation) {
        GenericSpecification genericSpecification = new GenericSpecification<BishClient>();
        if (status != null)
            genericSpecification.add(new SearchCriteria("status", status, SearchOperation.EQUAL));
        if (course != null)
            genericSpecification.add(new SearchCriteria("course", course, SearchOperation.EQUAL));
        if (occupation != null)
            genericSpecification.add(new SearchCriteria("occupation", occupation, SearchOperation.EQUAL));
        return bishClientRepo.findAll(genericSpecification);
    }
/*
    @Override
    public List<BishClient> getWithPredicate(LocalDateTime dateAfter, LocalDateTime dateBefore, Long status, Long course, String occupation, String utm) {
        GenericSpecification genericSpecification = new GenericSpecification<BishClient>();
        if (dateAfter != null)
            genericSpecification.add(new SearchCriteria("dateCreated", null, dateAfter, SearchOperation.GREATER_THAN_EQUAL));
        if (dateBefore != null)
            genericSpecification.add(new SearchCriteria("dateCreated", null, dateBefore, SearchOperation.LESS_THAN_EQUAL));
        if (status != null)
            genericSpecification.add(new SearchCriteria("status", "id", status, SearchOperation.EQUAL));
        if (course != null)
            genericSpecification.add(new SearchCriteria("course", "id", course, SearchOperation.EQUAL));
        if (occupation != null)
            genericSpecification.add(new SearchCriteria("occupation", null, occupation, SearchOperation.MATCH));
        if (utm != null)
            genericSpecification.add(new SearchCriteria("utm", null, utm, SearchOperation.MATCH));
//        List<BishClient> clients = clientRepo.findAll((root, criteriaQuery, criteriaBuilder) ->
//            criteriaBuilder.and(
//                    criteriaBuilder.greaterThanOrEqualTo(root.get("dateCreated"), dateAfter),
//                    criteriaBuilder.lessThanOrEqualTo(root.get("dateCreated"), dateBefore)
//            )
//        );
        return clientRepo.findAll(genericSpecification);
//        return clients;
    }
*/
    @Override
    public BishClient create(ClientDTO clientDTO, String userEmail) {
        BishClient client = new BishClient();
        client.setDateCreated(LocalDateTime.now());
        client.setPhoneNo(clientDTO.getPhoneNo());
        client.setName(clientDTO.getName());
        client.setSurname(clientDTO.getSurname());
        client.setEmail(clientDTO.getEmail());
        client.setTarget(clientDTO.getTarget());
        client.setExperience(clientDTO.isExperience());
        client.setLaptop(clientDTO.isLaptop());
        client.setDescription(clientDTO.getDescription());
        client.setCity("BISHKEK");
        client.setPrepayment(clientDTO.getPrepayment());
        client.setLeavingReason(clientDTO.getLeavingReason());


        BishHistory historyStatus = null, historyOccupation = null, historyCourse = null, historyUTM = null;
        if (clientDTO.getStatus() != 0) {
            BishStatuses statuses = bishStatusesRepo.findById(clientDTO.getStatus())
                    .orElseThrow(() -> new ResourceNotFoundException("Status with id " + clientDTO.getStatus() + " has not found"));
            client.setStatus(statuses);
            historyStatus = new BishHistory();
            historyStatus.setUserEmail(userEmail);
            historyStatus.setClientPhone(client.getPhoneNo());
            historyStatus.setAction("status");
            historyStatus.setNewData(statuses.getName());
        }
        if (clientDTO.getOccupation() != 0) {
            BishOccupation occupation = bishOccupationRepo.findById(clientDTO.getOccupation())
                    .orElseThrow(() -> new ResourceNotFoundException("Occupation with id " + clientDTO.getOccupation() + " has not found"));
            client.setOccupation(occupation);
            historyOccupation = new BishHistory();
            historyOccupation.setUserEmail(userEmail);
            historyOccupation.setClientPhone(client.getPhoneNo());
            historyOccupation.setAction("occupation");
            historyOccupation.setNewData(occupation.getName());
        }
        if (clientDTO.getCourse() != 0) {
            BishCourses courses = coursesService.findCourseById(clientDTO.getCourse());
            if (courses == null)
                throw new ResourceNotFoundException("Course with id " + clientDTO.getCourse() + " has not found");
            client.setCourse(courses);
            historyCourse = new BishHistory();
            historyCourse.setUserEmail(userEmail);
            historyCourse.setClientPhone(client.getPhoneNo());
            historyCourse.setAction("course");
            historyCourse.setNewData(courses.getName());
        }
        if (clientDTO.getUTM() != 0) {
            BishUTM utm = bishUTMRepo.findById(clientDTO.getUTM())
                    .orElseThrow(() -> new ResourceNotFoundException("UTM with id " + clientDTO.getUTM() + " has not found"));
            client.setUtm(utm);
            historyUTM = new BishHistory();
            historyUTM.setUserEmail(userEmail);
            historyUTM.setClientPhone(client.getPhoneNo());
            historyUTM.setAction("UTM");
            historyUTM.setNewData(utm.getName());
        }

        if (clientDTO.getTimer() == null)
            client.setTimer(LocalDateTime.now().plusHours(24L));
        else
            client.setTimer(clientDTO.getTimer());

        client = bishClientRepo.save(client);
        if (historyCourse != null) bishHistoryService.create(historyCourse);
        if (historyOccupation != null) bishHistoryService.create(historyOccupation);
        if (historyUTM != null) bishHistoryService.create(historyUTM);
        if (historyStatus != null) bishHistoryService.create(historyStatus);
        return client;
    }

    @Override
    public BishClient getClientByName(String name) {
        return bishClientRepo.findByNameContainingIgnoringCase(name);
    }

    @Override
    public BishClient getClientById(long id) {
        return bishClientRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " has not found"));
    }

    @Override
    public BishClient changeStatus(long id, long status, String username) {
        BishClient client = bishClientRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " has not found"));

        BishHistory bishHistory = new BishHistory();
        bishHistory.setAction("status");
        if (client.getStatus() == null)
            bishHistory.setOldData(null);
        else
            bishHistory.setOldData(client.getStatus().getName());
        bishHistory.setUserEmail(username);
        bishHistory.setClientPhone(client.getPhoneNo());

        BishStatuses statuses = bishStatusesRepo.findById(status)
                .orElseThrow(() -> new ResourceNotFoundException("Status with id " + id + " has not found"));
        bishHistory.setNewData(statuses.getName());

        client.setStatus(statuses);

        if (client.getTimer() == null) {
            client.setTimer(LocalDateTime.now().plusHours(24L));
        } else if (client.getTimer().isAfter(LocalDateTime.now().plusHours(24L))){
            client.setTimer(LocalDateTime.now().plusHours(24L));
        }

        client = bishClientRepo.save(client);

        bishHistoryService.create(bishHistory);
        return client;
    }

    @Override
    public BishClient updateClient(long id, ClientDTO clientDTO, String username) {
        BishClient client = bishClientRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " has not found"));

        BishHistory historyStatus = null, historyOccupation = null, historyCourse = null, historyUTM = null;
        if (clientDTO.getStatus() != 0) {
            BishStatuses statuses = bishStatusesRepo.findById(clientDTO.getStatus())
                    .orElseThrow(() -> new ResourceNotFoundException("Status with id " + clientDTO.getStatus() + " has not found"));

            historyStatus = new BishHistory();
            historyStatus.setUserEmail(username);
            historyStatus.setClientPhone(client.getPhoneNo());
            historyStatus.setAction("status");
            BishStatuses oldStatus = client.getStatus();
            if (oldStatus != null)
                historyStatus.setOldData(client.getStatus().getName());
            historyStatus.setNewData(statuses.getName());
            client.setStatus(statuses);
        }
        if (clientDTO.getOccupation() != 0) {
            BishOccupation occupation = bishOccupationRepo.findById(clientDTO.getOccupation())
                    .orElseThrow(() -> new ResourceNotFoundException("Occupation with id " + clientDTO.getOccupation() + " has not found"));
            historyOccupation = new BishHistory();
            historyOccupation.setUserEmail(username);
            historyOccupation.setClientPhone(client.getPhoneNo());
            historyOccupation.setAction("occupation");
            BishOccupation oldOccupation = client.getOccupation();
            if (oldOccupation != null)
                historyOccupation.setOldData(client.getOccupation().getName());
            historyOccupation.setNewData(occupation.getName());
            client.setOccupation(occupation);
        }
        if (clientDTO.getCourse() != 0) {
            BishCourses courses = coursesService.findCourseById(clientDTO.getCourse());
            if (courses == null)
                throw new ResourceNotFoundException("Course with id " + clientDTO.getCourse() + " has not found");
            historyCourse = new BishHistory();
            historyCourse.setUserEmail(username);
            historyCourse.setClientPhone(client.getPhoneNo());
            historyCourse.setAction("course");
            BishCourses oldCourse = client.getCourse();
            if (oldCourse != null)
                historyCourse.setOldData(client.getCourse().getName());
            historyCourse.setNewData(courses.getName());
            client.setCourse(courses);
        }
        if (clientDTO.getUTM() != 0) {
            BishUTM utm = bishUTMRepo.findById(clientDTO.getUTM())
                    .orElseThrow(() -> new ResourceNotFoundException("UTM with id " + clientDTO.getUTM() + " has not found"));
            historyUTM = new BishHistory();
            historyUTM.setUserEmail(username);
            historyUTM.setClientPhone(client.getPhoneNo());
            historyUTM.setAction("UTM");
            BishUTM oldUTM = client.getUtm();
            if (oldUTM != null)
                historyUTM.setNewData(utm.getName());
            client.setUtm(utm);
        }

        client.setPhoneNo(clientDTO.getPhoneNo());
        client.setName(clientDTO.getName());
        client.setSurname(clientDTO.getSurname());
        client.setEmail(clientDTO.getEmail());
	    client.setTarget(clientDTO.getTarget());
        client.setExperience(clientDTO.isExperience());
        client.setLaptop(clientDTO.isLaptop());
	    client.setDescription(clientDTO.getDescription());
        client.setCity("BISHKEK");
        client.setPrepayment(clientDTO.getPrepayment());
        client.setLeavingReason(clientDTO.getLeavingReason());

        if (client.getTimer() == null) {
            if (clientDTO.getTimer() == null)
                client.setTimer(LocalDateTime.now().plusHours(24L));
            else
                client.setTimer(clientDTO.getTimer());
        } else if (!client.getTimer().isAfter(LocalDateTime.now().plusHours(24L))){
            client.setTimer(clientDTO.getTimer());
        }
        client = bishClientRepo.save(client);
        if (historyCourse != null && !historyCourse.getOldData().equals(historyCourse.getNewData()))
            bishHistoryService.create(historyCourse);
        if (historyOccupation != null && !historyOccupation.getOldData().equals(historyOccupation.getNewData()))
            bishHistoryService.create(historyOccupation);
        if (historyUTM != null && !historyUTM.getOldData().equals(historyUTM.getNewData()))
            bishHistoryService.create(historyUTM);
        if (historyStatus != null && !historyStatus.getOldData().equals(historyStatus.getNewData()))
            bishHistoryService.create(historyStatus);
        return client;
    }

    @Override
    public void changeCity(long id, String userEmail) {
        BishClient bishClient = bishClientRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " has not found"));

//        First step - create oshClient
        OshClient oshClient = new OshClient();
        oshClient.setDateCreated(bishClient.getDateCreated());
        oshClient.setPhoneNo(bishClient.getPhoneNo());
        oshClient.setName(bishClient.getName());
        oshClient.setSurname(bishClient.getSurname());
        oshClient.setEmail(bishClient.getEmail());
        if (bishClient.getStatus() != null)
            oshClient.setStatus(oshStatusesRepo.findByNameContainingIgnoringCase(bishClient.getStatus().getName()));
        if (bishClient.getOccupation() != null)
            oshClient.setOccupation(oshOccupationRepo.findByNameContainingIgnoringCase(bishClient.getOccupation().getName()).orElse(null));
        oshClient.setTarget(bishClient.getTarget());
        oshClient.setExperience(bishClient.isExperience());
        oshClient.setLaptop(bishClient.isLaptop());
        if (bishClient.getUtm() != null)
            oshClient.setUtm(oshUTMRepo.findByNameContainingIgnoringCase(bishClient.getUtm().getName()).orElse(null));
        oshClient.setDescription(bishClient.getDescription());
        oshClient.setCity("OSH");
        oshClient.setFormName(bishClient.getFormName());
//         TODO
        oshClient.setTimer(bishClient.getTimer());
        oshClient.setPrepayment(bishClient.getPrepayment());
        oshClient.setLeavingReason(bishClient.getLeavingReason());
        oshClientRepo.save(oshClient);

//        Second step - create all payments of bishClient
        List<BishPayment> bishPayments =  bishPaymentService.getAllByClientID(id);
        List<OshPayment> oshPayments = new ArrayList<>();
        for (BishPayment bishPayment : bishPayments) {
            OshPayment oshPayment = new OshPayment();
            oshPayment.setMonth(bishPayment.getMonth());
            oshPayment.setPrice(bishPayment.getPrice());
            oshPayment.setDone(bishPayment.isDone());
            oshPayment.setMethod(bishPayment.getMethod());
            oshPayment.setClient(oshClient);
            oshPayments.add(oshPaymentService.save(oshPayment));

//            Third step is inside of second step - delete payments of bishClient
            bishPaymentService.delete(bishPayment);
        }

//        Fourth step - delete bishClient
        bishClientRepo.delete(bishClient);

        BishHistory history = new BishHistory();
        history.setUserEmail(userEmail);
        history.setAction("change city of client");
        history.setClientPhone(oshClient.getPhoneNo());
    }

    @Override
    public List<BishClient> search(String nameOrPhone) {
        List<BishClient> clients = new ArrayList<>();
        for (String item : nameOrPhone.split(" ")) {
            clients.addAll(bishClientRepo.findAllByNameContainingIgnoringCase(item));
            clients.addAll(bishClientRepo.findAllBySurnameContainingIgnoringCase(item));
        }
        clients.addAll(bishClientRepo.findAllByPhoneNoContaining(nameOrPhone));
        return clients;
    }
}
