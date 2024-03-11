package sg.okayfoods.lunchbunch.application;

import org.springframework.stereotype.Service;
import sg.okayfoods.lunchbunch.common.constant.ErrorCode;
import sg.okayfoods.lunchbunch.common.exception.AppException;
import sg.okayfoods.lunchbunch.common.util.DateTimeUtils;
import sg.okayfoods.lunchbunch.domain.entity.AppUser;
import sg.okayfoods.lunchbunch.domain.entity.LunchPlan;
import sg.okayfoods.lunchbunch.domain.repository.LunchPlanRepository;
import sg.okayfoods.lunchbunch.domain.repository.LunchPlanSuggestionRepository;
import sg.okayfoods.lunchbunch.domain.user.LoggedInUser;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.lunchplan.LunchPlanDetailedResponseDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.lunchplan.LunchPlanRequestDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.lunchplan.LunchPlanResponseDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.mapper.LunchPlanMapper;

import java.util.UUID;

@Service
public class LunchPlanService {


    private LunchPlanRepository lunchPlanRepository;
    private LunchPlanSuggestionRepository lunchPlanSuggestionRepository;
    private LunchPlanMapper lunchPlanMapper;

    private LoggedInUserService loggedInUserService;
    public LunchPlanService(LunchPlanRepository lunchPlanRepository,
                            LunchPlanSuggestionRepository lunchPlanSuggestionRepository,
                            LunchPlanMapper lunchPlanMapper,
                            LoggedInUserService loggedInUserService) {
        this.lunchPlanRepository = lunchPlanRepository;
        this.lunchPlanSuggestionRepository = lunchPlanSuggestionRepository;
        this.lunchPlanMapper = lunchPlanMapper;
        this.loggedInUserService = loggedInUserService;
    }

    public LunchPlanResponseDTO create(LunchPlanRequestDTO request){

        LoggedInUser loggedInUser = loggedInUserService.getLoggedInUser();
        AppUser appUser = new AppUser();
        appUser.setId(loggedInUser.getId());
        LunchPlan lunchPlan = LunchPlan.builder()
                .uuid(UUID.randomUUID().toString().replace("-",""))
                .description(request.getDescription())
                .date(DateTimeUtils.convert(request.getDate()))
                .initiatedBy(appUser)
                .build();

        LunchPlan result = lunchPlanRepository.save(lunchPlan);
        return lunchPlanMapper.map(result);
    }

    public LunchPlanDetailedResponseDTO get(String uuid) {
        var lunchPlan = lunchPlanRepository.findByUuid(uuid).orElseThrow(()->new AppException(ErrorCode.NOT_EXISTING));

        if(lunchPlan.isEnded()){
            throw new AppException(ErrorCode.LUNCH_PLAN_ENDED_ALREADY);
        }

        var lunchPlanSuggestions = lunchPlanSuggestionRepository.findByLunchPlanId(lunchPlan.getId());

        boolean isOwner = false;
        var loggedInUser = loggedInUserService.getLoggedInUser();
        if(loggedInUser!=null){
            isOwner = loggedInUser.getId() .equals( lunchPlan.getInitiatedBy().getId());
        }
        var lunchMap = lunchPlanMapper.map(lunchPlan, lunchPlanSuggestions);
        lunchMap.setOwner(isOwner);
        return lunchMap;
    }

    public void end(String uuid){
        LunchPlan lunchPlan = lunchPlanRepository.findByUuid(uuid).orElseThrow(()->new AppException(ErrorCode.NOT_EXISTING));

        if(lunchPlan.isEnded()){
            throw new AppException(ErrorCode.LUNCH_PLAN_ENDED_ALREADY);
        }

        lunchPlan.setEnded(true);
        lunchPlanRepository.save(lunchPlan);

    }

    //    private Map<Long, Sinks.Many<SessionRestaurant>> sinkMap = new ConcurrentHashMap<>();
//
//
//    private SessionRestaurantRepository sessionRestaurantRepository;
//
//    public SessionService(SessionRestaurantRepository sessionRestaurantRepository) {
//        this.sessionRestaurantRepository = sessionRestaurantRepository;
//    }
//
//
//
//    public Flux<SessionRestaurant> streamSessionRestaurant(Long sessionId){
//        var sink = sinkMap.get(sessionId);
//        if(sink==null){
//            // query
//            sink = Sinks.many().replay().all();
//            sinkMap.put(sessionId, sink);
//
//            sessionRestaurantRepository.findBySessionId(sessionId)
//                    .doOnNext(sink::tryEmitNext);
//            // and emit
//        }
//        return sink.asFlux();
//    }
//    public Mono<SessionRestaurant> saveSessionRestaurant(SessionRestaurantRequest request){
//        SessionRestaurant sessionRestaurant = SessionRestaurant.builder()
//                .sessionId(1L)
//                .restaurantName(request.getRestaurantName())
//                .suggestedBy(1L)
//                .build();
//        return sessionRestaurantRepository.save(sessionRestaurant)
//                .doOnNext(saved->{
//                    var sessionId = saved.getSessionId();
//                    var sink = sinkMap.getOrDefault(sessionId,Sinks.many().replay().all());
//                    sinkMap.put(sessionId, sink);
//                    sink.tryEmitNext(saved);
//                });
//    }




}
