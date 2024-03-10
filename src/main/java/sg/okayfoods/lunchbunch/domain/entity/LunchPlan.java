package sg.okayfoods.lunchbunch.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="lunch_plan")
public class LunchPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="uuid")
    private String uuid;

    @JoinColumn(name="initiated_by")
    @ManyToOne
    private AppUser initiatedBy;

    @Column(name="date")
    private LocalDate date;

    @Column(name="description")
    private String description;

    @Column(name="created_at")
    private LocalDateTime createdAt;

}
