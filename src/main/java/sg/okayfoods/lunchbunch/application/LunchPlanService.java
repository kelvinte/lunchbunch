package sg.okayfoods.lunchbunch.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.okayfoods.lunchbunch.common.constant.ErrorCode;
import sg.okayfoods.lunchbunch.common.exception.AppException;
import sg.okayfoods.lunchbunch.common.util.DateTimeUtils;
import sg.okayfoods.lunchbunch.domain.entity.AppUser;
import sg.okayfoods.lunchbunch.domain.entity.LunchPlan;
import sg.okayfoods.lunchbunch.domain.entity.LunchPlanWinner;
import sg.okayfoods.lunchbunch.domain.repository.LunchPlanRepository;
import sg.okayfoods.lunchbunch.domain.repository.LunchPlanSuggestionRepository;
import sg.okayfoods.lunchbunch.domain.repository.LunchPlanWinnerRepository;
import sg.okayfoods.lunchbunch.domain.user.LoggedInUser;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.lunchplan.LunchPlanDetailedResponseDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.lunchplan.LunchPlanRequestDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.lunchplan.LunchPlanResponseDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.response.LunchPlanWinnerResponseDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.websocket.response.SuggestionResponseDTO;
import sg.okayfoods.lunchbunch.infrastracture.adapter.mapper.LunchPlanMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
public class LunchPlanService {


    private LunchPlanRepository lunchPlanRepository;
    private LunchPlanWinnerRepository lunchPlanWinnerRepository;
    private LunchPlanMapper lunchPlanMapper;

    private LoggedInUserService loggedInUserService;
    public LunchPlanService(LunchPlanRepository lunchPlanRepository,
                            LunchPlanWinnerRepository lunchPlanWinnerRepository,
                            LunchPlanMapper lunchPlanMapper,
                            LoggedInUserService loggedInUserService) {
        this.lunchPlanRepository = lunchPlanRepository;
        this.lunchPlanWinnerRepository = lunchPlanWinnerRepository;
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

        boolean isOwner = false;
        var loggedInUser = loggedInUserService.getLoggedInUser();
        if(loggedInUser!=null){
            isOwner = loggedInUser.getId() .equals( lunchPlan.getInitiatedBy().getId());
        }
        var lunchMap = lunchPlanMapper.mapDetailed(lunchPlan);
        lunchMap.setOwner(isOwner);
        return lunchMap;
    }

    @Transactional(readOnly = false)
    public LunchPlanWinnerResponseDTO end(String uuid){
        LunchPlan lunchPlan = lunchPlanRepository.findByUuid(uuid).orElseThrow(()->new AppException(ErrorCode.NOT_EXISTING));

        if(lunchPlan.isEnded()){
            throw new AppException(ErrorCode.LUNCH_PLAN_ENDED_ALREADY);
        }

        lunchPlan.setEnded(true);
        lunchPlanRepository.save(lunchPlan);


        var suggestions = lunchPlan.getLunchPlanSuggestions();

        Random random = new Random();
        var winner = random.nextInt(suggestions.size());

        var winnerSuggestion = suggestions.get(winner);

        LunchPlanWinner lunchPlanWinner = LunchPlanWinner.builder()
                .lunchPlanSuggestion(winnerSuggestion)
                .lunchPlan(lunchPlan)
                .datePick(LocalDateTime.now())
                .build();

        lunchPlanWinnerRepository.save(lunchPlanWinner);

        return lunchPlanMapper.map(lunchPlanWinner);

    }



}
