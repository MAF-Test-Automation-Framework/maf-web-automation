package utils;

import java.text.NumberFormat;
import java.util.Locale;

public class Utils {
    public static Integer getIntegerValueOfPrice(String price) {
        String priceValue = price.replaceAll("\\D", "");
        return Integer.parseInt(priceValue);
    }

    public static String getStringValueOfPrice(Integer price) {
        return NumberFormat.getNumberInstance(Locale.US).format(price);
    }
}
