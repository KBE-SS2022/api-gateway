package gateway.dto;

import lombok.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class PizzaDTO {

    private Long id;
    private String name;
    private Map<Long,Double> ingredientIdToPrice;

}
