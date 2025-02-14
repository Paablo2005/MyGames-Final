package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import models.Game;
import utils.HibernateUtil;
import utils.PasswordUtil;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Session;
import dao.GameDaoImpl;
import jakarta.persistence.Query;

public class CollectionPaneController implements Initializable {

    /**
     * Instancia única (singleton) de {@link CollectionPaneController}.
     */
    private static CollectionPaneController instance;
    
    /**
     * Obtiene la instancia actual de {@link CollectionPaneController}.
     *
     * @return la instancia actual del controlador
     */
    public static CollectionPaneController getInstance() {
        return instance;
    }
    
    @FXML
    private GridPane gridGameContainer;
    
    @FXML
    private Pane titlePane;

    @FXML
    private Pane paneDescription;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	instance = this;
    	loadGamesFromDatabase(); 
        
        Rectangle clipImgBackgroundTitle = new Rectangle(643, 222);
        clipImgBackgroundTitle.setArcHeight(40);
        clipImgBackgroundTitle.setArcWidth(40);
        titlePane.setClip(clipImgBackgroundTitle);
        
        Rectangle clipTxtTitle = new Rectangle(320, 135);
        clipTxtTitle.setArcHeight(40);
        clipTxtTitle.setArcWidth(40);
        paneDescription.setClip(clipTxtTitle);
    }

    private void loadGamesFromDatabase() {
    	gridGameContainer.getChildren().clear();
    	try (Session session = HibernateUtil.getSessionFactory().openSession()) {
    		GameDaoImpl gameDao = new GameDaoImpl(session);
			
    		List<Game> games = gameDao.getGamesByUserCollection(PasswordUtil.getLoggedUser().getId());
    		
    		if (games != null) {
    			int column = 0;
    			int row = 0;
    			
    			for (Game game : games) {
    				
    				FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/GameContainer.fxml"));
    				Pane gameContainer = loader.load();
    				
    				GameContainerController controller = loader.getController();
    				controller.setGame(game);
    				
    				gridGameContainer.add(gameContainer, column, row);
    				
    				column++;
    				if (column == 3) { // Máximo de 3 columnas
    					column = 0;
    					row++;
    				}
    			}
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    @SuppressWarnings("unchecked")
	public List<Integer> getPlatformIds(List<String> currentPlatforms) {
    	try (Session session = HibernateUtil.getSessionFactory().openSession()) {
    		String hql = """
    	            SELECT p.platformPK FROM Platforms p
    	            WHERE p.name IN (:platforms)
    	        """;
    	
    	        Query query = session.createQuery(hql, Integer.class);
    	        query.setParameter("platforms", currentPlatforms);
    	        
    		return query.getResultList();
    	} catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    
    @SuppressWarnings("unchecked")
	public List<Integer> getGenreIds(List<String> currentGenres) {
    	try (Session session = HibernateUtil.getSessionFactory().openSession()) {
    		String hql = """
    	            SELECT g.genrePK FROM Genre g
    	            WHERE g.name IN (:genres)
    	        """;
    	
    	        Query query = session.createQuery(hql, Integer.class);
    	        query.setParameter("genres", currentGenres);
    	        
    		return query.getResultList();
    	} catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    
    @SuppressWarnings("unchecked")
	public void filterGames(String currentSearchQuery, List<String> currentGenres, List<String> currentPlatforms) {
    	gridGameContainer.getChildren().clear();
    	
    	List<Integer> selectedPlatforms = getPlatformIds(currentPlatforms);
    	List<Integer> selectedGenres = getGenreIds(currentGenres);
    	
    	if (selectedPlatforms.isEmpty() && selectedGenres.isEmpty() && currentSearchQuery.isEmpty()) {
    		loadGamesFromDatabase();
    		return;
    	}
    	try (Session session = HibernateUtil.getSessionFactory().openSession()) {
    		String hql = """
	            SELECT DISTINCT g FROM Game g
	            JOIN g.platforms gp
	            JOIN g.genres gg
	            WHERE (:searchQuery IS NULL OR LOWER(g.name) LIKE LOWER(CONCAT('%', :searchQuery, '%')))
	            """            
	            + (selectedPlatforms.isEmpty() ? "" : " AND gp.platformPK IN :platforms ") 
	            + (selectedGenres.isEmpty() ? "" : " AND gg.genrePK IN :genres ");
    		
	        Query query = session.createQuery(hql, Game.class);
	        
	        query.setParameter("searchQuery", currentSearchQuery == null || currentSearchQuery.isEmpty() ? null : currentSearchQuery);
	        if (!selectedPlatforms.isEmpty()) {
	            query.setParameter("platforms", selectedPlatforms);
	        }
	        if (!selectedGenres.isEmpty()) {
	            query.setParameter("genres", selectedGenres);
	        }
	
	        loadPageWithList(query.getResultList());
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
    private void loadPageWithList(List<Game> resultList) {
    	gridGameContainer.getChildren().clear();
        if (resultList != null && !resultList.isEmpty()) {
            int column = 0;
            int row = 0;
            for (Game game : resultList) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/GameContainer.fxml"));
                    Pane gameContainer = loader.load();

                    GameContainerController controller = loader.getController();
                    controller.setGame(game);

                    gridGameContainer.add(gameContainer, column, row);

                    column++;
                    if (column == 3) {
                        column = 0;
                        row++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
        	System.out.println("La BÚSQUEDA no dio resultados o está vacía");
        }
	}

	public void clearSearchAndFilters() {
        loadGamesFromDatabase();
    }
}
