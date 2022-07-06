package homework.database;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Criteria {
    @Builder.Default
    private String productNameQuery = "";
    @Builder.Default
    private String groupNameQuery = "";
    private double lowerBoundPrice;
    @Builder.Default
    private double upperBoundPrice = Double.POSITIVE_INFINITY;
    private int lowerBoundAmount;
    @Builder.Default
    private int upperBoundAmount = Integer.MAX_VALUE;
}
