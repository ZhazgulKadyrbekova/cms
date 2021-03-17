package neobis.cms.Service.Osh;

import neobis.cms.Dto.ClientDTO;
import neobis.cms.Entity.Bishkek.*;
import neobis.cms.Entity.Osh.*;
import neobis.cms.Exception.ResourceNotFoundException;
import neobis.cms.Repo.Bishkek.*;
import neobis.cms.Repo.Osh.*;
import neobis.cms.Search.GenericSpecification;
import neobis.cms.Search.SearchCriteria;
import neobis.cms.Search.SearchOperation;
import neobis.cms.Service.Bishkek.BishPaymentService;
import neobis.cms.Service.Bishkek.UserService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    private final BishClientRepo bishClientRepo;
    private final OshClientRepo oshClientRepo;

    private final BishStatusesRepo bishStatusesRepo;
    private final OshStatusesRepo oshStatusesRepo;

    private final BishOccupationRepo bishOccupationRepo;
    private final OshOccupationRepo oshOccupationRepo;

    private final BishUTMRepo bishUTMRepo;
    private final OshUTMRepo oshUTMRepo;

    private final OshCoursesService coursesService;

    private BishPaymentService bishPaymentService;
    private OshPaymentService oshPaymentService;

    private final OshHistoryService historyService;

    private final UserService userService;

    private final BishLeavingReasonRepo bishLeavingReasonRepo;
    private final OshLeavingReasonRepo oshLeavingReasonRepo;

    private final String dataResourceUrl
            = "https://neolabs.dev/mod/api/?api_key=e539509b630b27e47ac594d0dbba4e69&method=getLeads";

    public OshClientServiceImpl(BishClientRepo bishClientRepo, OshClientRepo oshClientRepo, BishStatusesRepo bishStatusesRepo,
                                OshStatusesRepo oshStatusesRepo, BishOccupationRepo bishOccupationRepo, OshOccupationRepo oshOccupationRepo,
                                BishUTMRepo bishUTMRepo, OshUTMRepo oshUTMRepo, OshCoursesService coursesService, @Lazy BishPaymentService bishPaymentService,
                                @Lazy OshPaymentService oshPaymentService, OshHistoryService historyService, UserService userService,
                                BishLeavingReasonRepo bishLeavingReasonRepo, OshLeavingReasonRepo oshLeavingReasonRepo) {
        this.bishClientRepo = bishClientRepo;
        this.oshClientRepo = oshClientRepo;
        this.bishStatusesRepo = bishStatusesRepo;
        this.oshStatusesRepo = oshStatusesRepo;
        this.bishPaymentService = bishPaymentService;
        this.oshPaymentService = oshPaymentService;
        this.bishOccupationRepo = bishOccupationRepo;
        this.oshOccupationRepo = oshOccupationRepo;
        this.bishUTMRepo = bishUTMRepo;
        this.oshUTMRepo = oshUTMRepo;
        this.coursesService = coursesService;
        this.historyService = historyService;
        this.userService = userService;
        this.bishLeavingReasonRepo = bishLeavingReasonRepo;
        this.oshLeavingReasonRepo = oshLeavingReasonRepo;
    }

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
        client.setCity("Osh");
        return oshClientRepo.save(client);
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
                                if (!data.getString(key).equals("neolabs.dev/")) {
                                    String utmName = data.getString(key);
                                    OshUTM utm;
                                    if (utmName.contains("instagram")) {
                                        utm = oshUTMRepo.findByNameContainingIgnoringCase("instagram").orElse(null);
                                        if (utm == null) {
                                            bishUTMRepo.save(new BishUTM(0, "instagram"));
                                            utm = oshUTMRepo.save(new OshUTM(0, "instagram"));
                                        }
                                        client.setUtm(utm);
                                    }
                                    if (utmName.contains("facebook")) {
                                        utm = oshUTMRepo.findByNameContainingIgnoringCase("facebook").orElse(null);
                                        if (utm == null) {
                                            bishUTMRepo.save(new BishUTM(0, "facebook"));
                                            utm = oshUTMRepo.save(new OshUTM(0, "facebook"));
                                        }
                                        client.setUtm(utm);
                                    }
                                    if (utmName.contains("google")) {
                                        utm = oshUTMRepo.findByNameContainingIgnoringCase("google").orElse(null);
                                        if (utm == null) {
                                            bishUTMRepo.save(new BishUTM(0, "google"));
                                            utm = oshUTMRepo.save(new OshUTM(0, "google"));
                                        }
                                        client.setUtm(utm);
                                    }
                                    if (utmName.contains("neobis")) {
                                        utm = oshUTMRepo.findByNameContainingIgnoringCase("neobis").orElse(null);
                                        if (utm == null) {
                                            bishUTMRepo.save(new BishUTM(0, "neobis"));
                                            utm = oshUTMRepo.save(new OshUTM(0, "neobis"));
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
                                    OshOccupation oshOccupation = oshOccupationRepo.findByNameContainingIgnoringCase(occupationName).orElse(null);
                                    if (oshOccupation == null) {
                                        bishOccupationRepo.save(new BishOccupation(0, occupationName));
                                        oshOccupation = oshOccupationRepo.save(new OshOccupation(0, occupationName));
                                    }
                                    client.setOccupation(oshOccupation);
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
    public Page<OshClient> getAllClientsFromDB(Pageable pageable) {
        List<OshClient> clients = oshClientRepo.findAllByOrderByDateCreatedDesc();
        final int start = (int) pageable.getOffset();
        final  int end = Math.min((start + pageable.getPageSize()), clients.size());
        final Page<OshClient> page = new PageImpl<>(clients.subList(start, end), pageable, clients.size());

        return page;
    }

    @Override
    public void addClientsToDB() {
        LocalDateTime dateTime = this.getDateOfLastClient(oshClientRepo.findAllByOrderByDateCreatedDesc());
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
    public List<OshClient> getAllByStatus(long status) {
        OshStatuses oshStatuses = oshStatusesRepo.findById(status)
                .orElseThrow(() -> new ResourceNotFoundException("Status with id " + status + " has not found"));
        return oshClientRepo.findAllByStatusOrderByDateCreatedDesc(oshStatuses);
    }

    @Override
    public List<OshClient> getAllByName(String name) {
        List<OshClient> clients = oshClientRepo.findAllBySurnameContainingIgnoringCase(name);
        clients.addAll(oshClientRepo.findAllByNameContainingIgnoringCase(name));
        return clients;
    }

    @Override
    public Page<OshClient> getWithPredicate(Pageable pageable, List<Long> status, List<Long> course, List<Long> occupation) {
        GenericSpecification genericSpecification = new GenericSpecification<OshClient>();
        if (status != null)
            genericSpecification.add(new SearchCriteria("status", status, SearchOperation.EQUAL));
        if (course != null)
            genericSpecification.add(new SearchCriteria("course", course, SearchOperation.EQUAL));
        if (occupation != null)
            genericSpecification.add(new SearchCriteria("occupation", occupation, SearchOperation.EQUAL));
        List<OshClient> clients = oshClientRepo.findAll(genericSpecification);

        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), clients.size());
        final Page<OshClient> page = new PageImpl<>(clients.subList(start, end), pageable, clients.size());

        return page;
    }

    @Override
    public OshClient create(ClientDTO clientDTO, String userEmail) {
        OshClient client = new OshClient();
        client.setDateCreated(LocalDateTime.now());
        client.setPhoneNo(clientDTO.getPhoneNo());
        client.setName(clientDTO.getName());
        client.setSurname(clientDTO.getSurname());
        client.setEmail(clientDTO.getEmail());
        client.setTarget(clientDTO.getTarget());
        client.setExperience(clientDTO.isExperience());
        client.setLaptop(clientDTO.isLaptop());
        client.setDescription(clientDTO.getDescription());
        client.setCity("OSH");
        client.setPrepayment(clientDTO.getPrepayment());
        if (clientDTO.getLeavingReason() != 0)
            client.setLeavingReason(oshLeavingReasonRepo.findById(clientDTO.getLeavingReason())
                    .orElseThrow(() -> new ResourceNotFoundException("Reason with id " + clientDTO.getLeavingReason() + "has not found")));

        OshHistory historyStatus = null, historyOccupation = null, historyCourse = null, historyUTM = null;
        if (clientDTO.getStatus() != 0) {
            OshStatuses statuses = oshStatusesRepo.findById(clientDTO.getStatus())
                    .orElseThrow(() -> new ResourceNotFoundException("Status with id " + clientDTO.getStatus() + " has not found"));
            client.setStatus(statuses);
            historyStatus = new OshHistory();
            historyStatus.setUserEmail(userEmail);
            historyStatus.setClientPhone(client.getPhoneNo());
            historyStatus.setAction("status");
            historyStatus.setNewData(statuses.getName());
        }
        if (clientDTO.getOccupation() != 0) {
            OshOccupation occupation = oshOccupationRepo.findById(clientDTO.getOccupation())
                    .orElseThrow(() -> new ResourceNotFoundException("Occupation with id " + clientDTO.getOccupation() + " has not found"));
            client.setOccupation(occupation);
            historyOccupation = new OshHistory();
            historyOccupation.setUserEmail(userEmail);
            historyOccupation.setClientPhone(client.getPhoneNo());
            historyOccupation.setAction("occupation");
            historyOccupation.setNewData(occupation.getName());
        }
        if (clientDTO.getCourse() != 0) {
            OshCourses courses = coursesService.findCourseById(clientDTO.getCourse());
            if (courses == null)
                throw new ResourceNotFoundException("Course with id " + clientDTO.getCourse() + " has not found");
            client.setCourse(courses);
            historyCourse = new OshHistory();
            historyCourse.setUserEmail(userEmail);
            historyCourse.setClientPhone(client.getPhoneNo());
            historyCourse.setAction("course");
            historyCourse.setNewData(courses.getName());
        }
        if (clientDTO.getUTM() != 0) {
            OshUTM utm = oshUTMRepo.findById(clientDTO.getUTM())
                    .orElseThrow(() -> new ResourceNotFoundException("UTM with id " + clientDTO.getUTM() + " has not found"));
            client.setUtm(utm);
            historyUTM = new OshHistory();
            historyUTM.setUserEmail(userEmail);
            historyUTM.setClientPhone(client.getPhoneNo());
            historyUTM.setAction("UTM");
            historyUTM.setNewData(utm.getName());
        }

        if (clientDTO.getTimer() == null)
            client.setTimer(LocalDateTime.now().plusHours(24L));
        else
            client.setTimer(clientDTO.getTimer());

        client = oshClientRepo.save(client);
        if (historyCourse != null) historyService.create(historyCourse);
        if (historyOccupation != null) historyService.create(historyOccupation);
        if (historyUTM != null) historyService.create(historyUTM);
        if (historyStatus != null) historyService.create(historyStatus);
        return client;
    }

    @Override
    public OshClient getClientByName(String name) {
        return oshClientRepo.findByNameContainingIgnoringCase(name);
    }

    @Override
    public OshClient getClientByID(long id) {
        return oshClientRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " has not found"));
    }

    @Override
    public OshClient changeStatus(long id, long status, String username) {
        OshClient client = oshClientRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " has not found"));
        OshHistory oshHistory = new OshHistory();
        oshHistory.setAction("status");
        if (client.getStatus() == null)
            oshHistory.setOldData(null);
        else
            oshHistory.setOldData(client.getStatus().getName());
        oshHistory.setUserEmail(username);
        oshHistory.setClientPhone(client.getPhoneNo());

        OshStatuses statuses = oshStatusesRepo.findById(status)
                .orElseThrow(() -> new ResourceNotFoundException("Status with id " + id + " has not found"));
        oshHistory.setNewData(statuses.getName());

        client.setStatus(statuses);
        if (client.getTimer() == null) {
            client.setTimer(LocalDateTime.now().plusHours(24L));
        } else if (client.getTimer().isAfter(LocalDateTime.now().plusHours(24L))){
            client.setTimer(LocalDateTime.now().plusHours(24L));
        }

        client = oshClientRepo.save(client);

        historyService.create(oshHistory);
        return client;
    }

    @Override
    public OshClient updateClient(long id, ClientDTO clientDTO, String username) {
        OshClient client = oshClientRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " has not found"));
        OshHistory historyStatus = null, historyOccupation = null, historyCourse = null, historyUTM = null;
        if (clientDTO.getStatus() != 0) {
            OshStatuses statuses = oshStatusesRepo.findById(clientDTO.getStatus())
                    .orElseThrow(() -> new ResourceNotFoundException("Status with id " + clientDTO.getStatus() + " has not found"));
            historyStatus = new OshHistory();
            historyStatus.setUserEmail(username);
            historyStatus.setClientPhone(client.getPhoneNo());
            historyStatus.setAction("status");
            OshStatuses oldStatus = client.getStatus();
            if (oldStatus != null)
                historyStatus.setOldData(client.getStatus().getName());
            historyStatus.setNewData(statuses.getName());
            client.setStatus(statuses);
        }
        if (clientDTO.getOccupation() != 0) {
            OshOccupation occupation = oshOccupationRepo.findById(clientDTO.getOccupation())
                    .orElseThrow(() -> new ResourceNotFoundException("Occupation with id " + clientDTO.getOccupation() + " has not found"));
            historyOccupation = new OshHistory();
            historyOccupation.setUserEmail(username);
            historyOccupation.setClientPhone(client.getPhoneNo());
            historyOccupation.setAction("occupation");
            OshOccupation oldOccupation = client.getOccupation();
            if (oldOccupation != null)
                historyOccupation.setOldData(client.getOccupation().getName());
            historyOccupation.setNewData(occupation.getName());
            client.setOccupation(occupation);
        }
        if (clientDTO.getCourse() != 0) {
            OshCourses courses = coursesService.findCourseById(clientDTO.getCourse());
            if (courses == null)
                throw new ResourceNotFoundException("Course with id " + clientDTO.getCourse() + " has not found");
            historyCourse = new OshHistory();
            historyCourse.setUserEmail(username);
            historyCourse.setClientPhone(client.getPhoneNo());
            historyCourse.setAction("course");
            OshCourses oldCourse = client.getCourse();
            if (oldCourse != null)
                historyCourse.setOldData(client.getCourse().getName());
            historyCourse.setNewData(courses.getName());
            client.setCourse(courses);
        }
        if (clientDTO.getUTM() != 0) {
            OshUTM utm = oshUTMRepo.findById(clientDTO.getUTM())
                    .orElseThrow(() -> new ResourceNotFoundException("UTM with id " + clientDTO.getUTM() + " has not found"));
            historyUTM = new OshHistory();
            historyUTM.setUserEmail(username);
            historyUTM.setClientPhone(client.getPhoneNo());
            historyUTM.setAction("UTM");
            OshUTM oldUTM = client.getUtm();
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
        client.setCity("OSH");
        client.setPrepayment(clientDTO.getPrepayment());
        if (clientDTO.getLeavingReason() != 0)
            client.setLeavingReason(oshLeavingReasonRepo.findById(clientDTO.getLeavingReason())
                    .orElseThrow((() -> new ResourceNotFoundException("Reason with id " + clientDTO.getLeavingReason() + "has not found"))));

        if (client.getTimer() == null) {
            if (clientDTO.getTimer() == null)
                client.setTimer(LocalDateTime.now().plusHours(24L));
            else
                client.setTimer(clientDTO.getTimer());
        } else if (!client.getTimer().isAfter(LocalDateTime.now().plusHours(24L))){
            client.setTimer(clientDTO.getTimer());
        }
        client = oshClientRepo.save(client);
        if (historyCourse != null && !historyCourse.getOldData().equals(historyCourse.getNewData()))
            historyService.create(historyCourse);
        if (historyOccupation != null && !historyOccupation.getOldData().equals(historyOccupation.getNewData()))
            historyService.create(historyOccupation);
        if (historyUTM != null && !historyUTM.getOldData().equals(historyUTM.getNewData()))
            historyService.create(historyUTM);
        if (historyStatus != null && !historyStatus.getOldData().equals(historyStatus.getNewData()))
            historyService.create(historyStatus);
        return client;
    }

    @Override
    public void changeCity(long id, String userEmail) {
        OshClient oshClient = oshClientRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " has not found"));

