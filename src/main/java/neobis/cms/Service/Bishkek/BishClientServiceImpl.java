package neobis.cms.Service.Bishkek;

import neobis.cms.Dto.ClientDTO;
import neobis.cms.Dto.PaymentDTO;
import neobis.cms.Dto.ResponseMessage;
import neobis.cms.Entity.Bishkek.*;
import neobis.cms.Entity.Osh.*;
import neobis.cms.Exception.IllegalArgumentException;
import neobis.cms.Exception.ResourceNotFoundException;
import neobis.cms.Repo.Bishkek.*;
import neobis.cms.Repo.Osh.*;
import neobis.cms.Search.BishClientSpecification;
import neobis.cms.Search.SearchCriteria;
import neobis.cms.Search.SearchOperation;
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

    private final BishPaymentRepo bishPaymentRepo;
    private final OshPaymentRepo oshPaymentRepo;

    private final BishHistoryService bishHistoryService;

    private final UserService userService;

    private final BishLeavingReasonRepo bishLeavingReasonRepo;
    private final OshLeavingReasonRepo oshLeavingReasonRepo;

    private final BishTargetRepo bishTargetRepo;
    private final OshTargetRepo oshTargetRepo;

    private final BishMethodRepo bishMethodRepo;
    private final OshMethodRepo oshMethodRepo;

    public BishClientServiceImpl(BishStatusesRepo statusesRepo, BishClientRepo bishClientRepo, OshClientRepo oshClientRepo, OshStatusesRepo oshStatusesRepo,
                                 BishOccupationRepo bishOccupationRepo, OshOccupationRepo oshOccupationRepo, BishUTMRepo bishUTMRepo, OshUTMRepo oshUTMRepo,
                                 BishCoursesService coursesService, BishPaymentRepo bishPaymentService, OshPaymentRepo oshPaymentService,
                                 BishHistoryService bishHistoryService, UserService userService, BishLeavingReasonRepo bishLeavingReasonRepo,
                                 OshLeavingReasonRepo oshLeavingReasonRepo, BishTargetRepo bishTargetRepo, OshTargetRepo oshTargetRepo, BishMethodRepo bishMethodRepo, OshMethodRepo oshMethodRepo) {
        this.bishStatusesRepo = statusesRepo;
        this.bishClientRepo = bishClientRepo;
        this.oshClientRepo = oshClientRepo;
        this.oshStatusesRepo = oshStatusesRepo;
        this.bishOccupationRepo = bishOccupationRepo;
        this.oshOccupationRepo = oshOccupationRepo;
        this.bishUTMRepo = bishUTMRepo;
        this.oshUTMRepo = oshUTMRepo;
        this.coursesService = coursesService;
        this.bishPaymentRepo = bishPaymentService;
        this.oshPaymentRepo = oshPaymentService;
        this.bishHistoryService = bishHistoryService;
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
        String dataResourceUrl = "https://neolabs.dev/mod/api/?api_key=e539509b630b27e47ac594d0dbba4e69&method=getLeads&start=0&count=200";
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
                                    BishUTM utm;
                                    if (utmName.contains("instagram")) {
                                        utm = bishUTMRepo.findByNameContainingIgnoringCase("Instagram").orElse(null);
                                        if (utm == null) {
                                            oshUTMRepo.save(new OshUTM("Instagram"));
                                            utm = bishUTMRepo.save(new BishUTM("Instagram"));
                                        }
                                        client.setUtm(utm);
                                    }
                                    if (utmName.contains("facebook")) {
                                        utm = bishUTMRepo.findByNameContainingIgnoringCase("Facebook").orElse(null);
                                        if (utm == null) {
                                            oshUTMRepo.save(new OshUTM("Facebook"));
                                            utm = bishUTMRepo.save(new BishUTM("Facebook"));
                                        }
                                        client.setUtm(utm);
                                    }
                                    if (utmName.contains("google")) {
                                        utm = bishUTMRepo.findByNameContainingIgnoringCase("Google").orElse(null);
                                        if (utm == null) {
                                            oshUTMRepo.save(new OshUTM("Google"));
                                            utm = bishUTMRepo.save(new BishUTM("Google"));
                                        }
                                        client.setUtm(utm);
                                    }
                                    if (utmName.contains("neobis")) {
                                        utm = bishUTMRepo.findByNameContainingIgnoringCase("Neobis").orElse(null);
                                        if (utm == null) {
                                            oshUTMRepo.save(new OshUTM("Neobis"));
                                            utm = bishUTMRepo.save(new BishUTM("Neobis"));
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
                                    BishTarget target = bishTargetRepo.findByNameContainingIgnoringCase(targetName);
                                    if (target == null) {
                                        oshTargetRepo.save(new OshTarget(targetName));
                                        target = bishTargetRepo.save(new BishTarget(targetName));
                                    }
                                    client.setTarget(target);
                                }
                                if (occupation) {
                                    String occupationName = data.getString(key);
                                    BishOccupation bishOccupation = bishOccupationRepo.findByNameContainingIgnoringCase(occupationName);
                                    if (bishOccupation == null) {
                                        oshOccupationRepo.save(new OshOccupation(occupationName));
                                        bishOccupation = bishOccupationRepo.save(new BishOccupation(occupationName));
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
    public Page<BishClient> getAllClientsFromDB(Pageable pageable) {
        List<BishClient> clients = bishClientRepo.findAllByOrderByDateCreatedDesc();
        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), clients.size());

        return new PageImpl<>(clients.subList(start, end), pageable, clients.size());
    }

    @Override
    public void addClientsToDB() {
        JSONObject data;
        data = new JSONObject(this.getNewClients());
        List<BishClient> clients = this.getClientsFromJson(data);
        for (BishClient client : clients) {
            if (!client.getFormName().equals("Набор в клуб"))
                this.create(client);
        }
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
    public Set<BishClient> getWithPredicate(String field, List<Long> status, List<Long> course, List<Long> occupation, List<Long> utm) {
        BishClientSpecification<BishClient> bishClientSpecification = new BishClientSpecification<>();
        if (status != null)
            bishClientSpecification.add(new SearchCriteria("status", null, status, SearchOperation.EQUAL));
        if (course != null)
            bishClientSpecification.add(new SearchCriteria("course", null, course, SearchOperation.EQUAL));
        if (occupation != null)
            bishClientSpecification.add(new SearchCriteria("occupation", null, occupation, SearchOperation.EQUAL));
        if (utm != null)
            bishClientSpecification.add(new SearchCriteria("utm", null, utm, SearchOperation.EQUAL));
        if (field != null) {
            for (String item : field.split(" ")) {
                bishClientSpecification.add(new SearchCriteria("name", item, null, SearchOperation.MATCH));
                bishClientSpecification.add(new SearchCriteria("surname", item, null, SearchOperation.MATCH));
            }
            bishClientSpecification.add(new SearchCriteria("phoneNo", field, null, SearchOperation.MATCH));
            bishClientSpecification.add(new SearchCriteria("email", field, null, SearchOperation.MATCH));
        }
        Set<BishClient> clients = new HashSet<>(bishClientRepo.findAll(bishClientSpecification));
        return clients;
    }

    @Override
    public BishClient create(ClientDTO clientDTO, String userEmail) {
        User user = userService.findByEmail(userEmail);

        BishClient client = new BishClient();
        client.setDateCreated(LocalDateTime.now());
        client.setPhoneNo(clientDTO.getPhoneNo());
        client.setName(clientDTO.getName());
        client.setSurname(clientDTO.getSurname());
        client.setPatronymic(clientDTO.getPatronymic());
        client.setEmail(clientDTO.getEmail());
        client.setExperience(clientDTO.isExperience());
        client.setLaptop(clientDTO.isLaptop());
        client.setDescription(clientDTO.getDescription());
        client.setCity("BISHKEK");
        client.setPrepayment(clientDTO.getPrepayment());
        if (clientDTO.getTarget() != 0)
            client.setTarget(bishTargetRepo.findById(clientDTO.getTarget())
                    .orElseThrow(() -> new ResourceNotFoundException("Target with ID " + clientDTO.getTarget() + " has not found")));
        if (clientDTO.getLeavingReason() != 0)
            client.setLeavingReason(bishLeavingReasonRepo.findById(clientDTO.getLeavingReason())
                    .orElseThrow(() -> new ResourceNotFoundException("Reason with id " + clientDTO.getLeavingReason() + "has not found")));

        BishHistory historyStatus = null, historyOccupation = null, historyCourse = null, historyUTM = null;
        if (clientDTO.getStatus() != 0) {
            BishStatuses statuses = bishStatusesRepo.findById(clientDTO.getStatus())
                    .orElseThrow(() -> new ResourceNotFoundException("Status with id " + clientDTO.getStatus() + " has not found"));
            client.setStatus(statuses);
            historyStatus = new BishHistory();
            historyStatus.setFullName(user.getName() + " " + user.getSurname());
            historyStatus.setClientPhone(client.getPhoneNo());
            historyStatus.setAction("status");
            historyStatus.setNewData(statuses.getName());
        }
        if (clientDTO.getOccupation() != 0) {
            BishOccupation occupation = bishOccupationRepo.findById(clientDTO.getOccupation())
                    .orElseThrow(() -> new ResourceNotFoundException("Occupation with id " + clientDTO.getOccupation() + " has not found"));
            client.setOccupation(occupation);
            historyOccupation = new BishHistory();
            historyOccupation.setFullName(user.getName() + " " + user.getSurname());
            historyOccupation.setClientPhone(client.getPhoneNo());
            historyOccupation.setAction("occupation");
            historyOccupation.setNewData(occupation.getName());
        }
        if (clientDTO.getCourses() != null) {
            List<BishCourses> courses = new ArrayList<>();
            StringBuilder courseNames = new StringBuilder();
            historyCourse = new BishHistory();
            historyCourse.setFullName(user.getName() + " " + user.getSurname());
            historyCourse.setClientPhone(client.getPhoneNo());
            historyCourse.setAction("course");

            for (long courseID : clientDTO.getCourses()) {
                BishCourses course = coursesService.findCourseById(courseID);
                if (course == null)
                    throw new ResourceNotFoundException("Course with id " + courseID + " has not found");
                courses.add(course);
                courseNames.append(course.getName()).append(", ");
            }
            client.setCourses(courses);
            historyCourse.setNewData(courseNames.toString());
        }
        if (clientDTO.getUTM() != 0) {
            BishUTM utm = bishUTMRepo.findById(clientDTO.getUTM())
                    .orElseThrow(() -> new ResourceNotFoundException("UTM with id " + clientDTO.getUTM() + " has not found"));
            client.setUtm(utm);
            historyUTM = new BishHistory();
            historyUTM.setFullName(user.getName() + " " + user.getSurname());
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
        User user = userService.findByEmail(username);

        BishClient client = bishClientRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " has not found"));

        BishHistory bishHistory = new BishHistory();
        bishHistory.setAction("status");
        if (client.getStatus() == null)
            bishHistory.setOldData(null);
        else
            bishHistory.setOldData(client.getStatus().getName());
        bishHistory.setFullName(user.getName() + " " + user.getSurname());
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
        User user = userService.findByEmail(username);
        BishClient client = bishClientRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " has not found"));

        BishHistory historyStatus = null, historyOccupation = null, historyCourse = null, historyUTM = null;
        if (clientDTO.getStatus() != 0) {
            BishStatuses statuses = bishStatusesRepo.findById(clientDTO.getStatus())
                    .orElseThrow(() -> new ResourceNotFoundException("Status with id " + clientDTO.getStatus() + " has not found"));

            historyStatus = new BishHistory();
            historyStatus.setFullName(user.getName() + " " + user.getSurname());
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
            historyOccupation.setFullName(user.getName() + " " + user.getSurname());
            historyOccupation.setClientPhone(client.getPhoneNo());
            historyOccupation.setAction("occupation");
            BishOccupation oldOccupation = client.getOccupation();
            if (oldOccupation != null)
                historyOccupation.setOldData(client.getOccupation().getName());
            historyOccupation.setNewData(occupation.getName());
            client.setOccupation(occupation);
        }
        if (clientDTO.getCourses() != null) {
            historyCourse = new BishHistory();
            historyCourse.setFullName(user.getName() + " " + user.getSurname());
            historyCourse.setClientPhone(client.getPhoneNo());
            historyCourse.setAction("course");

            List<BishCourses> courses = new ArrayList<>();
            StringBuilder courseNames = new StringBuilder();
            for (BishCourses course : client.getCourses()) {
                courses.add(course);
                courseNames.append(course.getName()).append(", ");
            }
            historyCourse.setOldData(courseNames.toString());

            courses = new ArrayList<>();
            courseNames = new StringBuilder();
            for (long courseID : clientDTO.getCourses()) {
                BishCourses course = coursesService.findCourseById(courseID);
                if (course == null)
                    throw new ResourceNotFoundException("Course with id " + courseID + " has not found");
                courses.add(course);
                courseNames.append(course.getName()).append(", ");
            }
            client.setCourses(courses);
            historyCourse.setNewData(courseNames.toString());
        }
        if (clientDTO.getUTM() != 0) {
            BishUTM utm = bishUTMRepo.findById(clientDTO.getUTM())
                    .orElseThrow(() -> new ResourceNotFoundException("UTM with id " + clientDTO.getUTM() + " has not found"));
            historyUTM = new BishHistory();
            historyUTM.setFullName(user.getName() + " " + user.getSurname());
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
        client.setPatronymic(clientDTO.getPatronymic());
        client.setEmail(clientDTO.getEmail());
        client.setExperience(clientDTO.isExperience());
        client.setLaptop(clientDTO.isLaptop());
	    client.setDescription(clientDTO.getDescription());
        client.setCity("BISHKEK");
        client.setPrepayment(clientDTO.getPrepayment());
        if (clientDTO.getTarget() != 0)
            client.setTarget(bishTargetRepo.findById(clientDTO.getTarget())
                    .orElseThrow(() -> new ResourceNotFoundException("Target with ID " + clientDTO.getTarget() + " has not found")));
        if (clientDTO.getLeavingReason() != 0)
            client.setLeavingReason(bishLeavingReasonRepo.findById(clientDTO.getLeavingReason())
                    .orElseThrow(() -> new ResourceNotFoundException("Reason with id " + clientDTO.getLeavingReason() + "has not found")));


        if (client.getTimer() == null) {
            if (clientDTO.getTimer() == null)
                client.setTimer(LocalDateTime.now().plusHours(24L));
            else
                client.setTimer(clientDTO.getTimer());
        } else if (!client.getTimer().isAfter(LocalDateTime.now().plusHours(24L))){
            client.setTimer(clientDTO.getTimer());
        }
        client = bishClientRepo.save(client);
        if (historyCourse != null && (historyCourse.getOldData() == null || !historyCourse.getOldData().equals(historyCourse.getNewData())))
            bishHistoryService.create(historyCourse);
        if (historyOccupation != null && (historyOccupation.getOldData() == null || !historyOccupation.getOldData().equals(historyOccupation.getNewData())))
            bishHistoryService.create(historyOccupation);
        if (historyUTM != null && (historyUTM.getOldData() == null || !historyUTM.getOldData().equals(historyUTM.getNewData())))
            bishHistoryService.create(historyUTM);
        if (historyStatus != null && (historyStatus.getOldData() == null || !historyStatus.getOldData().equals(historyStatus.getNewData())))
            bishHistoryService.create(historyStatus);
        return client;
    }

    @Override
    public void changeCity(long id, String userEmail) {
        User user = userService.findByEmail(userEmail);
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
            oshClient.setOccupation(oshOccupationRepo.findByNameContainingIgnoringCase(bishClient.getOccupation().getName()));
        if (bishClient.getTarget() != null)
            oshClient.setTarget(oshTargetRepo.findByNameContainingIgnoringCase(bishClient.getTarget().getName()));
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
        oshClient.setLeavingReason(oshLeavingReasonRepo.findByNameContainingIgnoringCase(bishClient.getLeavingReason().getName()).orElse(null));
        oshClientRepo.save(oshClient);

//        Second step - create all payments of bishClient
        List<BishPayment> bishPayments =  bishClient.getPayments();
        List<OshPayment> oshPayments = new ArrayList<>();
        for (BishPayment bishPayment : bishPayments) {
            OshPayment oshPayment = new OshPayment();
            oshPayment.setMonth(bishPayment.getMonth());
            oshPayment.setPrice(bishPayment.getPrice());
            oshPayment.setDone(bishPayment.isDone());
            BishMethod bishMethod = bishPayment.getMethod();
            oshPayment.setMethod(oshMethodRepo.findByNameContainingIgnoringCase(bishMethod.getName()));
            oshPayments.add(oshPaymentRepo.save(oshPayment));

//            Third step is inside of second step - delete payments of bishClient
            bishPaymentRepo.delete(bishPayment);
        }
        oshClient.setPayments(oshPayments);
        oshClientRepo.save(oshClient);

//        Fourth step - delete bishClient
        bishClientRepo.delete(bishClient);

        BishHistory history = new BishHistory();
        history.setFullName(user.getName() + " " + user.getSurname());
        history.setAction("change city of client");
        history.setClientPhone(oshClient.getPhoneNo());
        history.setOldData("Bishkek");
        history.setNewData("Osh");
        bishHistoryService.create(history);
    }

    @Override
    public Set<Object> simpleSearch(String nameOrPhone) {
        Set<Object> clients = new HashSet<>();
//        List<Object> clients = new ArrayList<>();
        for (String item : nameOrPhone.split(" ")) {
            clients.addAll(bishClientRepo.findAllByNameContainingIgnoringCase(item));
            clients.addAll(bishClientRepo.findAllBySurnameContainingIgnoringCase(item));
        }
        clients.addAll(bishClientRepo.findAllByPhoneNoContaining(nameOrPhone));
        return clients;
    }

    @Override
    public Set<BishClient> search(String nameOrPhone) {
        Set<BishClient> clients = new HashSet<>();
//        List<Object> clients = new ArrayList<>();
        for (String item : nameOrPhone.split(" ")) {
            clients.addAll(bishClientRepo.findAllByNameContainingIgnoringCase(item));
            clients.addAll(bishClientRepo.findAllBySurnameContainingIgnoringCase(item));
        }
        clients.addAll(bishClientRepo.findAllByPhoneNoContaining(nameOrPhone));
        clients.addAll(bishClientRepo.findAllByEmailContainingIgnoringCase(nameOrPhone));

        return clients;
    }
/*
    @Override
    public List<BishClient> advancedSearch(List<Long> status, List<Long> course, List<Long> occupation) {
        BishClientSpecification<BishClient> bishClientSpecification = new BishClientSpecification<>();
        if (status != null)
            bishClientSpecification.add(new SearchCriteria("status", status, SearchOperation.EQUAL));
        if (course != null)
            bishClientSpecification.add(new SearchCriteria("course", course, SearchOperation.EQUAL));
        if (occupation != null)
            bishClientSpecification.add(new SearchCriteria("occupation", occupation, SearchOperation.EQUAL));
        return bishClientRepo.findAll(bishClientSpecification);
    }

    @Override
    public List<BishClient> advancedStudentSearch(List<Long> course) {
        BishClientSpecification<BishClient> bishClientSpecification = new BishClientSpecification<>();
        if (bishStatusesRepo.findByNameContainingIgnoringCase("Студент") != null)
            bishClientSpecification.add(new SearchCriteria("status",
                    Collections.singletonList(bishStatusesRepo.findByNameContainingIgnoringCase("Студент").getID()), SearchOperation.EQUAL));
        if (course != null)
            bishClientSpecification.add(new SearchCriteria("course", course, SearchOperation.EQUAL));
        return bishClientRepo.findAll(bishClientSpecification);
    }
*/
    @Override
    public BishClient addNewPayment(long clientID, PaymentDTO paymentDTO, String userEmail) {
        BishClient client = this.getClientById(clientID);
        List<BishPayment> payments = client.getPayments();
        BishPayment payment = new BishPayment();
        payment.setMonth(paymentDTO.getMonth());
        payment.setPrice(paymentDTO.getPrice());
        payment.setDone(paymentDTO.isDone());
        if (paymentDTO.getMethodID() != 0)
            payment.setMethod(bishMethodRepo.findById(paymentDTO.getMethodID()).orElseThrow(() ->
                new ResourceNotFoundException("Method with ID " + paymentDTO.getMethodID() + " has not found")));
        BishCourses course = coursesService.findCourseById(paymentDTO.getCourseID());
        if (paymentDTO.getCourseID() != 0 && client.getCourses().contains(course))
            payment.setCourse(course);
        else if (paymentDTO.getCourseID() != 0)
            throw new IllegalArgumentException("Course with ID " + paymentDTO.getCourseID() + " not listed in client's course list");
        payments.add(bishPaymentRepo.save(payment));
        client.setPayments(payments);
        return bishClientRepo.save(client);
    }

    @Override
    public BishClient editPayment(long clientID, PaymentDTO paymentDTO, long paymentID, String userEmail) {
        BishClient client = this.getClientById(clientID);
        List<BishPayment> payments = client.getPayments();
        BishPayment payment = bishPaymentRepo.findById(paymentID)
                .orElseThrow(() -> new ResourceNotFoundException("Payment with ID " + paymentID + " has not found"));
        if (!payments.contains(payment))
            throw new ResourceNotFoundException("Payment with ID " + paymentID + " has not found in list of this client");

        payment.setMonth(paymentDTO.getMonth());
        payment.setPrice(paymentDTO.getPrice());
        payment.setDone(paymentDTO.isDone());
        if (paymentDTO.getMethodID() != 0)
            payment.setMethod(bishMethodRepo.findById(paymentDTO.getMethodID()).orElseThrow(() ->
                new ResourceNotFoundException("Method with ID " + paymentDTO.getMethodID() + " has not found")));
        BishCourses course = coursesService.findCourseById(paymentDTO.getCourseID());
        if (paymentDTO.getCourseID() != 0 && client.getCourses().contains(course))
            payment.setCourse(course);
        else if (paymentDTO.getCourseID() != 0)
            throw new IllegalArgumentException("Course with ID " + paymentDTO.getCourseID() + " not listed in client's course list");
        bishPaymentRepo.save(payment);
        return client;
    }

    @Override
    public ResponseMessage deletePayment(long clientID, long paymentID, String userEmail) {
        BishClient client = bishClientRepo.findById(clientID)
                .orElseThrow(() -> new ResourceNotFoundException("Client with ID " + clientID + " has not found"));
        List<BishPayment> payments = client.getPayments();
        BishPayment payment = bishPaymentRepo.findById(paymentID)
                .orElseThrow(() -> new ResourceNotFoundException("Payment with ID " + paymentID + " has not found"));
        bishPaymentRepo.delete(payment);
        payments.remove(payment);
        client.setPayments(payments);
        bishClientRepo.save(client);
        return new ResponseMessage("Payments of client with ID " + clientID + " has been updated");
    }

    @Override
    public ResponseMessage deleteClient(long clientID, String userEmail) {
        BishClient client = this.getClientById(clientID);
        for (BishPayment payment : client.getPayments())
            bishPaymentRepo.delete(payment);
        bishClientRepo.delete(client);
        return new ResponseMessage("Client with ID " + clientID + " has been deleted");
    }

    @Override
    public List<BishCourses> getCourses(long clientID) {
        BishClient client = this.getClientById(clientID);
        return client.getCourses();
    }
}
