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
    public enum AccountPageSection{
        YOUR_ACCOUNT("YOUR ACCOUNT"),
        COMPLETE_YOUR_DETAILS("COMPLETE YOUR DETAILS"),
        YOUR_WISHLIST("YOUR WISHLIST"),
        ACCOUNT_SIGN_OUT("ACCOUNT SIGN OUT"),
        ORDERS("ORDERS");
        final String section;
        }
}
