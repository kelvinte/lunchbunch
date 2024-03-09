package sg.okayfoods.lunchbunch.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import sg.okayfoods.lunchbunch.common.constant.UserStatus;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "app_user")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="name")
    private String name;


    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @OneToOne
    @JoinColumn(name = "app_role_id")
    private AppRole appRole;


}
