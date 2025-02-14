package dao;

import jakarta.persistence.EntityManager;
import models.User;
import java.util.List;

public class UserDaoImpl extends CommonDaoImpl<User> implements UserDaoInt {

    public UserDaoImpl() {
        super(User.class);
    }

    @Override
    public User findByMail(EntityManager em, String mail) {
        String query = "FROM User WHERE mail = :mail";
        List<User> result = em.createQuery(query, User.class)
                               .setParameter("mail", mail)
                               .getResultList();
        // Si no se encuentra el usuario, devolvemos null
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public List<User> findByUserName(EntityManager em, String userName) {
        String query = "SELECT u FROM User u WHERE u.userName = :userName";
        return em.createQuery(query, User.class)
                 .setParameter("userName", userName)
                 .getResultList();
    }
}
