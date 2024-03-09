package sg.okayfoods.lunchbunch.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.okayfoods.lunchbunch.domain.entity.LunchPlan;

public interface LunchPlanRepository extends JpaRepository<LunchPlan, Long> {
}
