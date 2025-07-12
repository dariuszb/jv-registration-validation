package core.basesyntax;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.dao.StorageDaoImpl;
import core.basesyntax.db.Storage;
import core.basesyntax.model.User;
import core.basesyntax.service.RegistrationException;
import core.basesyntax.service.RegistrationServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HelloWorldTest {

    private static final Integer AGE_UNDER_LIMIT = 10;
    private static final Integer AGE_ABOVE_MIN_BOUND = 20;
    private static final Integer AGE_EQUAL_BOUND = 18;
    private static final Integer NEGATIVE_AGE = -5;
    private static RegistrationServiceImpl registrationService = new RegistrationServiceImpl();
    private static StorageDao storageDao = new StorageDaoImpl();
    private static User user1 = new User();
    private static User user2 = new User();
    private static User user3 = new User();

    @BeforeEach public void beforeEach() {
        user1 = new User();
        user1.setLogin("login123");
        user1.setPassword("password321");
        user1.setAge(AGE_ABOVE_MIN_BOUND);

        user2 = new User();
        user2.setLogin("login456");
        user2.setPassword("password654");
        user2.setAge(AGE_ABOVE_MIN_BOUND);

        user3 = new User();
        user3.setLogin("login789");
        user3.setPassword("password987");
        user3.setAge(AGE_ABOVE_MIN_BOUND);

        registrationService = new RegistrationServiceImpl();
        storageDao = new StorageDaoImpl();
    }

    @AfterEach void afterEach() {
        Storage.people.clear();
    }

    @Test
    void setLogin_WithEightSigns_Ok() {
        user1.setAge(AGE_ABOVE_MIN_BOUND);
        storageDao.add(user1);
        Assertions.assertEquals(user1, storageDao.get("login123"));
    }

    @Test
    void register_WithValueUnderLimit_notOk() {
        user1.setLogin("");
        Assertions.assertThrows(RegistrationException.class,
                () -> registrationService.register(user1));

        user1.setLogin("qwe");
        Assertions.assertThrows(RegistrationException.class,
                () -> registrationService.register(user1));

        user1.setLogin("qwert");
        Assertions.assertThrows(RegistrationException.class,
                () -> registrationService.register(user1));
    }

    @Test
    void register_setLoginWithSixLetters_ok() {
        user1.setLogin("login1");
        user1.setAge(AGE_ABOVE_MIN_BOUND);
        Assertions.assertEquals(user1, registrationService.register(user1));
        Assertions.assertEquals(user1, storageDao.get("login1"));
    }

    @Test
    void setPassword_isThrowExceptionWhenLoginLengthIsIncorrect_notOk() {
        user1.setPassword("login");
        Assertions.assertThrows(RegistrationException.class,
                () -> registrationService.register(user1));
    }

    @Test
    void setAge_isThrowExceptionWhenAgeIsIncorrect_notOk() {
        User user1 = new User();
        user1.setAge(AGE_UNDER_LIMIT);
        Assertions.assertThrows(RegistrationException.class,
                () -> registrationService.register(user1));
    }

    @Test
    void setAge_ValueOfAgeIsOverBound_ok() {
        storageDao.add(user1);
        Assertions.assertEquals(user1, storageDao.get("login123"));
        Assertions.assertEquals(AGE_ABOVE_MIN_BOUND, storageDao.get("login123").getAge());
    }

    @Test
    void setAge_ValueOfAgeIsNegative_not_Ok() {
        user1.setAge(NEGATIVE_AGE);
        Assertions.assertThrows(RegistrationException.class,
                () -> registrationService.register(user1));
    }

    @Test
    void setAge_ValueOfAgeEqualsBound_ok() {
        user1.setAge(AGE_EQUAL_BOUND);
        registrationService.register(user1);
        Assertions.assertEquals(user1, storageDao.get("login123"));
        Assertions.assertEquals(AGE_EQUAL_BOUND, storageDao.get("login123").getAge());
    }

    @Test
    void register_LoginIsNull_not_Ok() {
        user1.setLogin(null);
        Assertions.assertThrows(RegistrationException.class,
                () -> registrationService.register(user1));
    }

    @Test
    void register_PasswordIsNull_not_Ok() {
        user1.setPassword(null);
        Assertions.assertThrows(RegistrationException.class,
                () -> registrationService.register(user1));
    }

    @Test
    void register_AgeIsNull_not_Ok() {
        user1.setAge(null);
        Assertions.assertThrows(RegistrationException.class,
                () -> registrationService.register(user1));
    }

    @Test
    void register_CollisionWithTwoTheSameLogins_not_Ok() {
        registrationService.register(user1);
        Assertions.assertEquals(user1, storageDao.get("login123"));
        user2.setLogin("login123");
        Assertions.assertThrows(RegistrationException.class,
                () -> registrationService.register(user2));
    }

    @Test
    void add_UsersToTheStorage_ok() {
        storageDao.add(user1);
        Assertions.assertEquals(user1, storageDao.get("login123"));
        storageDao.add(user2);
        Assertions.assertEquals(user2, storageDao.get("login456"));
        storageDao.add(user3);
        Assertions.assertEquals(user3, storageDao.get("login789"));
        Assertions.assertEquals(3, (long) Storage.people.size());

    }

    @Test
    void get_WithThreeElementsStorage_ok() {
        storageDao.add(user1);
        storageDao.add(user2);
        storageDao.add(user3);

        Assertions.assertEquals(user1, storageDao.get("login123"));
        Assertions.assertEquals(user2, storageDao.get("login456"));
        Assertions.assertEquals(user3, storageDao.get("login789"));
    }
}
