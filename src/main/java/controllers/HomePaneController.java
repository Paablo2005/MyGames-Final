package controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import models.Game;
import utils.ApiUtils;

public class HomePaneController implements Initializable {

    @FXML
    private GridPane gridGameContainer;

    @FXML
    private Pane titlePane;

    private MainPaneController mainPaneController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadGamesFromAPI(9);

        Rectangle clipImgBackgroundTitle = new Rectangle(643, 222);
        clipImgBackgroundTitle.setArcHeight(40);
        clipImgBackgroundTitle.setArcWidth(40);
        titlePane.setClip(clipImgBackgroundTitle);
    }

    public void setMainPaneController(MainPaneController controller) {
        this.mainPaneController = controller;
    }

    private void loadGamesFromAPI(int count) {
        List<Game> games = ApiUtils.fetchGames(count, count);
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

                    // Agregar efecto de hover
                    addHoverEffect(gameContainer);

                    // Agregar evento de clic a la imagen del juego
                    gameContainer.setOnMouseClicked(event -> {
                        if (mainPaneController != null) {
                            mainPaneController.loadDescriptionPane(game);
                        }
                    });

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

    // Método para agregar efecto de hover
    private void addHoverEffect(Pane pane) {
        Scale scaleUp = new Scale(1.1, 1.1, pane.getWidth() / 2, pane.getHeight() / 2);
        Scale scaleDown = new Scale(1.0, 1.0, pane.getWidth() / 2, pane.getHeight() / 2);
        
        DropShadow shadowEffect = new DropShadow(15, Color.web("#007bff"));

        pane.setOnMouseEntered(event -> {
            pane.getTransforms().add(scaleUp);
            pane.setEffect(shadowEffect);
        });

        pane.setOnMouseExited(event -> {
            pane.getTransforms().remove(scaleUp);
            pane.getTransforms().add(scaleDown);
            pane.setEffect(null);
        });
    }
}
