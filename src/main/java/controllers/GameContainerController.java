package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import models.Game;

public class GameContainerController {

    @FXML
    private Pane paneImg;

    @FXML
    private Label labelName;

    public void initialize() {
        
		Rectangle clipImg = new Rectangle(156, 97.6);
		clipImg.setArcHeight(40);
		clipImg.setArcWidth(40);
		paneImg.setClip(clipImg);
    }

    public void setGame(Game game) {
    	paneImg.setStyle("-fx-background-image: url('"+game.getPrincipalImg()+"'); -fx-background-size: cover; -fx-background-position: center;");
        labelName.setText(game.getName());
        //Image image = new Image(game.getPrincipalImg(), 156, 97.6, true, true);
        //imageView.setImage(image);
    }
    
    public void setOnClickAction(Runnable action) {
      paneImg.setOnMouseClicked(event -> {
          System.out.println("Click en juego: " + labelName.getText()); // Debugging
          action.run();
      });
  }

}