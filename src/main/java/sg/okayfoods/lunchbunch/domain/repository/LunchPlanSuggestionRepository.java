package sg.okayfoods.lunchbunch.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.okayfoods.lunchbunch.domain.entity.LunchPlanSuggestion;

public interface LunchPlanSuggestionRepository extends JpaRepository<LunchPlanSuggestion, Long> {
}
