package core.basesyntax.service;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.dao.StorageDaoImpl;
import core.basesyntax.db.Storage;
import core.basesyntax.model.User;

public class RegistrationServiceImpl implements RegistrationService {
    private static final int MIN_LENGTH = 6;
    private static final int MIN_AGE = 18;
    private final StorageDao storageDao = new StorageDaoImpl();

    @Override
    public User register(User user) {
        if (user.getLogin() == null) {
            throw new RegistrationException("Login can't be null");
        }
        if (user.getPassword() == null) {
            throw new RegistrationException("Password can't be null");
        }
        if (user.getAge() == null) {
            throw new RegistrationException("Age can't be null");
        }
        if (user.getLogin().length() < MIN_LENGTH) {
            throw new RegistrationException("Length of login is too short.");
        }
        if (user.getPassword().length() < MIN_LENGTH) {
            throw new RegistrationException("Length of password is too short.");
        }
        if (user.getAge() < MIN_AGE) {
            throw new RegistrationException("Age is too low.");
        }
        for (User current : Storage.people) {
            if (current.getLogin().equals(user.getLogin())) {
                throw new RegistrationException("This login exist in database. Create new.");
            }
        }
        storageDao.add(user);
        return user;
    }
}
