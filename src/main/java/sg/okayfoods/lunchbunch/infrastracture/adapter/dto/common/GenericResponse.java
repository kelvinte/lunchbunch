package sg.okayfoods.lunchbunch.infrastracture.adapter.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GenericResponse(String title, Object data, int status,  boolean success) {

}
