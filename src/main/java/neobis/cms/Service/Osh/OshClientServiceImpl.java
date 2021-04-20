package neobis.cms.Service.Osh;

import neobis.cms.Dto.ClientDTO;
import neobis.cms.Dto.PaymentDTO;
import neobis.cms.Dto.ResponseMessage;
import neobis.cms.Entity.Bishkek.*;
import neobis.cms.Entity.Osh.*;
import neobis.cms.Exception.ResourceNotFoundException;
import neobis.cms.Repo.Bishkek.*;
import neobis.cms.Repo.Osh.*;
import neobis.cms.Search.OshClientSpecification;
import neobis.cms.Search.SearchCriteria;
import neobis.cms.Search.SearchOperation;
import neobis.cms.Service.Bishkek.UserService;
import org.json.JSONArray;
import org.json.JSONObject;
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
import java.util.*;

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

    private final BishPaymentRepo bishPaymentRepo;
    private final OshPaymentRepo oshPaymentRepo;

    private final OshHistoryService historyService;

    private final UserService userService;

    private final BishLeavingReasonRepo bishLeavingReasonRepo;
    private final OshLeavingReasonRepo oshLeavingReasonRepo;

    private final BishTargetRepo bishTargetRepo;
    private final OshTargetRepo oshTargetRepo;

    private final BishMethodRepo bishMethodRepo;
    private final OshMethodRepo oshMethodRepo;
    public OshClientServiceImpl(BishClientRepo bishClientRepo, OshClientRepo oshClientRepo, BishStatusesRepo
                                bishStatusesRepo, OshStatusesRepo oshStatusesRepo, BishOccupationRepo bishOccupationRepo,
                                OshOccupationRepo oshOccupationRepo, BishUTMRepo bishUTMRepo, OshUTMRepo oshUTMRepo,
                                OshCoursesService coursesService, BishPaymentRepo bishPaymentService, OshPaymentRepo
                                oshPaymentService, OshHistoryService historyService, UserService userService,
                                BishLeavingReasonRepo bishLeavingReasonRepo, OshLeavingReasonRepo oshLeavingReasonRepo,
                                BishTargetRepo bishTargetRepo, OshTargetRepo oshTargetRepo, BishMethodRepo
                                bishMethodRepo, OshMethodRepo oshMethodRepo) {
        this.bishClientRepo = bishClientRepo;
        this.oshClientRepo = oshClientRepo;
        this.bishStatusesRepo = bishStatusesRepo;
        this.oshStatusesRepo = oshStatusesRepo;
        this.bishPaymentRepo = bishPaymentService;
        this.oshPaymentRepo = oshPaymentService;
        this.bishOccupationRepo = bishOccupationRepo;
        this.oshOccupationRepo = oshOccupationRepo;
        this.bishUTMRepo = bishUTMRepo;
        this.oshUTMRepo = oshUTMRepo;
        this.coursesService = coursesService;
        this.historyService = historyService;
        this.userService = userService;
        this.bishLeavingReasonRepo = bishLeavingReasonRepo;
        this.oshLeavingReasonRepo = oshLeavingReasonRepo;
        this.bishTargetRepo = bishTargetRepo;
        this.oshTargetRepo = oshTargetRepo;
        this.bishMethodRepo = bishMethodRepo;
        this.oshMethodRepo = oshMethodRepo;
    }

    @Override
    public String getNewClients() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, MediaType.APPLICATION_OCTET_STREAM));
        String dataResourceUrl = "https://neolabs.dev/mod/api/?api_key=e539509b630b27e47ac594d0dbba4e69&method=getLeads&start=0&count=150";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(dataResourceUrl);
        HttpEntity<String> httpEntity = new HttpEntity<>("", httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, httpEntity, String.class);
        return response.getBody();
    }
    
    @Override
    public OshClient create(OshClient client) {
//        client.setStatus("New");
        client.setCity("Osh");
        client.setDateCreated(LocalDateTime.now());
        client.setTimer(LocalDateTime.now().plusHours(24L));
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
                        boolean hasTarget;
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
                                        utm = oshUTMRepo.findByNameContainingIgnoringCase("Instagram").orElse(null);
                                        if (utm == null) {
                                            bishUTMRepo.save(new BishUTM("Instagram"));
                                            utm = oshUTMRepo.save(new OshUTM("Instagram"));
                                        }
                                        client.setUtm(utm);
                                    }
                                    if (utmName.contains("facebook")) {
                                        utm = oshUTMRepo.findByNameContainingIgnoringCase("Facebook").orElse(null);
                                        if (utm == null) {
                                            bishUTMRepo.save(new BishUTM("Facebook"));
                                            utm = oshUTMRepo.save(new OshUTM("Facebook"));
                                        }
                                        client.setUtm(utm);
                                    }
                                    if (utmName.contains("google")) {
                                        utm = oshUTMRepo.findByNameContainingIgnoringCase("Google").orElse(null);
                                        if (utm == null) {
                                            bishUTMRepo.save(new BishUTM("Google"));
                                            utm = oshUTMRepo.save(new OshUTM("Google"));
                                        }
                                        client.setUtm(utm);
                                    }
                                    if (utmName.contains("neobis")) {
                                        utm = oshUTMRepo.findByNameContainingIgnoringCase("Neobis").orElse(null);
                                        if (utm == null) {
                                            bishUTMRepo.save(new BishUTM("Neobis"));
                                            utm = oshUTMRepo.save(new OshUTM("Neobis"));
                                        }
                                        client.setUtm(utm);
                                    }

                                }
                                break;
                            case "value" :
                                hasTarget = (data.getString(key).equals("Освоить профессию программиста") ||
                                        data.getString(key).equals("Подготовиться к университету") ||
                                        data.getString(key).equals("Участвовать в олимпиадах") ||
                                        data.getString(key).equals("Повышение квалификации"));
                                occupation = (data.getString(key).equals("Студент") || data.getString(key).equals("Школьник") ||
                                        data.getString(key).equals("Временно безработный") || data.getString(key).equals("Работающий") ||
                                        data.getString(key).equals("Предприниматель"));
                                experience = data.getString(key).equals("Да");
                                if (hasTarget) {
                                    String targetName =data.getString(key);
                                    OshTarget target = oshTargetRepo.findByNameContainingIgnoringCase(targetName);
                                    if (target == null) {
                                        bishTargetRepo.save(new BishTarget(targetName));
                                        target = oshTargetRepo.save(new OshTarget(targetName));
                                    }
                                    client.setTarget(target);
                                }
                                if (occupation) {
                                    String occupationName = data.getString(key);
                                    OshOccupation oshOccupation = oshOccupationRepo.findByNameContainingIgnoringCase(occupationName);
                                    if (oshOccupation == null) {
                                        bishOccupationRepo.save(new BishOccupation(occupationName));
                                        oshOccupation = oshOccupationRepo.save(new OshOccupation(occupationName));
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
        final int end = Math.min((start + pageable.getPageSize()), clients.size());

        return new PageImpl<>(clients.subList(start, end), pageable, clients.size());
    }

    @Override
    public void addClientsToDB() {
        JSONObject data;
        data = new JSONObject(this.getNewClients());
        List<OshClient> clients = this.getClientsFromJson(data);
        for (OshClient client : clients) {
            if (!client.getFormName().equals("Набор в клуб"))
                this.create(client);
        }
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
    public Set<OshClient> getWithPredicate(String field, List<Long> status, List<Long> course, List<Long> occupation, List<Long> utm) {
        OshClientSpecification<OshClient> oshClientSpecification = new OshClientSpecification<>();
        if (status != null)
            oshClientSpecification.add(new SearchCriteria("status", null, status, SearchOperation.EQUAL));
        if (course != null)
            oshClientSpecification.add(new SearchCriteria("course", null, course, SearchOperation.EQUAL));
        if (occupation != null)
            oshClientSpecification.add(new SearchCriteria("occupation", null, occupation, SearchOperation.EQUAL));
        if (utm != null)
            oshClientSpecification.add(new SearchCriteria("utm", null, utm, SearchOperation.EQUAL));
        if (field != null) {
            for (String item : field.split(" ")) {
                oshClientSpecification.add(new SearchCriteria("name", item, null, SearchOperation.MATCH));
                oshClientSpecification.add(new SearchCriteria("surname", item, null, SearchOperation.MATCH));
            }
            oshClientSpecification.add(new SearchCriteria("phoneNo", field, null, SearchOperation.MATCH));
            oshClientSpecification.add(new SearchCriteria("email", field, null, SearchOperation.MATCH));
        }
        return new HashSet<>(oshClientRepo.findAll(oshClientSpecification));
    }

    @Override
    public OshClient create(ClientDTO clientDTO, String userEmail) {
        User user = userService.findByEmail(userEmail);

        OshClient client = new OshClient();
        client.setDateCreated(LocalDateTime.now());
        client.setPhoneNo(clientDTO.getPhoneNo());
        client.setName(clientDTO.getName());
        client.setSurname(clientDTO.getSurname());
        client.setPatronymic(clientDTO.getPatronymic());
        client.setEmail(clientDTO.getEmail());
        client.setExperience(clientDTO.isExperience());
        client.setLaptop(clientDTO.isLaptop());
        client.setDescription(clientDTO.getDescription());
        client.setCity("OSH");
        client.setPrepayment(clientDTO.getPrepayment());
        if (clientDTO.getTarget() != 0)
            client.setTarget(oshTargetRepo.findById(clientDTO.getTarget())
                    .orElseThrow(() -> new ResourceNotFoundException("Target with ID " + clientDTO.getTarget() + " has not found")));
        if (clientDTO.getLeavingReason() != 0)
            client.setLeavingReason(oshLeavingReasonRepo.findById(clientDTO.getLeavingReason())
                    .orElseThrow(() -> new ResourceNotFoundException("Reason with id " + clientDTO.getLeavingReason() + "has not found")));

        OshHistory historyStatus = null, historyOccupation = null, historyCourse = null, historyUTM = null;
        if (clientDTO.getStatus() != 0) {
            OshStatuses statuses = oshStatusesRepo.findById(clientDTO.getStatus())
                    .orElseThrow(() -> new ResourceNotFoundException("Status with id " + clientDTO.getStatus() + " has not found"));
            client.setStatus(statuses);
            historyStatus = new OshHistory();
            historyStatus.setFullName(user.getName() + " " + user.getSurname());
            historyStatus.setClientPhone(client.getPhoneNo());
            historyStatus.setAction("status");
            historyStatus.setNewData(statuses.getName());
        }
        if (clientDTO.getOccupation() != 0) {
            OshOccupation occupation = oshOccupationRepo.findById(clientDTO.getOccupation())
                    .orElseThrow(() -> new ResourceNotFoundException("Occupation with id " + clientDTO.getOccupation() + " has not found"));
            client.setOccupation(occupation);
            historyOccupation = new OshHistory();
            historyOccupation.setFullName(user.getName() + " " + user.getSurname());
            historyOccupation.setClientPhone(client.getPhoneNo());
            historyOccupation.setAction("occupation");
            historyOccupation.setNewData(occupation.getName());
        }
        if (clientDTO.getCourse() != 0) {
            OshCourses course = coursesService.findCourseById(clientDTO.getCourse());
            if (course == null)
                throw new ResourceNotFoundException("Course with id " + clientDTO.getCourse() + " has not found");
            client.setCourse(course);
            historyCourse = new OshHistory();
            historyCourse.setFullName(user.getName() + " " + user.getSurname());
            historyCourse.setClientPhone(client.getPhoneNo());
            historyCourse.setAction("course");
            historyCourse.setNewData(course.getName());
        }
        if (clientDTO.getUTM() != 0) {
            OshUTM utm = oshUTMRepo.findById(clientDTO.getUTM())
                    .orElseThrow(() -> new ResourceNotFoundException("UTM with id " + clientDTO.getUTM() + " has not found"));
            client.setUtm(utm);
            historyUTM = new OshHistory();
            historyUTM.setFullName(user.getName() + " " + user.getSurname());
            historyUTM.setClientPhone(client.getPhoneNo());
            historyUTM.setAction("UTM");
            historyUTM.setNewData(utm.getName());
        }

        if (client.getStatus().getID() == 7)
            client.setTimer(null);
        else if (clientDTO.getTimer() == null)
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
        User user = userService.findByEmail(username);

        OshClient client = oshClientRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " has not found"));

        OshHistory oshHistory = new OshHistory();
        oshHistory.setAction("status");
        if (client.getStatus() == null)
            oshHistory.setOldData(null);
        else
            oshHistory.setOldData(client.getStatus().getName());
        oshHistory.setFullName(user.getName() + " " + user.getSurname());
        oshHistory.setClientPhone(client.getPhoneNo());

        OshStatuses statuses = oshStatusesRepo.findById(status)
                .orElseThrow(() -> new ResourceNotFoundException("Status with id " + id + " has not found"));
        oshHistory.setNewData(statuses.getName());

        client.setStatus(statuses);

        if (client.getStatus().getID() == 7)
            client.setTimer(null);
        else if (client.getTimer() == null)
            client.setTimer(LocalDateTime.now().plusHours(24L));
        else if (!client.getTimer().isAfter(LocalDateTime.now().plusHours(24L))){
            client.setTimer(LocalDateTime.now().plusHours(24L));
        }

        client = oshClientRepo.save(client);

        historyService.create(oshHistory);
        return client;
    }

    @Override
    public OshClient updateClient(long id, ClientDTO clientDTO, String username) {
        User user = userService.findByEmail(username);
        OshClient client = oshClientRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " has not found"));

        OshHistory historyStatus = null, historyOccupation = null, historyCourse = null, historyUTM = null;
        if (clientDTO.getStatus() != 0) {
            OshStatuses statuses = oshStatusesRepo.findById(clientDTO.getStatus())
                    .orElseThrow(() -> new ResourceNotFoundException("Status with id " + clientDTO.getStatus() + " has not found"));

            historyStatus = new OshHistory();
            historyStatus.setFullName(user.getName() + " " + user.getSurname());
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
            historyOccupation.setFullName(user.getName() + " " + user.getSurname());
            historyOccupation.setClientPhone(client.getPhoneNo());
            historyOccupation.setAction("occupation");
            OshOccupation oldOccupation = client.getOccupation();
            if (oldOccupation != null)
                historyOccupation.setOldData(client.getOccupation().getName());
            historyOccupation.setNewData(occupation.getName());
            client.setOccupation(occupation);
        }
        if (clientDTO.getCourse() != 0) {
            OshCourses course = coursesService.findCourseById(clientDTO.getCourse());
            historyCourse = new OshHistory();
            historyCourse.setFullName(user.getName() + " " + user.getSurname());
            historyCourse.setClientPhone(client.getPhoneNo());
            historyCourse.setAction("course");
            OshCourses oldCourse = client.getCourse();
            if (oldCourse != null)
                historyCourse.setOldData(oldCourse.getName());
            historyCourse.setNewData(course.getName());
            client.setCourse(course);
        }
        if (clientDTO.getUTM() != 0) {
            OshUTM utm = oshUTMRepo.findById(clientDTO.getUTM())
                    .orElseThrow(() -> new ResourceNotFoundException("UTM with id " + clientDTO.getUTM() + " has not found"));
            historyUTM = new OshHistory();
            historyUTM.setFullName(user.getName() + " " + user.getSurname());
            historyUTM.setClientPhone(client.getPhoneNo());
            historyUTM.setAction("UTM");
            OshUTM oldUTM = client.getUtm();
            if (oldUTM != null)
                historyUTM.setOldData(oldUTM.getName());
            historyUTM.setNewData(utm.getName());
            client.setUtm(utm);
        }

        client.setPhoneNo(clientDTO.getPhoneNo());
        client.setName(clientDTO.getName());
        client.setSurname(clientDTO.getSurname());
        client.setPatronymic(clientDTO.getPatronymic());
        client.setEmail(clientDTO.getEmail());
        client.setExperience(clientDTO.isExperience());
        client.setLaptop(clientDTO.isLaptop());
	    client.setDescription(clientDTO.getDescription());
        client.setCity("OSH");
        client.setPrepayment(clientDTO.getPrepayment());
        if (clientDTO.getTarget() != 0)
            client.setTarget(oshTargetRepo.findById(clientDTO.getTarget())
                    .orElseThrow(() -> new ResourceNotFoundException("Target with ID " + clientDTO.getTarget() + " has not found")));
        if (clientDTO.getLeavingReason() != 0)
            client.setLeavingReason(oshLeavingReasonRepo.findById(clientDTO.getLeavingReason())
                    .orElseThrow((() -> new ResourceNotFoundException("Reason with id " + clientDTO.getLeavingReason() + "has not found"))));

        if (client.getStatus().getID() == 7)
            client.setTimer(null);
        else if (client.getTimer() == null && clientDTO.getTimer() == null)
            client.setTimer(LocalDateTime.now().plusHours(24L));
        else if (client.getTimer() == null)
            client.setTimer(clientDTO.getTimer());
        else if (!client.getTimer().isAfter(LocalDateTime.now().plusHours(24L))){
            client.setTimer(clientDTO.getTimer());
        }

        client = oshClientRepo.save(client);
        if (historyCourse != null && (historyCourse.getOldData() == null || !historyCourse.getOldData().equals(historyCourse.getNewData())))
            historyService.create(historyCourse);
        if (historyOccupation != null && (historyOccupation.getOldData() == null || !historyOccupation.getOldData().equals(historyOccupation.getNewData())))
            historyService.create(historyOccupation);
        if (historyUTM != null && (historyUTM.getOldData() == null || !historyUTM.getOldData().equals(historyUTM.getNewData())))
            historyService.create(historyUTM);
        if (historyStatus != null && (historyStatus.getOldData() == null || !historyStatus.getOldData().equals(historyStatus.getNewData())))
            historyService.create(historyStatus);
        return client;
    }

    @Override
    public void changeCity(long id, String userEmail) {
        User user = userService.findByEmail(userEmail);
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
            bishClient.setOccupation(bishOccupationRepo.findByNameContainingIgnoringCase(oshClient.getOccupation().getName()));
        if (oshClient.getTarget() != null)
            bishClient.setTarget(bishTargetRepo.findByNameContainingIgnoringCase(oshClient.getTarget().getName()));
        bishClient.setExperience(oshClient.isExperience());
        bishClient.setLaptop(oshClient.isLaptop());
        if (oshClient.getUtm() != null)
            bishClient.setUtm(bishUTMRepo.findByNameContainingIgnoringCase(oshClient.getUtm().getName()).orElse(null));
        bishClient.setDescription(oshClient.getDescription());
        bishClient.setCity("OSH");
        bishClient.setFormName(oshClient.getFormName());
//         TODO
        bishClient.setTimer(LocalDateTime.now().plusHours(24L));
        bishClient.setPrepayment(oshClient.getPrepayment());
        if (oshClient.getLeavingReason() != null)
            bishClient.setLeavingReason(bishLeavingReasonRepo.findByNameContainingIgnoringCase(bishClient.getLeavingReason().getName()).orElse(null));
        bishClientRepo.save(bishClient);

//        Second step - create all payments of oshClient
        List<OshPayment> oshPayments =  oshClient.getPayments();
        List<BishPayment> bishPayments = new ArrayList<>();
        for (OshPayment oshPayment : oshPayments) {
            BishPayment bishPayment = new BishPayment();
            bishPayment.setMonth(oshPayment.getMonth());
            bishPayment.setPrice(oshPayment.getPrice());
            bishPayment.setDone(oshPayment.isDone());
            OshMethod oshMethod = oshPayment.getMethod();
            bishPayment.setMethod(bishMethodRepo.findByNameContainingIgnoringCase(oshMethod.getName()));
            bishPayments.add(bishPaymentRepo.save(bishPayment));

//            Third step is inside of second step - delete payments of oshClient
            oshPaymentRepo.delete(oshPayment);
        }
        bishClient.setPayments(bishPayments);
        bishClientRepo.save(bishClient);

//        Fourth step - delete oshClient
        oshClientRepo.delete(oshClient);

        OshHistory history = new OshHistory();
        history.setFullName(user.getName() + " " + user.getSurname());
        history.setAction("change city of client");
        history.setClientPhone(bishClient.getPhoneNo());
        history.setOldData("Osh");
        history.setNewData("Bishkek");
        historyService.create(history);
    }

    @Override
    public List<Object> simpleSearch(String nameOrPhone) {
        List<Object> clients = new ArrayList<>();
        for (String item : nameOrPhone.split(" ")) {
            clients.addAll(oshClientRepo.findAllByNameContainingIgnoringCase(item));
            clients.addAll(oshClientRepo.findAllBySurnameContainingIgnoringCase(item));
        }
        clients.addAll(oshClientRepo.findAllByPhoneNoContaining(nameOrPhone));
        return clients;
    }

    @Override
    public Set<OshClient> search(String nameOrPhone) {
        Set<OshClient> clients = new HashSet<>();
        for (String item : nameOrPhone.split(" ")) {
            clients.addAll(oshClientRepo.findAllByNameContainingIgnoringCase(item));
            clients.addAll(oshClientRepo.findAllBySurnameContainingIgnoringCase(item));
        }
        clients.addAll(oshClientRepo.findAllByPhoneNoContaining(nameOrPhone));
        clients.addAll(oshClientRepo.findAllByEmailContainingIgnoringCase(nameOrPhone));

        return clients;
    }

    @Override
    public OshClient addPayment(long clientID, PaymentDTO paymentDTO, String userEmail) {
        User user = userService.findByEmail(userEmail);

        OshClient client = this.getClientByID(clientID);
        List<OshPayment> payments = client.getPayments();
        OshPayment payment = new OshPayment();
        payment.setMonth(paymentDTO.getMonth());
        payment.setPrice(paymentDTO.getPrice());
        payment.setDone(paymentDTO.isDone());
        OshCourses course = client.getCourse();
        if (course == null)
            throw new IllegalArgumentException("Enroll a student for a course before setting up payment");
        payment.setCourse(course);
        if (paymentDTO.getMethodID() != 0)
            payment.setMethod(oshMethodRepo.findById(paymentDTO.getMethodID()).orElseThrow(() ->
                new ResourceNotFoundException("Method with ID " + paymentDTO.getMethodID() + " has not found")));
        payments.add(oshPaymentRepo.save(payment));
        client.setPayments(payments);
        client = oshClientRepo.save(client);

        OshHistory history = new OshHistory();
        history.setFullName(user.getName() + " " + user.getSurname());
        history.setClientPhone(client.getPhoneNo());
        history.setAction("add payment");
        history.setNewData(course.getName() + " " + payment.getMonth());
        historyService.create(history);
        return client;
    }

    @Override
    public OshClient editPayment(long clientID, PaymentDTO paymentDTO, long paymentID, String userEmail) {
        User user = userService.findByEmail(userEmail);

        OshClient client = this.getClientByID(clientID);
        List<OshPayment> payments = client.getPayments();
        OshPayment payment = oshPaymentRepo.findById(paymentID)
                .orElseThrow(() -> new ResourceNotFoundException("Payment with ID " + paymentID + " has not found"));
        if (!payments.contains(payment))
            throw new ResourceNotFoundException("Payment with ID " + paymentID + " has not found in list of this client");

        payment.setMonth(paymentDTO.getMonth());
        payment.setPrice(paymentDTO.getPrice());
        payment.setDone(paymentDTO.isDone());
        OshCourses course = client.getCourse();
        if (course == null)
            throw new IllegalArgumentException("Enroll a student for a course before setting up payment");
        payment.setCourse(course);
        if (paymentDTO.getMethodID() != 0)
            payment.setMethod(oshMethodRepo.findById(paymentDTO.getMethodID()).orElseThrow(() ->
                    new ResourceNotFoundException("Method with ID " + paymentDTO.getMethodID() + " has not found")));
        oshPaymentRepo.save(payment);

        OshHistory history = new OshHistory();
        history.setFullName(user.getName() + " " + user.getSurname());
        history.setClientPhone(client.getPhoneNo());
        history.setAction("update payment");
        history.setNewData(course.getName() + " " + payment.getMonth());
        historyService.create(history);
        return client;
    }

    @Override
    public ResponseMessage deletePayment(long clientID, long paymentID, String userEmail) {
        User user = userService.findByEmail(userEmail);

        OshClient client = oshClientRepo.findById(clientID)
                .orElseThrow(() -> new ResourceNotFoundException("Client with ID " + clientID + " has not found"));
        List<OshPayment> payments = client.getPayments();
        OshPayment payment = oshPaymentRepo.findById(paymentID)
                .orElseThrow(() -> new ResourceNotFoundException("Payment with ID " + paymentID + " has not found"));
        oshPaymentRepo.delete(payment);
        payments.remove(payment);
        client.setPayments(payments);
        oshClientRepo.save(client);

        OshHistory history = new OshHistory();
        history.setFullName(user.getName() + " " + user.getSurname());
        history.setClientPhone(client.getPhoneNo());
        history.setAction("delete payment");
        history.setNewData(payment.getCourse().getName() + " " + payment.getMonth());
        historyService.create(history);

        return new ResponseMessage("Payments of client with ID " + clientID + " has been updated");
    }

    @Override
    public ResponseMessage deleteClient(long clientID, String userEmail) {
        User user = userService.findByEmail(userEmail);

        OshClient client = this.getClientByID(clientID);
        for (OshPayment payment : client.getPayments())
            oshPaymentRepo.delete(payment);
        oshClientRepo.delete(client);

        OshHistory history = new OshHistory();
        history.setFullName(user.getName() + " " + user.getSurname());
        history.setClientPhone(client.getPhoneNo());
        history.setAction("delete client");
        historyService.create(history);
        return new ResponseMessage("Client with ID " + clientID + " has been deleted");
    }

    @Override
    public List<OshClient> getClientsWithExpiredTimer() {
        return oshClientRepo.findAllByTimerBefore(LocalDateTime.now());
    }

    @Override
    public OshClient updateTimer(String userEmail, long clientID, LocalDateTime timer) {
        OshClient client = this.getClientByID(clientID);
        client.setTimer(timer);
        client = oshClientRepo.save(client);

        User user = userService.findByEmail(userEmail);
        OshHistory history = new OshHistory();
        history.setFullName(user.getName() + " " + user.getSurname());
        history.setClientPhone(client.getPhoneNo());
        history.setAction("Обновление даты таймера");
        history.setNewData(timer.toString());
        historyService.create(history);
        return client;
    }
}
