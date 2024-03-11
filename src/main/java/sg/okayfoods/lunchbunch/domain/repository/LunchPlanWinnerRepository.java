package sg.okayfoods.lunchbunch.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.okayfoods.lunchbunch.domain.entity.LunchPlanWinner;

public interface LunchPlanWinnerRepository extends JpaRepository<LunchPlanWinner, Long> {
}
