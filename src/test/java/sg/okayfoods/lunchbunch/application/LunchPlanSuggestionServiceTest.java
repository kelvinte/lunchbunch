package sg.okayfoods.lunchbunch.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sg.okayfoods.lunchbunch.common.exception.AppException;
import sg.okayfoods.lunchbunch.domain.entity.LunchPlan;
import sg.okayfoods.lunchbunch.domain.entity.LunchPlanSuggestion;
import sg.okayfoods.lunchbunch.domain.repository.LunchPlanRepository;
import sg.okayfoods.lunchbunch.domain.repository.LunchPlanSuggestionRepository;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.request.CreateSuggestionDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.mapper.LunchPlanMapper;
import sg.okayfoods.lunchbunch.infrastracture.adapter.mapper.LunchPlanMapperImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LunchPlanSuggestionServiceTest {

    @Mock
    LunchPlanRepository lunchPlanRepository;
    @Mock
    LunchPlanSuggestionRepository lunchPlanSuggestionRepository;

    @Mock
    LunchPlanMapperImpl lunchPlanMapper;
    @InjectMocks
    LunchPlanSuggestionService lunchPlanSuggestionService;
    @Test
    void validUuid_retrieve_shouldRetrieveSuggestion() {
        when(lunchPlanMapper.map(any(LunchPlanSuggestion.class))).thenCallRealMethod();
        LunchPlanSuggestion lunchPlanSuggestion = new LunchPlanSuggestion();
        lunchPlanSuggestion.setRestaurantName("some resto");
        lunchPlanSuggestion.setSuggestedBy("me");
        when(lunchPlanSuggestionRepository.findByLunchPlanUuid(anyString())).thenReturn(List.of(lunchPlanSuggestion));
        var result = lunchPlanSuggestionService.retrieve("asdf");
        assertEquals(1, result.size());
        assertEquals("some resto", result.get(0).getRestaurant());
    }

    @Test
    void lunchPlanEnded_createSuggestion_shouldThrowException() {
        LunchPlan lunchPlan = new LunchPlan();
        lunchPlan.setEnded(true);

        when(lunchPlanRepository.findByUuid(anyString())).thenReturn(Optional.of(lunchPlan));


        assertThrows(AppException.class, ()->{

            lunchPlanSuggestionService.create("asdf", CreateSuggestionDTO.builder().build());
        });
    }

    @Test
    void lunchPlanNotEnded_createSuggestion_shouldSave() {
        LunchPlan lunchPlan = new LunchPlan();
        lunchPlan.setEnded(false);

        when(lunchPlanRepository.findByUuid(anyString())).thenReturn(Optional.of(lunchPlan));

        when(lunchPlanSuggestionRepository.save(any(LunchPlanSuggestion.class)))
                .then(i->i.getArguments()[0]);

        lunchPlanSuggestionService.create("asdf", CreateSuggestionDTO.builder().restaurant("asdf").suggestedBy("asdf").build());

        ArgumentCaptor<LunchPlanSuggestion> lunchPlanSuggestionArgumentCaptor = ArgumentCaptor.forClass(LunchPlanSuggestion.class);
        verify(lunchPlanSuggestionRepository).save(lunchPlanSuggestionArgumentCaptor.capture());

        assertEquals("asdf",lunchPlanSuggestionArgumentCaptor.getValue().getRestaurantName());
    }

}