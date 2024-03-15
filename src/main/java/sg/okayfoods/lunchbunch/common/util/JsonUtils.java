package sg.okayfoods.lunchbunch.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonUtils {

    private JsonUtils() {
    }

    public static String toJson(Object object){
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(object);
        }catch (IOException e){
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static <T> T toObject(String jsonStr, Class<T> clazz) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonStr, clazz);
        }catch (IOException e){
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static <T> T toObject(Object obj , Class<T> clazz){
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(obj, clazz);

    }
}
