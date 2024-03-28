package sg.okayfoods.lunchbunch.application;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sg.okayfoods.lunchbunch.common.constant.ErrorCode;
import sg.okayfoods.lunchbunch.common.exception.AppException;
import sg.okayfoods.lunchbunch.domain.entity.AppUser;
import sg.okayfoods.lunchbunch.domain.entity.AppUserCassandra;
import sg.okayfoods.lunchbunch.domain.entity.LunchPlan;
import sg.okayfoods.lunchbunch.domain.entity.LunchPlanSuggestion;
import sg.okayfoods.lunchbunch.domain.repository.AppUserCassandraRepository;
import sg.okayfoods.lunchbunch.domain.repository.LunchPlanRepository;
import sg.okayfoods.lunchbunch.domain.repository.LunchPlanSuggestionRepository;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.request.CreateSuggestionDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.response.SuggestionResponseDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.mapper.LunchPlanMapper;

import java.util.List;
import java.util.UUID;

@Service
public class LunchPlanSuggestionService {

    private final LunchPlanRepository lunchPlanRepository;
    private final LunchPlanSuggestionRepository lunchPlanSuggestionRepository;

    private LunchPlanMapper lunchPlanMapper;
    public LunchPlanSuggestionService(LunchPlanRepository lunchPlanRepository,
                                      LunchPlanSuggestionRepository lunchPlanSuggestionRepository,
                                    LunchPlanMapper lunchPlanMapper) {
        this.lunchPlanRepository = lunchPlanRepository;
        this.lunchPlanSuggestionRepository = lunchPlanSuggestionRepository;
        this.lunchPlanMapper = lunchPlanMapper;
    }

    @Autowired
    AppUserCassandraRepository repository;
    @PostConstruct
    public void postConstruct(){
        AppUserCassandra appUserCassandra = new AppUserCassandra();
        appUserCassandra.setId(UUID.randomUUID());
        appUserCassandra.setName("Test");
        appUserCassandra.setEmail("kelvinclarkte@gmail.com");
        appUserCassandra.setPassword("asddf");
        appUserCassandra.setStatus("ok");
        repository.save(appUserCassandra);
    }

    public List<SuggestionResponseDTO> retrieve(String uuid){
        var result= lunchPlanSuggestionRepository.findByLunchPlanUuid(uuid);
        return result.stream().map(lunchPlanMapper::map).toList();
    }

    public void create(String uuid, CreateSuggestionDTO requestDTO){

        LunchPlan lunchPlan = lunchPlanRepository.findByUuid(uuid).orElseThrow(()->new AppException(ErrorCode.NOT_EXISTING));

        if(lunchPlan.isEnded()){
            throw new AppException(ErrorCode.LUNCH_PLAN_ENDED_ALREADY);
        }
        LunchPlanSuggestion lunchPlanSuggestion = LunchPlanSuggestion.builder()
                .lunchPlan(lunchPlan)
                .restaurantName(requestDTO.getRestaurant())
                .suggestedBy(requestDTO.getSuggestedBy())
                .build();

        lunchPlanSuggestionRepository.save(lunchPlanSuggestion);
    }
}
