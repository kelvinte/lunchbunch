package sg.okayfoods.lunchbunch.application;

import org.springframework.stereotype.Service;
import sg.okayfoods.lunchbunch.domain.entity.LunchPlan;
import sg.okayfoods.lunchbunch.domain.entity.LunchPlanSuggestion;
import sg.okayfoods.lunchbunch.domain.repository.LunchPlanRepository;
import sg.okayfoods.lunchbunch.domain.repository.LunchPlanSuggestionRepository;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.lunchplan.LunchPlanSuggestionRequestDTO;

@Service
public class LunchPlanSuggestionService {

    private LunchPlanRepository lunchPlanRepository;
    private LunchPlanSuggestionRepository lunchPlanSuggestionRepository;

    public LunchPlanSuggestionService(LunchPlanRepository lunchPlanRepository,
                                      LunchPlanSuggestionRepository lunchPlanSuggestionRepository) {
        this.lunchPlanRepository = lunchPlanRepository;
        this.lunchPlanSuggestionRepository = lunchPlanSuggestionRepository;
    }


    public void create(String uuid, LunchPlanSuggestionRequestDTO requestDTO){

        LunchPlan lunchPlan = lunchPlanRepository.findByUuid(uuid);
        LunchPlanSuggestion lunchPlanSuggestion = LunchPlanSuggestion.builder()
                .lunchPlan(lunchPlan)
                .restaurantName(requestDTO.getRestaurantName())
                .suggestedBy(requestDTO.getSuggestedBy())
                .build();

        lunchPlanSuggestionRepository.save(lunchPlanSuggestion);


    }
}
