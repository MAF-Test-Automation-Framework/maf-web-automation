package that.entities;

import lombok.*;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class User {
    public static User LOGIN_TEST_USER = new User("testuser@testuser.com", "UserTest1!");
    public static User TEST_USER = new User(String.format("%s@testuser.com", UUID.randomUUID()),
            "UserTest1!", "Mr", "User", "Test", "UNITED ARAB EMIRATES",
            "2000", "JANUARY", "1", "501234567");
    @NonNull
    private String email;
    @NonNull
    private String password;
    private String title;
    private String firstName;
    private String lastName;
    private String nationality;
    private String year;
    private String month;
    private String day;
    private String phoneNumber;
}
