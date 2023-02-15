package that.test_data;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class Categories {
    public enum L1Categories {
        WOMEN
    }

    public enum L2Categories {
        SHOES
    }

    @AllArgsConstructor
    @Getter
    public enum SortOptions {
        LOWEST_PRICE("Lowest Price"),
        HIGHEST_PRICE("Highest Price");

        final String option;

    }
}
