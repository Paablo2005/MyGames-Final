package dao;

import jakarta.persistence.EntityManager;
import java.util.List;

public interface CommonDaoInt<T> {
    T findById(EntityManager em, Object id);
    List<T> findAll(EntityManager em);
    void save(EntityManager em, T entity);
    void update(EntityManager em, T entity);
    void delete(EntityManager em, Object id);
}
