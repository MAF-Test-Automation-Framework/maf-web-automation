package that.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BankCard {
    public static BankCard TEST_BANK_CARD = new BankCard("4456530000001005", "12/30", "111");
    private String cardNumber;
    private String expirationDate;
    private String cvv;
}
