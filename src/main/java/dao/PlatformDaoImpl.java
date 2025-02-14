package dao;

import jakarta.persistence.EntityManager;
import models.Platform;
import java.util.List;

public class PlatformDaoImpl extends CommonDaoImpl<Platform> {

    public PlatformDaoImpl() {
        super(Platform.class);
    }

    public Platform findBySlug(EntityManager em, String slug) {
        String query = "SELECT p FROM Platform p WHERE p.slug = :slug";
        return em.createQuery(query, Platform.class)
                 .setParameter("slug", slug)
                 .getSingleResult();
    }

    public List<Platform> findByName(EntityManager em, String name) {
        String query = "SELECT p FROM Platform p WHERE p.name = :name";
        return em.createQuery(query, Platform.class)
                 .setParameter("name", name)
                 .getResultList();
    }
}
