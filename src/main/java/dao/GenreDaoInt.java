package dao;

import jakarta.persistence.EntityManager;
import models.Genre;
import java.util.List;

public interface GenreDaoInt extends CommonDaoInt<Genre> {
    Genre findBySlug(EntityManager em, String slug);
    List<Genre> findByName(EntityManager em, String name);
}
