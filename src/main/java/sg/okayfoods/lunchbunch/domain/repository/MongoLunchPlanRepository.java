package sg.okayfoods.lunchbunch.domain.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import sg.okayfoods.lunchbunch.domain.entity.LunchPlanMongo;

public interface MongoLunchPlanRepository extends MongoRepository<LunchPlanMongo, String> {
}
