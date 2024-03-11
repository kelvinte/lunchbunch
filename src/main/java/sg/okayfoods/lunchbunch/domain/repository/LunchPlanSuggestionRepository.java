package sg.okayfoods.lunchbunch.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.okayfoods.lunchbunch.domain.entity.LunchPlanSuggestion;

import java.util.List;

public interface LunchPlanSuggestionRepository extends JpaRepository<LunchPlanSuggestion, Long> {
    List<LunchPlanSuggestion> findByLunchPlanId(Long lunchPlanId);

    List<LunchPlanSuggestion> findByLunchPlanUuid(String uuid);
}
