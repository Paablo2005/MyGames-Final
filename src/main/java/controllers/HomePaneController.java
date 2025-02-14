package controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import models.Game;
import utils.ApiUtils;

public class HomePaneController implements Initializable {

    @FXML
    private GridPane gridGameContainer;

    @FXML
    private HBox paneDescription;

    @FXML
    private Pane titlePane;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadGamesFromAPI(9);
		
        Rectangle clipImgBackgroundTitle = new Rectangle(643, 222);
        clipImgBackgroundTitle.setArcHeight(40);
        clipImgBackgroundTitle.setArcWidth(40);
        titlePane.setClip(clipImgBackgroundTitle);
	}

    private void loadGamesFromAPI(int count) {
        List<Game> games = ApiUtils.fetchGames(1,9);
        if (games != null) {
            int column = 0;
            int row = 0;

            for (int i = 0; i < Math.min(count, games.size()); i++) {
                Game game = games.get(i);
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
}