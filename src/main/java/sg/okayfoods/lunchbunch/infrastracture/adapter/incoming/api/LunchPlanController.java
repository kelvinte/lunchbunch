package sg.okayfoods.lunchbunch.infrastracture.adapter.incoming.api;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import sg.okayfoods.lunchbunch.application.LunchPlanService;
import sg.okayfoods.lunchbunch.common.constant.UrlConstants;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.common.PaginatedResponse;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.lunchplan.LunchPlanDetailedResponseDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.lunchplan.LunchPlanRequestDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.lunchplan.LunchPlanResponseDTO;

@RestController
@RequestMapping(UrlConstants.LUNCH_PLAN)
public class LunchPlanController {

    private LunchPlanService lunchPlanService;

    public LunchPlanController(LunchPlanService lunchPlanService) {
        this.lunchPlanService = lunchPlanService;
    }

    @PostMapping
    public LunchPlanResponseDTO create(@RequestBody @Valid LunchPlanRequestDTO request){
        return lunchPlanService.create(request);
    }

    @GetMapping
    public PaginatedResponse<LunchPlanResponseDTO> getAll(@RequestParam(value = "page", defaultValue = "0") int page,
                                                          @RequestParam(value = "size", defaultValue = "10") int size
    ){
        return lunchPlanService.getAll(page, size);
    }

    @GetMapping("/{uuid}")
    public LunchPlanDetailedResponseDTO get(@PathVariable("uuid") String uuid){
        return lunchPlanService.get(uuid);
    }

}
