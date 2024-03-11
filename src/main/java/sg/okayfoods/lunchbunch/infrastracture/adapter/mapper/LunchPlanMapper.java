package sg.okayfoods.lunchbunch.infrastracture.adapter.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sg.okayfoods.lunchbunch.domain.entity.AppUser;
import sg.okayfoods.lunchbunch.domain.entity.LunchPlan;
import sg.okayfoods.lunchbunch.domain.entity.LunchPlanSuggestion;
import sg.okayfoods.lunchbunch.domain.user.LoggedInUser;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.auth.LoginResponse;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.lunchplan.LunchPlanDetailedResponseDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.lunchplan.LunchPlanRequestDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.lunchplan.LunchPlanResponseDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.response.SuggestionResponseDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LunchPlanMapper {
    @Mapping(target = "uuid", expression = "java(java.util.UUID.randomUUID().toString().replace(\"-\",\"\"))")
    LunchPlan map(LunchPlanRequestDTO lunchPlanRequestDTO);


    LunchPlanResponseDTO map(LunchPlan lunchPlan);

    @Mapping(target = "id", source = "lunchPlan.id")
    @Mapping(target = "uuid", source="lunchPlan.uuid")
    @Mapping(target = "date", source="lunchPlan.date")
    @Mapping(target = "description", source="lunchPlan.description")
    @Mapping(target = "suggestions", source="suggestions")
    @Mapping(target="initiator", source = "lunchPlan.initiatedBy.name")
    LunchPlanDetailedResponseDTO map(LunchPlan lunchPlan, List<LunchPlanSuggestion> suggestions);


    @Mapping(target = "restaurant", source = "restaurantName")
    SuggestionResponseDTO map(LunchPlanSuggestion suggestion);


}
