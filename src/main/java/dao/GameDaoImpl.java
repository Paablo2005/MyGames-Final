package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import models.Game;

public class GameDaoImpl extends CommonDaoImpl<Game> implements GameDaoInt {
	private Session session;
	
	public GameDaoImpl(Session session) {
		super(session);
		this.session = session;
	}
	
	@Override
	public List<Game> getGamesByUserCollection(int userId) {
		if (!session.getTransaction().getStatus().equals(TransactionStatus.ACTIVE)) {
			session.getTransaction().begin();
		}
		try {
			return session.createQuery(
					"SELECT g FROM Game g JOIN Collection c ON c.id.gameId = g.gameId WHERE c.id.userId = :userId", Game.class)
				    	    .setParameter("userId", userId)
				    	    .getResultList();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public Game getByApiId(int apiId) {
		if (!session.getTransaction().getStatus().equals(TransactionStatus.ACTIVE)) {
			session.getTransaction().begin();
		}
		try {
			return session.createQuery(
					"SELECT g FROM Game g WHERE g.apiId = :apiId", Game.class)
		    	    .setParameter("apiId", apiId)
		    	    .getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public Game getByDatabaseId(int gameId) {
		if (!session.getTransaction().getStatus().equals(TransactionStatus.ACTIVE)) {
			session.getTransaction().begin();
		}
		try {
			
			return session.createQuery(
		            "SELECT DISTINCT g FROM Game g " +
                    "LEFT JOIN FETCH g.platforms p " +
                    "LEFT JOIN FETCH p.games " +
                    "LEFT JOIN FETCH g.genres r " +
                    "LEFT JOIN FETCH r.games " +
                    "WHERE g.gameId = :gameId", Game.class)
		    	    .setParameter("gameId", gameId)
		    	    .getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
