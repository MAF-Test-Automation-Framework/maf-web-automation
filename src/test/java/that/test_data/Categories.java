package that.test_data;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class Categories {
    @AllArgsConstructor
    @Getter
    public enum HeaderMenuCategories {
        WOMEN("WOMEN"),
        SHOES("SHOES"),
        SANDALS("Sandals");

        final String category;
    }

    @AllArgsConstructor
    @Getter
    public enum SortOptions {
        LOWEST_PRICE("Lowest Price"),
        HIGHEST_PRICE("Highest Price"),
        RECOMMENDED("Recommended");

        final String option;
    }

    @AllArgsConstructor
    @Getter
    public enum FilterOptions {
        BRAND("Brand"),
        JUIN_BRAND("3JUIN"),
        CATEGORY("Category"),
        ON_SALE_CATEGORY("On Sale");

        final String option;
    }

    @AllArgsConstructor
    @Getter
    public enum AccountPageSections {
        YOUR_ACCOUNT("YOUR ACCOUNT"),
        UPCOMING_ORDERS("UPCOMING ORDERS"),
        YOUR_WISHLIST("YOUR WISHLIST"),
        ACCOUNT_SIGN_OUT("ACCOUNT SIGN OUT"),
        ORDERS("ORDERS");
        final String section;
    }
}
