package dao;

import jakarta.persistence.EntityManager;
import models.Game;
import java.util.List;

public interface GameDaoInt extends CommonDaoInt<Game> {
    List<Game> findByName(EntityManager em, String name);
}
