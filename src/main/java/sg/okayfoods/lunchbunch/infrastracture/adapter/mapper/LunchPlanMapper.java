package sg.okayfoods.lunchbunch.infrastracture.adapter.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sg.okayfoods.lunchbunch.domain.entity.LunchPlan;
import sg.okayfoods.lunchbunch.domain.entity.LunchPlanSuggestion;
import sg.okayfoods.lunchbunch.domain.entity.LunchPlanWinner;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.lunchplan.LunchPlanDetailedResponseDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.lunchplan.LunchPlanRequestDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.lunchplan.LunchPlanResponseDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.response.LunchPlanWinnerResponseDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.response.SuggestionResponseDTO;

@Mapper(componentModel = "spring")
public interface LunchPlanMapper {
    @Mapping(target = "uuid", expression = "java(java.util.UUID.randomUUID().toString().replace(\"-\",\"\"))")
    LunchPlan map(LunchPlanRequestDTO lunchPlanRequestDTO);

    @Mapping(target = "winner", source="lunchPlan.lunchPlanWinner")
    @Mapping(target = "createdAt",source = "createdAt")

    LunchPlanResponseDTO map(LunchPlan lunchPlan);

    @Mapping(target = "id", source = "lunchPlan.id")
    @Mapping(target = "uuid", source="lunchPlan.uuid")
    @Mapping(target = "date", source="lunchPlan.date")
    @Mapping(target = "description", source="lunchPlan.description")
    @Mapping(target = "suggestions", source="lunchPlan.lunchPlanSuggestions")
    @Mapping(target="initiator", source = "lunchPlan.initiatedBy.name")
    @Mapping(target = "winner", source="lunchPlan.lunchPlanWinner")
    LunchPlanDetailedResponseDTO mapDetailed(LunchPlan lunchPlan);


    @Mapping(target = "restaurant", source = "restaurantName")
    SuggestionResponseDTO map(LunchPlanSuggestion suggestion);

    @Mapping(target = "restaurant",source ="lunchPlanSuggestion.restaurantName" )
    @Mapping(target = "suggestedBy", source="lunchPlanSuggestion.suggestedBy")
    @Mapping(target = "date", source = "datePick")
    LunchPlanWinnerResponseDTO map(LunchPlanWinner winner);




}
