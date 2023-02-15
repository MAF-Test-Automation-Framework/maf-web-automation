package utils;

public class Utils {
    public static Integer getIntegerValueOfPrice(String price) {
        String priceValue = price.replaceAll("\\D", "");
        return Integer.parseInt(priceValue);
    }
}
