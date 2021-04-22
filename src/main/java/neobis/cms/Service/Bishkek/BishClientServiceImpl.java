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

    public BishClientServiceImpl(BishStatusesRepo statusesRepo, BishClientRepo bishClientRepo, OshClientRepo
                                 oshClientRepo, OshStatusesRepo oshStatusesRepo, BishOccupationRepo bishOccupationRepo,
                                 OshOccupationRepo oshOccupationRepo, BishUTMRepo bishUTMRepo, OshUTMRepo oshUTMRepo,
                                 BishCoursesService coursesService, BishPaymentRepo bishPaymentService, OshPaymentRepo
                                 oshPaymentService, BishHistoryService bishHistoryService, UserService userService,
                                 BishLeavingReasonRepo bishLeavingReasonRepo, OshLeavingReasonRepo oshLeavingReasonRepo,
                                 BishTargetRepo bishTargetRepo, OshTargetRepo oshTargetRepo, BishMethodRepo bishMethodRepo,
                                 OshMethodRepo oshMethodRepo) {
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
        String dataResourceUrl = "https://neolabs.dev/mod/api/?api_key=e539509b630b27e47ac594d0dbba4e69&method=getLeads&start=0&count=100";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(dataResourceUrl);
        HttpEntity<String> httpEntity = new HttpEntity<>("", httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, httpEntity, String.class);
        return response.getBody();
    }

    @Override
    public BishClient create(BishClient client) {
        client.setCity("Bishkek");
        client.setDateCreated(LocalDateTime.now());
        client.setTimer(LocalDateTime.now().plusHours(24L));
        client.setStatus(bishStatusesRepo.findById(1L).orElse(null));
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
                .orElseThrow(() -> new ResourceNotFoundException("Статус с идентификатором " + status + " не найден."));
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
        return new HashSet<>(bishClientRepo.findAll(bishClientSpecification));
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
                    .orElseThrow(() -> new ResourceNotFoundException("Цель с идентификатором " + clientDTO.getTarget() + " не найдена.")));
        if (clientDTO.getLeavingReason() != 0)
            client.setLeavingReason(bishLeavingReasonRepo.findById(clientDTO.getLeavingReason())
                    .orElseThrow(() -> new ResourceNotFoundException("Причина с идентификатором " + clientDTO.getLeavingReason() + " не найдена.")));

        BishHistory historyStatus = null, historyOccupation = null, historyCourse = null, historyUTM = null;
        if (clientDTO.getStatus() != 0) {
            BishStatuses statuses = bishStatusesRepo.findById(clientDTO.getStatus())
                    .orElseThrow(() -> new ResourceNotFoundException("Статус с идентификатором " + clientDTO.getStatus() + " не найден."));
            client.setStatus(statuses);
            historyStatus = new BishHistory();
            historyStatus.setFullName(user.getName() + " " + user.getSurname());
            historyStatus.setClientPhone(client.getPhoneNo());
            historyStatus.setAction("Статус");
            historyStatus.setNewData(statuses.getName());
        }
        if (clientDTO.getOccupation() != 0) {
            BishOccupation occupation = bishOccupationRepo.findById(clientDTO.getOccupation())
                    .orElseThrow(() -> new ResourceNotFoundException("Деятельность с идентификатором " + clientDTO.getOccupation() + " не найдена."));
            client.setOccupation(occupation);
            historyOccupation = new BishHistory();
            historyOccupation.setFullName(user.getName() + " " + user.getSurname());
            historyOccupation.setClientPhone(client.getPhoneNo());
            historyOccupation.setAction("Деятельность");
            historyOccupation.setNewData(occupation.getName());
        }
        if (clientDTO.getCourse() != 0) {
            BishCourses course = coursesService.findCourseById(clientDTO.getCourse());
            if (course == null)
                throw new ResourceNotFoundException("Курс  с идентификатором " + clientDTO.getCourse() + " не найден.");
            client.setCourse(course);
            historyCourse = new BishHistory();
            historyCourse.setFullName(user.getName() + " " + user.getSurname());
            historyCourse.setClientPhone(client.getPhoneNo());
            historyCourse.setAction("Курс");
            historyCourse.setNewData(course.getName());
        }
        if (clientDTO.getUTM() != 0) {
            BishUTM utm = bishUTMRepo.findById(clientDTO.getUTM())
                    .orElseThrow(() -> new ResourceNotFoundException("UTM-метка с идентификатором " + clientDTO.getUTM() + " не найдена."));
            client.setUtm(utm);
            historyUTM = new BishHistory();
            historyUTM.setFullName(user.getName() + " " + user.getSurname());
            historyUTM.setClientPhone(client.getPhoneNo());
            historyUTM.setAction("UTM-метка");
            historyUTM.setNewData(utm.getName());
        }

        if (client.getStatus().getID() == 7)
            client.setTimer(null);
        else if (clientDTO.getTimer() == null)
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
                .orElseThrow(() -> new ResourceNotFoundException("Клиент с идентификатором  " + id + " не найден."));
    }

    @Override
    public BishClient changeStatus(long id, long status, String username) {
        User user = userService.findByEmail(username);

        BishClient client = getClientById(id);

        BishHistory bishHistory = new BishHistory();
        bishHistory.setAction("Статус");
        if (client.getStatus() == null)
            bishHistory.setOldData(null);
        else
            bishHistory.setOldData(client.getStatus().getName());
        bishHistory.setFullName(user.getName() + " " + user.getSurname());
        bishHistory.setClientPhone(client.getPhoneNo());

        BishStatuses statuses = bishStatusesRepo.findById(status)
                .orElseThrow(() -> new ResourceNotFoundException("Статус с идентификатором " + id + " не найден."));
        bishHistory.setNewData(statuses.getName());

        client.setStatus(statuses);

        if (client.getStatus().getID() == 7)
            client.setTimer(null);
        else if (client.getTimer() == null)
            client.setTimer(LocalDateTime.now().plusHours(24L));
        else if (!client.getTimer().isAfter(LocalDateTime.now().plusHours(24L))){
            client.setTimer(LocalDateTime.now().plusHours(24L));
        }

        client = bishClientRepo.save(client);

        bishHistoryService.create(bishHistory);
        return client;
    }

    @Override
    public BishClient updateClient(long id, ClientDTO clientDTO, String username) {
        User user = userService.findByEmail(username);
        BishClient client = getClientById(id);

        BishHistory historyStatus = null, historyOccupation = null, historyCourse = null, historyUTM = null;
        if (clientDTO.getStatus() != 0) {
            BishStatuses statuses = bishStatusesRepo.findById(clientDTO.getStatus())
                    .orElseThrow(() -> new ResourceNotFoundException("Статус с идентификатором " + clientDTO.getStatus() + " не найден."));

            historyStatus = new BishHistory();
            historyStatus.setFullName(user.getName() + " " + user.getSurname());
            historyStatus.setClientPhone(client.getPhoneNo());
            historyStatus.setAction("Статус");
            BishStatuses oldStatus = client.getStatus();
            if (oldStatus != null)
                historyStatus.setOldData(oldStatus.getName());
            historyStatus.setNewData(statuses.getName());
            client.setStatus(statuses);
        }
        if (clientDTO.getOccupation() != 0) {
            BishOccupation occupation = bishOccupationRepo.findById(clientDTO.getOccupation())
                    .orElseThrow(() -> new ResourceNotFoundException("Деятельность с идентификатором " + clientDTO.getOccupation() + " не найдена."));
            historyOccupation = new BishHistory();
            historyOccupation.setFullName(user.getName() + " " + user.getSurname());
            historyOccupation.setClientPhone(client.getPhoneNo());
            historyOccupation.setAction("Деятельность");
            BishOccupation oldOccupation = client.getOccupation();
            if (oldOccupation != null)
                historyOccupation.setOldData(oldOccupation.getName());
            historyOccupation.setNewData(occupation.getName());
            client.setOccupation(occupation);
        }
        if (clientDTO.getCourse() != 0) {
            BishCourses course = coursesService.findCourseById(clientDTO.getCourse());
            historyCourse = new BishHistory();
            historyCourse.setFullName(user.getName() + " " + user.getSurname());
            historyCourse.setClientPhone(client.getPhoneNo());
            historyCourse.setAction("Курс");
            BishCourses oldCourse = client.getCourse();
            if (oldCourse != null)
                historyCourse.setOldData(oldCourse.getName());
            historyCourse.setNewData(course.getName());
            client.setCourse(course);
        }
        if (clientDTO.getUTM() != 0) {
            BishUTM utm = bishUTMRepo.findById(clientDTO.getUTM())
                    .orElseThrow(() -> new ResourceNotFoundException("UTM-метка с идентификатором " + clientDTO.getUTM() + " не найдена."));
            historyUTM = new BishHistory();
            historyUTM.setFullName(user.getName() + " " + user.getSurname());
            historyUTM.setClientPhone(client.getPhoneNo());
            historyUTM.setAction("UTM-метка");
            BishUTM oldUTM = client.getUtm();
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
        client.setCity("BISHKEK");
        client.setPrepayment(clientDTO.getPrepayment());
        if (clientDTO.getTarget() != 0)
            client.setTarget(bishTargetRepo.findById(clientDTO.getTarget())
                    .orElseThrow(() -> new ResourceNotFoundException("Цель с идентификатором " + clientDTO.getTarget() + " не найдена.")));
        if (clientDTO.getLeavingReason() != 0)
            client.setLeavingReason(bishLeavingReasonRepo.findById(clientDTO.getLeavingReason())
                    .orElseThrow(() -> new ResourceNotFoundException("Причина с идентификатором " + clientDTO.getLeavingReason() + " не найдена.")));

        if (client.getStatus().getID() == 7)
            client.setTimer(null);
        else if (client.getTimer() == null && clientDTO.getTimer() == null)
            client.setTimer(LocalDateTime.now().plusHours(24L));
        else if (client.getTimer() == null)
            client.setTimer(clientDTO.getTimer());
        else if (!client.getTimer().isAfter(LocalDateTime.now().plusHours(24L))){
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
        BishClient bishClient = getClientById(id);

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
        oshClient.setTimer(LocalDateTime.now().plusHours(24L));
        oshClient.setPrepayment(bishClient.getPrepayment());
        if (bishClient.getLeavingReason() != null)
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
        history.setAction("Город");
        history.setClientPhone(oshClient.getPhoneNo());
        history.setOldData("Бишкек");
        history.setNewData("Ош");
        bishHistoryService.create(history);
    }

    @Override
    public Set<Object> simpleSearch(String nameOrPhone) {
        Set<Object> clients = new HashSet<>();
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
        for (String item : nameOrPhone.split(" ")) {
            clients.addAll(bishClientRepo.findAllByNameContainingIgnoringCase(item));
            clients.addAll(bishClientRepo.findAllBySurnameContainingIgnoringCase(item));
        }
        clients.addAll(bishClientRepo.findAllByPhoneNoContaining(nameOrPhone));
        clients.addAll(bishClientRepo.findAllByEmailContainingIgnoringCase(nameOrPhone));

        return clients;
    }

    @Override
    public BishClient addNewPayment(long clientID, PaymentDTO paymentDTO, String userEmail) {
        User user = userService.findByEmail(userEmail);

        BishClient client = this.getClientById(clientID);
        List<BishPayment> payments = client.getPayments();
        BishPayment payment = new BishPayment();
        for (BishPayment payment1 : payments) {
            if (payment1.getMonth().equals(paymentDTO.getMonth()))
                throw new IllegalArgumentException("За месяц " + paymentDTO.getMonth() +
                        " оплата уже существует. Воспользуйтесь методом для изменения данных платежа.");
        }
        payment.setMonth(paymentDTO.getMonth());
        payment.setPrice(paymentDTO.getPrice());
        payment.setDone(paymentDTO.isDone());
        BishCourses course = client.getCourse();
        if (course == null)
            throw new IllegalArgumentException("Запишите студента на курс перед добавлением оплаты.");
        payment.setCourse(course);
        if (paymentDTO.getMethodID() != 0)
            payment.setMethod(bishMethodRepo.findById(paymentDTO.getMethodID()).orElseThrow(() ->
                new ResourceNotFoundException("Метод с идентификатором " + paymentDTO.getMethodID() + " не найден.")));
        payments.add(bishPaymentRepo.save(payment));
        client.setPayments(payments);
        client = bishClientRepo.save(client);

        BishHistory history = new BishHistory();
        history.setFullName(user.getName() + " " + user.getSurname());
        history.setClientPhone(client.getPhoneNo());
        history.setAction("Добавление оплаты");
        history.setNewData(course.getName() + " " + payment.getMonth());
        bishHistoryService.create(history);
        return client;
    }

    @Override
    public BishClient editPayment(long clientID, PaymentDTO paymentDTO, long paymentID, String userEmail) {
        User user = userService.findByEmail(userEmail);

        BishClient client = this.getClientById(clientID);
        List<BishPayment> payments = client.getPayments();
        BishPayment payment = bishPaymentRepo.findById(paymentID)
                .orElseThrow(() -> new ResourceNotFoundException("Оплата с идентификатором " + paymentID + " не найдена."));
        if (!payments.contains(payment))
            throw new ResourceNotFoundException("Оплата с идентификатором " + paymentID + " не находится на карточке данного студента.");

        payment.setMonth(paymentDTO.getMonth());
        payment.setPrice(paymentDTO.getPrice());
        payment.setDone(paymentDTO.isDone());
        BishCourses course = client.getCourse();
        if (course == null)
            throw new IllegalArgumentException("Запишите студента на курс перед добавлением оплаты.");
        payment.setCourse(course);
        if (paymentDTO.getMethodID() != 0)
            payment.setMethod(bishMethodRepo.findById(paymentDTO.getMethodID()).orElseThrow(() ->
                new ResourceNotFoundException("Метод с идентификатором " + paymentDTO.getMethodID() + " не найден.")));
        bishPaymentRepo.save(payment);

        BishHistory history = new BishHistory();
        history.setFullName(user.getName() + " " + user.getSurname());
        history.setClientPhone(client.getPhoneNo());
        history.setAction("Изменение оплаты");
        history.setNewData(course.getName() + " " + payment.getMonth());
        bishHistoryService.create(history);

        return client;
    }

    @Override
    public ResponseMessage deletePayment(long clientID, long paymentID, String userEmail) {
        User user = userService.findByEmail(userEmail);

        BishClient client = this.getClientById(clientID);
        List<BishPayment> payments = client.getPayments();
        BishPayment payment = bishPaymentRepo.findById(paymentID)
                .orElseThrow(() -> new ResourceNotFoundException("Оплата с идентификатором " + paymentID + " не найдена."));
        if (!payments.contains(payment))
            throw new ResourceNotFoundException("Оплата с идентификатором " + paymentID + " не находится на карточке данного студента.");

        bishPaymentRepo.delete(payment);
        payments.remove(payment);
        client.setPayments(payments);
        bishClientRepo.save(client);

        BishHistory history = new BishHistory();
        history.setFullName(user.getName() + " " + user.getSurname());
        history.setClientPhone(client.getPhoneNo());
        history.setAction("Удаление оплаты");
        history.setNewData(payment.getCourse().getName() + " " + payment.getMonth());
        bishHistoryService.create(history);

        return new ResponseMessage("Оплата с идентификатором " + paymentID + " данного студента  удалена.");
    }

    @Override
    public ResponseMessage deleteClient(long clientID, String userEmail) {
        User user = userService.findByEmail(userEmail);

        BishClient client = this.getClientById(clientID);
        for (BishPayment payment : client.getPayments())
            bishPaymentRepo.delete(payment);
        bishClientRepo.delete(client);

        BishHistory history = new BishHistory();
        history.setFullName(user.getName() + " " + user.getSurname());
        history.setClientPhone(client.getPhoneNo());
        history.setAction("Удаление клиента");
        bishHistoryService.create(history);

        return new ResponseMessage("Студент с идентификатором " + clientID + " удален");
    }

    @Override
    public BishClient parseClient(String data) {
        BishClient client = new BishClient();
        String[] properties = data.split("&");
        for (String property : properties) {
            List<String> keyValue = Arrays.asList(property.split("="));
            switch (keyValue.get(0)) {
                case "data[client][phone]" :
                    client.setPhoneNo(keyValue.get(1));
                    break;
                case "data[client][email]" :
                    if (keyValue.size() != 1)
                        client.setEmail(keyValue.get(1));
                    break;
                case "data[form_name]" :
                    BishCourses course = coursesService.findCourseByFormName(keyValue.get(1));
                    client.setCourse(course);
                    client.setFormName(keyValue.get(1));
                    break;
                case "data[form_data][f27b7c6e2][value]" :
                    BishTarget target = bishTargetRepo.findByNameContainingIgnoringCase(keyValue.get(1));
                    if (target == null) {
                        oshTargetRepo.save(new OshTarget(keyValue.get(1)));
                        target = bishTargetRepo.save(new BishTarget(keyValue.get(1)));
                    }
                    client.setTarget(target);
                    break;
                case "data[form_data][f39494f11][value]" :
                    BishOccupation occupation = bishOccupationRepo.findByNameContainingIgnoringCase(keyValue.get(1));
                    if (occupation == null) {
                        oshOccupationRepo.save(new OshOccupation(keyValue.get(1)));
                        occupation = bishOccupationRepo.save(new BishOccupation(keyValue.get(1)));
                    }
                    client.setOccupation(occupation);
                    break;
                case "data[form_data][f8d5336ff][name]" :
                    client.setExperience(keyValue.get(1).equals("Да"));
                    break;
                case  "data[utm][url]" :
                    BishUTM utm;
                    String utmName = keyValue.get(1);
                    if (utmName.contains("instagram")) {
                        utm = bishUTMRepo.findByNameContainingIgnoringCase("Instagram").orElse(null);
                        if (utm == null) {
                            oshUTMRepo.save(new OshUTM("Instagram"));
                            utm = bishUTMRepo.save(new BishUTM("Instagram"));
                        }
                        client.setUtm(utm);
                    } else if (utmName.contains("facebook")) {
                        utm = bishUTMRepo.findByNameContainingIgnoringCase("Facebook").orElse(null);
                        if (utm == null) {
                            oshUTMRepo.save(new OshUTM("Facebook"));
                            utm = bishUTMRepo.save(new BishUTM("Facebook"));
                        }
                        client.setUtm(utm);
                    } else if (utmName.contains("google")) {
                        utm = bishUTMRepo.findByNameContainingIgnoringCase("Google").orElse(null);
                        if (utm == null) {
                            oshUTMRepo.save(new OshUTM("Google"));
                            utm = bishUTMRepo.save(new BishUTM("Google"));
                        }
                        client.setUtm(utm);
                    } else if (utmName.contains("neobis")) {
                        utm = bishUTMRepo.findByNameContainingIgnoringCase("Neobis").orElse(null);
                        if (utm == null) {
                            oshUTMRepo.save(new OshUTM("Neobis"));
                            utm = bishUTMRepo.save(new BishUTM("Neobis"));
                        }
                        client.setUtm(utm);
                    }
                    break;
                case "data[form_data][1000][value]" :
                    client.setName(keyValue.get(1));
                    break;
                case "data[form_data][481576][value]" :
                    BishCourses courses = coursesService.findCourseByFormName(keyValue.get(1));
                    client.setCourse(courses);
                    break;
            }
            client.setCity("Bishkek");
            client.setTimer(LocalDateTime.now().plusHours(24L));
            client.setStatus(bishStatusesRepo.findById(1L).orElse(null));
        }
        return client;
    }

    @Override
    public List<BishClient> getClientsWithExpiredTimer() {
        return bishClientRepo.findAllByTimerBeforeOrderByTimerAsc(LocalDateTime.now());
    }

    @Override
    public BishClient updateTimer(String userEmail, long clientID, LocalDateTime timer) {
        BishClient client = getClientById(clientID);
        client.setTimer(timer);
        client = bishClientRepo.save(client);

        User user = userService.findByEmail(userEmail);
        BishHistory history = new BishHistory();
        history.setFullName(user.getName() + " " + user.getSurname());
        history.setClientPhone(client.getPhoneNo());
        history.setAction("Обновление даты таймера");
        history.setNewData(timer.toString());
        bishHistoryService.create(history);
        return client;
    }
}
