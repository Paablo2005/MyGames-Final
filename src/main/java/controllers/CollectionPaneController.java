package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class CollectionPaneController implements Initializable{
	
    @FXML
    private GridPane gridGameContainer;
	
    @FXML
    private Pane titlePane;

    @FXML
    private Pane paneDescription;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Rectangle clipImgBackgroundTitle = new Rectangle(643, 222);
		clipImgBackgroundTitle.setArcHeight(40);
		clipImgBackgroundTitle.setArcWidth(40);
		titlePane.setClip(clipImgBackgroundTitle);
		
		Rectangle clipTxtTitle = new Rectangle(320, 135);
		clipTxtTitle.setArcHeight(40);
		clipTxtTitle.setArcWidth(40);
		paneDescription.setClip(clipTxtTitle);
	}
}
