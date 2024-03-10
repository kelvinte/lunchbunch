package sg.okayfoods.lunchbunch.infrastracture.adapter.incoming;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import sg.okayfoods.lunchbunch.application.LunchPlanService;
import sg.okayfoods.lunchbunch.application.LunchPlanSuggestionService;
import sg.okayfoods.lunchbunch.common.constant.UrlConstants;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.lunchplan.LunchPlanSuggestionRequestDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.lunchplan.LunchPlanSuggestionResponseDTO;

@RestController
@RequestMapping(UrlConstants.SUGGESTION)
public class LunchPlanSuggestionController {

    private LunchPlanSuggestionService lunchPlanSuggestionService;

    public LunchPlanSuggestionController(LunchPlanSuggestionService lunchPlanSuggestionService) {
        this.lunchPlanSuggestionService = lunchPlanSuggestionService;
    }

    @PostMapping
    public void createSuggestion(@PathVariable String uuid,@RequestBody @Valid LunchPlanSuggestionRequestDTO request){
        lunchPlanSuggestionService.create(uuid, request);
    }
}
