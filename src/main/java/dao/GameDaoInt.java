package dao;

import java.sql.SQLException;
import java.util.List;

import models.Game;

public interface GameDaoInt {	
	/**
	 * Obtenemos la lista de juegos que esten en la lista de coleccion, y que correspondan a un usuario
	 * segun el ID que le pasemos por parametro
	 * @param userId - ID del usuario del que vamos a buscar sus juegos
	 * @return
	 * @throws SQLException
	 */
	public List<Game> getGamesByUserCollection(int userId) throws SQLException;
}
