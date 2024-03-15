package sg.okayfoods.lunchbunch.infrastracture.adapter.dto.auth;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class LoginResponse {
    private String accessToken;
    private Long expiresAt;
    private String email;
    private Long id;
    private String role;

}
