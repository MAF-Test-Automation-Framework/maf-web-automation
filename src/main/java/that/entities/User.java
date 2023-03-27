package that.entities;

import lombok.*;

import java.util.UUID;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class User {
    public static User LOGIN_TEST_USER = new User("testuser@testuser.com", "UserTest1!", "Mr",
            "User", "Test", "UNITED ARAB EMIRATES", "2000", "JANUARY", "1",
            "+971", "501234567");
    public static User SIGN_UP_TEST_USER = new User(String.format("%s@testuser.com", UUID.randomUUID()),
            "UserTest1!", "Mr", "User", "Test", "UNITED ARAB EMIRATES",
            "2000", "JANUARY", "1", "+971", "501234567");

    public static User TABBY_TEST_USER = new User("card.success@tabby.ai", "+971", "500000001");
    @NonNull
    private String email;
    private String password;
    private String title;
    private String firstName;
    private String lastName;
    private String nationality;
    private String year;
    private String month;
    private String day;
    @NonNull
    private String phoneCountryCode;
    @NonNull
    private String phoneNumber;
}
