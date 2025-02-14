package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class GameItemController implements Initializable {

    @FXML
    private Pane gameImg;

    @FXML
    private Label lblGame;

    private Runnable onClickAction;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Rectangle clipImg = new Rectangle(156, 97.6);
        clipImg.setArcHeight(40);
        clipImg.setArcWidth(40);
        gameImg.setClip(clipImg);

        // Asegurar que el panel pueda recibir eventos de clic
        gameImg.setOnMouseClicked(event -> {
            if (onClickAction != null) {
                System.out.println("Clicked on game: " + lblGame.getText()); // Debugging
                onClickAction.run();
            }
        });

        // Hacer que el panel acepte clics
        gameImg.setPickOnBounds(true);
    }

    public void setData(String title, String imgUrl) {
        if (imgUrl != null && !imgUrl.isEmpty()) {
            gameImg.setStyle("-fx-background-image: url('" + imgUrl + "'); -fx-background-size: cover; -fx-background-position: center;");
        } else {
            gameImg.setStyle("-fx-background-color: #333333;"); // Color de fondo si no hay imagen
        }
        lblGame.setText(title);
    }
    
    public void setOnClickAction(Runnable action) {
      this.onClickAction = action;
      gameImg.setOnMouseClicked(event -> {
          if (onClickAction != null) {
              System.out.println("Seleccionado: " + lblGame.getText());
              onClickAction.run();
          }
      });
  }

}
