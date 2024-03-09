package sg.okayfoods.lunchbunch.application;

import org.springframework.stereotype.Service;
import sg.okayfoods.lunchbunch.domain.repository.LunchPlanRepository;

@Service
public class LunchPlanService {



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
