package dao;

import jakarta.persistence.EntityManager;
import models.Genre;
import java.util.List;

public class GenreDaoImpl extends CommonDaoImpl<Genre> {

    public GenreDaoImpl() {
        super(Genre.class);
    }

    public Genre findBySlug(EntityManager em, String slug) {
        String query = "SELECT g FROM Genre g WHERE g.slug = :slug";
        return em.createQuery(query, Genre.class)
                 .setParameter("slug", slug)
                 .getSingleResult();
    }

    public List<Genre> findByName(EntityManager em, String name) {
        String query = "SELECT g FROM Genre g WHERE g.name = :name";
        return em.createQuery(query, Genre.class)
                 .setParameter("name", name)
                 .getResultList();
    }
}
