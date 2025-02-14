package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import utils.ConversionUtils;

import java.util.ArrayList;
import java.util.List;

import org.controlsfx.control.CheckComboBox;

/**
 * Controlador del panel de cabecera que gestiona las acciones de búsqueda y filtrado.
 * <p>
 * Este controlador se encarga de inicializar los componentes de la interfaz del encabezado,
 * incluyendo el campo de búsqueda, los botones y los combo boxes de filtros (géneros y plataformas).
 * Gestiona las acciones de búsqueda, aplicación de filtros y limpieza de filtros.
 * </p>
 */
public class HeaderPaneController {

    /**
     * Campo de texto para ingresar la consulta de búsqueda.
     */
    @FXML
    private TextField txtSearch;

    /**
     * Botón para iniciar la búsqueda.
     */
    @FXML
    private Button btnSearch;

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
    
    /**
     * Botón para borrar los filtros aplicados.
     */
    @FXML
    private Button btnDeleteFilters;
    
    /**
     * Botón para aplicar los filtros seleccionados.
     */
    @FXML
    private Button btnFilter;
    
    /**
     * Inicializa el controlador.
     * <p>
     * Configura las acciones de los botones de búsqueda, borrado de filtros y filtrado.
     * Además, se inicializan los items de los combo boxes para los géneros y las plataformas.
     * </p>
     */
    @FXML
    public void initialize() {
        btnSearch.setOnAction(event -> handleSearch());
        
        btnDeleteFilters.setOnAction(event -> clearFilters());
        
        btnFilter.setOnAction(event -> handleFilter());
        
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
    }

    /**
     * Maneja la acción de búsqueda.
     * <p>
     * Obtiene el texto ingresado en el campo de búsqueda. Si la consulta está vacía, recarga la página
     * actual de juegos; de lo contrario, inicia una búsqueda mediante el {@link GamesPaneController}.
     * </p>
     */
    private void handleSearch() {
        String query = txtSearch.getText().trim();
        if (query.isEmpty()) {
            GamesPaneController.getInstance().loadPage(GamesPaneController.getInstance().getCurrentPage());
        } else {
            GamesPaneController.getInstance().searchGames(query);
        }
    }
    
    /**
     * Limpia los filtros seleccionados.
     * <p>
     * Borra las selecciones de los combo boxes de géneros y plataformas y restablece
     * la búsqueda y filtros en el {@link GamesPaneController}.
     * </p>
     */
    private void clearFilters() {
        comboGenre.getCheckModel().clearChecks();
        comboPlatform.getCheckModel().clearChecks();
        GamesPaneController.getInstance().clearSearchAndFilters();
    }

    /**
     * Maneja la acción de filtrado.
     * <p>
     * Recupera los elementos seleccionados en los combo boxes de géneros y plataformas, los convierte
     * en el formato adecuado (slug para géneros e identificadores para plataformas) y aplica los filtros
     * llamando al método {@code filterGames} del {@link GamesPaneController}.
     * </p>
     */
    private void handleFilter() {
        List<String> selectedGenres = comboGenre.getCheckModel().getCheckedItems();
        List<String> selectedPlatforms = comboPlatform.getCheckModel().getCheckedItems();

        List<String> genresSlug = new ArrayList<>();
        for (String genre : selectedGenres) {
            genresSlug.add(genre.toLowerCase().replace(" ", "-"));
        }

        List<String> platformsIds = new ArrayList<>();
        for (String platform : selectedPlatforms) {
            String id = ConversionUtils.PLATFORM_MAP.get(platform);
            if (id != null && !id.isEmpty()) {
                platformsIds.add(id);
            }
        }

        String genresParam = String.join(",", genresSlug);
        String platformsParam = String.join(",", platformsIds);

        GamesPaneController.getInstance().filterGames(genresParam, platformsParam);
    }  
}
