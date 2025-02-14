package dao;

import jakarta.persistence.EntityManager;
import models.Developer;
import java.util.List;

public class DeveloperDaoImpl extends CommonDaoImpl<Developer> {

    public DeveloperDaoImpl() {
        super(Developer.class);
    }

    public List<Developer> findByName(EntityManager em, String name) {
        String query = "SELECT d FROM Developer d WHERE d.name = :name";
        return em.createQuery(query, Developer.class)
                 .setParameter("name", name)
                 .getResultList();
    }

    public Developer findBySlug(EntityManager em, String slug) {
        String query = "SELECT d FROM Developer d WHERE d.slug = :slug";
        return em.createQuery(query, Developer.class)
                 .setParameter("slug", slug)
                 .getSingleResult();
    }
}
