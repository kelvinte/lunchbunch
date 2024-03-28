package sg.okayfoods.lunchbunch.domain.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("lunchPlan")
public class LunchPlanMongo {

    @Id
    private String id;

    private String uuid;

    private AppUser initiatedBy;

    private LocalDate date;

    private String description;
    private LocalDateTime createdAt;

    private boolean ended;

    @Version
    private Long version;

}
