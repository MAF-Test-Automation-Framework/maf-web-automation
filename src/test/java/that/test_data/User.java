package that.test_data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class User {
    public static User TEST_USER = new User("testuser@testuser.com", "UserTest1!");

    private String email;
    private String password;
}
