package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import models.Game;

import java.util.List;

public class GameDaoImpl extends CommonDaoImpl<Game> {

    public GameDaoImpl() {
        super(Game.class);
    }

    public List<Game> findByName(EntityManager em, String name) {
        String query = "SELECT g FROM Game g WHERE g.name = :name";
        return em.createQuery(query, Game.class)
                 .setParameter("name", name)
                 .getResultList();
    }

    // Método para guardar un juego en la base de datos
    public void saveGame(EntityManager em, Game game) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(game);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    // Método para obtener todos los juegos guardados
    public List<Game> getAllGames(EntityManager em) {
        return em.createQuery("SELECT g FROM Game g", Game.class).getResultList();
    }
}
