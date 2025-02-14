package dao;

import jakarta.persistence.EntityManager;
import models.Platform;
import java.util.List;

public interface PlatformDaoInt extends CommonDaoInt<Platform> {
    Platform findBySlug(EntityManager em, String slug);
    List<Platform> findByName(EntityManager em, String name);
}
