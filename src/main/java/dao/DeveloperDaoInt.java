package dao;

import jakarta.persistence.EntityManager;
import models.Developer;
import java.util.List;

public interface DeveloperDaoInt extends CommonDaoInt<Developer> {
    List<Developer> findByName(EntityManager em, String name);
    Developer findBySlug(EntityManager em, String slug);
}
