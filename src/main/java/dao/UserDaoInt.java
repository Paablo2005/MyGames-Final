package dao;

import jakarta.persistence.EntityManager;
import models.User;
import java.util.List;

public interface UserDaoInt extends CommonDaoInt<User> {
    User findByMail(EntityManager em, String mail);
    List<User> findByUserName(EntityManager em, String userName);
}
