package controllers;

import dao.GameDaoImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import models.Game;
import utils.HibernateUtil;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CollectionPaneController implements Initializable {

    @FXML
    private GridPane gridGameContainer;

    @FXML
    private Pane titlePane;

    @FXML
    private Pane paneDescription;

    private GameDaoImpl gameDao;

    public CollectionPaneController() {
        this.gameDao = new GameDaoImpl();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadSavedGames();

        Rectangle clipImgBackgroundTitle = new Rectangle(643, 222);
        clipImgBackgroundTitle.setArcHeight(40);
        clipImgBackgroundTitle.setArcWidth(40);
        titlePane.setClip(clipImgBackgroundTitle);

        Rectangle clipTxtTitle = new Rectangle(320, 135);
        clipTxtTitle.setArcHeight(40);
        clipTxtTitle.setArcWidth(40);
        paneDescription.setClip(clipTxtTitle);
    }

    // Cargar los juegos guardados desde la base de datos
    private void loadSavedGames() {
        gridGameContainer.getChildren().clear();
        EntityManager em = HibernateUtil.getSessionFactory().createEntityManager();
        List<Game> savedGames = gameDao.getAllGames(em);
        em.close();

        int column = 0;
        int row = 0;

        for (Game game : savedGames) {
            try {
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}