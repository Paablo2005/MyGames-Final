package dao;

import jakarta.persistence.EntityManager;
import models.GameImage;
import java.util.List;

public class GameImageDaoImpl extends CommonDaoImpl<GameImage> {

    public GameImageDaoImpl() {
        super(GameImage.class);
    }

    public List<GameImage> findByGameId(EntityManager em, Integer gameId) {
        String query = "SELECT gi FROM GameImage gi WHERE gi.game.id = :gameId";
        return em.createQuery(query, GameImage.class)
                 .setParameter("gameId", gameId)
                 .getResultList();
    }
}
