package sg.okayfoods.lunchbunch.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Getter
@Setter
@Table("user")
public class AppUserCassandra {
    @PrimaryKey
    private UUID id;

    private String email;
    private String name;
    private String password;
    private String status;


}
