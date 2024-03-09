package sg.okayfoods.lunchbunch.infrastracture.adapter.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class RegistrationRequest {

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid email")
    private String email;
    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    private String password;
    @NotEmpty(message = "Name cannot be empty")
    private String name;

}
