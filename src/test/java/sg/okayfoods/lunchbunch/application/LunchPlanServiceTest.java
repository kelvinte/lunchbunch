package sg.okayfoods.lunchbunch.application;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import sg.okayfoods.lunchbunch.common.constant.UserStatus;
import sg.okayfoods.lunchbunch.domain.entity.*;
import sg.okayfoods.lunchbunch.domain.repository.AppUserRepository;
import sg.okayfoods.lunchbunch.domain.repository.LunchPlanRepository;
import sg.okayfoods.lunchbunch.domain.repository.LunchPlanWinnerRepository;
import sg.okayfoods.lunchbunch.domain.user.LoggedInUser;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.lunchplan.LunchPlanRequestDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.mapper.LunchPlanMapper;
import sg.okayfoods.lunchbunch.infrastracture.adapter.mapper.LunchPlanMapperImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LunchPlanServiceTest {

    @Mock
    private LunchPlanRepository lunchPlanRepository;
    @Mock
    private LunchPlanWinnerRepository lunchPlanWinnerRepository;
    @Mock
    private LunchPlanMapperImpl lunchPlanMapper;
    @Mock
    private LoggedInUserService loggedInUserService;

    @InjectMocks
    private LunchPlanService lunchPlanService;
    @Test
    void end() {
        LunchPlanSuggestion lunchPlanSuggestion = LunchPlanSuggestion.builder()
                .suggestedBy("Me")
                .restaurantName("lau pa sat")
                .build();
        LunchPlan sample = LunchPlan.builder()
                .uuid("test")
                .description("birthday")
                .date(LocalDate.now())
                .ended(false)
                .lunchPlanSuggestions(List.of(lunchPlanSuggestion))
                .build();
        when(lunchPlanRepository.findByUuid(anyString())).thenReturn(Optional.of(sample));

        when(lunchPlanWinnerRepository.save(any(LunchPlanWinner.class))).then(i->i.getArguments()[0]);
        when(lunchPlanMapper.map(any(LunchPlanWinner.class))).thenCallRealMethod();

        lunchPlanService.end("asdf");

        ArgumentCaptor<LunchPlan> captor = ArgumentCaptor.forClass(LunchPlan.class);
        Mockito.verify(lunchPlanRepository).save(captor.capture());

        assertTrue(captor.getValue().isEnded());

        ArgumentCaptor<LunchPlanWinner> captorWinner = ArgumentCaptor.forClass(LunchPlanWinner.class);
        Mockito.verify(lunchPlanWinnerRepository).save(captorWinner.capture());

        assertEquals("lau pa sat", captorWinner.getValue().getLunchPlanSuggestion().getRestaurantName());

    }
}