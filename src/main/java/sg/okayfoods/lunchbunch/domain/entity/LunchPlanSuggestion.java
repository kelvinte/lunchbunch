package sg.okayfoods.lunchbunch.domain.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="lunch_plan_suggestion")
public class LunchPlanSuggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="restaurant_name")
    private String restaurantName;

    @JoinColumn(name="lunch_plan_id")
    @ManyToOne
    private LunchPlan lunchPlan;

    @Column(name="suggested_by")
    private String suggestedBy;
}
