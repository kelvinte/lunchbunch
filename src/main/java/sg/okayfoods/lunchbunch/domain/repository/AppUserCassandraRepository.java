package sg.okayfoods.lunchbunch.domain.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import sg.okayfoods.lunchbunch.domain.entity.AppUserCassandra;

import java.util.UUID;

public interface AppUserCassandraRepository extends CassandraRepository<AppUserCassandra, UUID> {
}
