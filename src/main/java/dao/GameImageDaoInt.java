package dao;

import jakarta.persistence.EntityManager;
import models.GameImage;
import java.util.List;

public interface GameImageDaoInt extends CommonDaoInt<GameImage> {
    List<GameImage> findByGameId(EntityManager em, Integer gameId);
}
