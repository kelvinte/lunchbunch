package sg.okayfoods.lunchbunch.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "lunch_plan_winner")
public class LunchPlanWinner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @OneToOne
    @JoinColumn(name = "lunch_plan_id")
    private LunchPlan lunchPlan;
    @OneToOne
    @JoinColumn(name = "lunch_plan_suggestion_Id")
    private LunchPlanSuggestion lunchPlanSuggestion;

    @Column(name="datetime_pick")
    private LocalDateTime datePick;

}
