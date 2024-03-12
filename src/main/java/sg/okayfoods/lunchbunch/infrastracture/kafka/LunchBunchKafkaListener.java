package sg.okayfoods.lunchbunch.infrastracture.kafka;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class LunchBunchKafkaListener {
    @KafkaListener(topics = "lunchbunchonepartition")
    public void processMessage(String content) {
        System.out.println(content);
    }
}