//        First step - create bishClient
        BishClient bishClient = new BishClient();
        bishClient.setDateCreated(oshClient.getDateCreated());
        bishClient.setPhoneNo(oshClient.getPhoneNo());
        bishClient.setName(oshClient.getName());
        bishClient.setSurname(oshClient.getSurname());
        bishClient.setEmail(oshClient.getEmail());
        if (oshClient.getStatus() != null)
            bishClient.setStatus(bishStatusesRepo.findByNameContainingIgnoringCase(oshClient.getStatus().getName()));
        if (oshClient.getOccupation() != null)
            bishClient.setOccupation(bishOccupationRepo.findByNameContainingIgnoringCase(oshClient.getOccupation().getName()).orElse(null));
        bishClient.setTarget(oshClient.getTarget());
        bishClient.setExperience(oshClient.isExperience());
        bishClient.setLaptop(oshClient.isLaptop());
        if (oshClient.getUtm() != null)
            bishClient.setUtm(bishUTMRepo.findByNameContainingIgnoringCase(oshClient.getUtm().getName()).orElse(null));
        bishClient.setDescription(oshClient.getDescription());
        bishClient.setCity("OSH");
        bishClient.setFormName(oshClient.getFormName());
//         TODO
        bishClient.setTimer(oshClient.getTimer());
        bishClient.setPrepayment(oshClient.getPrepayment());
        bishClient.setLeavingReason(bishLeavingReasonRepo.findByNameContainingIgnoringCase(bishClient.getLeavingReason().getName()).orElse(null));
        bishClientRepo.save(bishClient);

