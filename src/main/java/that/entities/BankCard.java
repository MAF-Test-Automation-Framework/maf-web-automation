package that.entities;

import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
public class BankCard {
    public static BankCard TEST_BANK_CARD = new BankCard("4456530000001005", "12/30", "111");
    public static BankCard TABBY_TEST_BANK_CARD =
            new BankCard("4111111111111111", "12/30", "111", new String[]{"8", "8", "8", "8"});
    @NonNull
    private String cardNumber;
    @NonNull
    private String expirationDate;
    @NonNull
    private String cvv;
    private String[] otp;
}
