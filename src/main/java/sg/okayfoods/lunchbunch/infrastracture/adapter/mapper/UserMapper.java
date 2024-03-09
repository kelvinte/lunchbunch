package sg.okayfoods.lunchbunch.infrastracture.adapter.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sg.okayfoods.lunchbunch.domain.entity.AppUser;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.auth.LoginResponse;

@Mapper(componentModel = "spring")

public interface UserMapper {

    @Mapping(target = "accessToken", source = "accessToken")
    @Mapping(target = "expiresAt", source = "expiresAt")
    @Mapping(target = "role", source = "user.appRole.name")
    LoginResponse map(AppUser user, String accessToken, Long expiresAt);
}
