package cn.eyeo.compact.user;

import cn.eyeo.compact.dto.UserRegisterCmd;
import org.junit.jupiter.api.Test;

public class UserValidatorTest {

    @Test
    public void testValidation() {
        new UserRegisterCmd("amos", "");
    }
}
