package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import models.Game;

public abstract class GameDaoImpl extends CommonDaoImpl<Game> implements GameDaoInt {
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
			return (List<Game>)session.createQuery(
				    "SELECT g FROM Game g " +
				    	    "JOIN g.collections c " +
				    	    "WHERE c.user.id = :userId", Game.class)
				    	    .setParameter("userId", userId)
				    	    .getResultList();

		} catch (Exception e) {
			return null;
		}
	}
}
