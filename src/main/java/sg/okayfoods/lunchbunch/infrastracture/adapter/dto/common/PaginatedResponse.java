package sg.okayfoods.lunchbunch.infrastracture.adapter.dto.common;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaginatedResponse<T> {

    private List<T> result;
    private Integer page;
    private Integer size;
    private Integer totalPage;
    private Long totalItems;

}