//        Second step - create all payments of oshClient
        List<OshPayment> oshPayments =  oshPaymentService.getAllByClientID(id);
        List<BishPayment> bishPayments = new ArrayList<>();
        for (OshPayment oshPayment : oshPayments) {
            BishPayment bishPayment = new BishPayment();
            bishPayment.setMonth(oshPayment.getMonth());
            bishPayment.setPrice(oshPayment.getPrice());
            bishPayment.setDone(oshPayment.isDone());
            bishPayment.setMethod(oshPayment.getMethod());
            bishPayment.setClient(bishClient);
            bishPayments.add(bishPaymentService.save(bishPayment));

//            Third step is inside of second step - delete payments of oshClient
            oshPaymentService.delete(oshPayment);
        }

//        Fourth step - delete oshClient
        oshClientRepo.delete(oshClient);

        OshHistory history = new OshHistory();
        history.setUserEmail(userEmail);
        history.setAction("change city of client");
        history.setClientPhone(bishClient.getPhoneNo());
        history.setOldData("OSH");
        history.setNewData("BISHKEK");
    }

    @Override
    public Page<OshClient> search(Pageable pageable, String nameOrPhone) {
        List<OshClient> clients = new ArrayList<>();
        for (String item : nameOrPhone.split(" ")) {
            clients.addAll(oshClientRepo.findAllByNameContainingIgnoringCase(item));
            clients.addAll(oshClientRepo.findAllBySurnameContainingIgnoringCase(item));
        }
        clients.addAll(oshClientRepo.findAllByPhoneNoContaining(nameOrPhone));

        final int start = (int) pageable.getOffset();
        final  int end = Math.min((start + pageable.getPageSize()), clients.size());
        final Page<OshClient> page = new PageImpl<>(clients.subList(start, end), pageable, clients.size());

        return page;
    }
}
