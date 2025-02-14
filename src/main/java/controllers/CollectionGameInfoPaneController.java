package controllers;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import org.controlsfx.control.CheckComboBox;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

public class CollectionGameInfoPaneController implements Initializable {

    @FXML
    private BorderPane paneImage;
    @FXML
    private Button btnUploadImage;
    
    @FXML
    private BorderPane paneImage1;
    @FXML
    private Button btnUploadImage1;
    
    @FXML
    private BorderPane paneImage2;
    @FXML
    private Button btnUploadImage2;
    
    @FXML
    private BorderPane paneImage3;
    @FXML
    private Button btnUploadImage3;
    
    /**
     * Combo box que permite seleccionar múltiples géneros para filtrar.
     */
    @FXML
    private CheckComboBox<String> comboGenre;
    
    /**
     * Combo box que permite seleccionar múltiples plataformas para filtrar.
     */
    @FXML
    private CheckComboBox<String> comboPlatform;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Añadir elementos al combo de géneros
        comboGenre.getItems().addAll(
            "Action", "Indie", "Adventure", "RPG", "Strategy", "Shooter", "Casual", 
            "Simulation", "Puzzle", "Arcade", "Platformer", "Massively Multiplayer", 
            "Racing", "Sports", "Fighting", "Family", "Board Games", "Educational", "Card"
        );
        
        // Añadir elementos al combo de plataformas
        comboPlatform.getItems().addAll(
            "PC", "PlayStation 5", "Xbox One", "PlayStation 4", "Xbox Series S/X", 
            "Nintendo Switch", "iOS", "Android", "Nintendo 3DS", "Nintendo DS", "Nintendo DSi", 
            "macOS", "Linux", "Xbox 360", "Xbox", "PlayStation 3", "PlayStation 2", 
            "PlayStation", "PS Vita", "PSP", "Wii U", "Wii", "GameCube", "Nintendo 64", 
            "Game Boy Advance", "Game Boy Color", "Game Boy", "SNES", "NES", "Classic Macintosh", 
            "Apple II", "Commodore / Amiga", "Atari 7800", "Atari 5200", "Atari 2600", "Atari Flashback", 
            "Atari 8-bit", "Atari ST", "Atari Lynx", "Atari XEGS", "Genesis", "SEGA Saturn", 
            "SEGA CD", "SEGA 32X", "SEGA Master System", "Dreamcast", "3DO", "Jaguar", "Game Gear", 
            "Neo Geo", "Web"
        );
        setupImageUploader(paneImage, btnUploadImage);
        setupImageUploader(paneImage1, btnUploadImage1);
        setupImageUploader(paneImage2, btnUploadImage2);
        setupImageUploader(paneImage3, btnUploadImage3);
    }
    
    /**
     * Configura el botón para abrir un FileChooser que solo permita seleccionar imágenes,
     * y establece la imagen seleccionada como fondo del pane indicado.
     *
     * @param pane   El contenedor en el que se establecerá la imagen de fondo.
     * @param button El botón que, al pulsarlo, abre el FileChooser.
     */
    private void setupImageUploader(BorderPane pane, Button button) {
        button.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleccionar Imagen");
            // Filtro para seleccionar únicamente imágenes
            FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter(
                "Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif");
            fileChooser.getExtensionFilters().add(imageFilter);
            
            // Se obtiene la ventana actual desde el botón
            File file = fileChooser.showOpenDialog(button.getScene().getWindow());
            if (file != null) {
                // Se carga la imagen
                Image image = new Image(file.toURI().toString());
                // Se crea el BackgroundImage con las propiedades deseadas
                BackgroundImage bgImage = new BackgroundImage(
                    image,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false)
                );
                // Se establece el fondo en el pane
                pane.setBackground(new Background(bgImage));
            }
        });
    }
}
