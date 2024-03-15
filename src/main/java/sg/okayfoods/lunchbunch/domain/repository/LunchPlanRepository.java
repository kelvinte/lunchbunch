package sg.okayfoods.lunchbunch.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import sg.okayfoods.lunchbunch.domain.entity.LunchPlan;

import java.util.Optional;

public interface LunchPlanRepository extends PagingAndSortingRepository<LunchPlan, Long>,JpaRepository<LunchPlan, Long> {

    Optional<LunchPlan> findByUuid(String uuid);
    @Query("Select l from LunchPlan l where l.initiatedBy.id = :userId order by l.createdAt desc")

    Page<LunchPlan> findByInitiatedBy(@Param("userId") Long userId, Pageable pageable);
}
