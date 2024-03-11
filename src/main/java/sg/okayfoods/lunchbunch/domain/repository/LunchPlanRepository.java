package sg.okayfoods.lunchbunch.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.okayfoods.lunchbunch.domain.entity.LunchPlan;

import java.util.Optional;

public interface LunchPlanRepository extends JpaRepository<LunchPlan, Long> {

    Optional<LunchPlan> findByUuid(String uuid);
}
