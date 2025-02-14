package controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import models.Game;
import utils.ApiUtils;

/**
 * Controlador para la gestión y visualización de la lista de juegos en un panel de cuadrícula.
 * <p>
 * Este controlador se encarga de la paginación, búsqueda y filtrado de objetos {@link models.Game}.
 * Utiliza llamadas a la API para obtener los datos y actualiza los componentes de la interfaz
 * de usuario (JavaFX) según corresponda.
 * </p>
 */
public class GamesPaneController implements Initializable {

    /**
     * Instancia única (singleton) de {@link GamesPaneController}.
     */
    private static GamesPaneController instance;
    
    /**
     * Obtiene la instancia actual de {@link GamesPaneController}.
     *
     * @return la instancia actual del controlador
     */
    public static GamesPaneController getInstance() {
        return instance;
    }
    
    /**
     * Página actual que se está mostrando.
     */
    private int currentPage = 1;
    
    /**
     * Número de juegos que se muestran por página.
     */
    private final int gamesPerPage = 18;
    
    /**
     * Consulta de búsqueda actual para filtrar juegos por nombre.
     */
    private String currentSearchQuery = "";
    
    /**
     * Filtro actual de géneros.
     */
    private String currentGenres = "";
    
    /**
     * Filtro actual de plataformas.
     */
    private String currentPlatforms = "";
    
    /**
     * Panel de cuadrícula donde se muestran los contenedores de juegos.
     */
    @FXML
    private GridPane gridGameContainer;
    
    /**
     * Panel que contiene la imagen de fondo del título.
     */
    @FXML
    private Pane titlePane;
    
    /**
     * Panel que contiene la descripción o texto relacionado.
     */
    @FXML
    private Pane paneDescription;
    
    /**
     * Botón para navegar a la página anterior.
     */
    @FXML
    private Button btnPrevious;
    
    /**
     * Botón para navegar a la página siguiente.
     */
    @FXML
    private Button btnNext;
    
    /**
     * Método de inicialización llamado automáticamente al cargar la vista FXML.
     * <p>
     * Se establece la instancia singleton, se carga la primera página de juegos y se configuran
     * los recortes (clips) para los paneles de imagen y descripción, otorgándoles bordes redondeados.
     * Además, se deshabilita el botón de página anterior al inicio.
     * </p>
     *
     * @param location  la ubicación utilizada para resolver rutas relativas o {@code null} si es desconocida
     * @param resources los recursos utilizados para la localización de la raíz o {@code null} si no se localiza
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        loadPage(currentPage);

        Rectangle clipImgBackgroundTitle = new Rectangle(643, 222);
        clipImgBackgroundTitle.setArcHeight(40);
        clipImgBackgroundTitle.setArcWidth(40);
        titlePane.setClip(clipImgBackgroundTitle);

        Rectangle clipTxtTitle = new Rectangle(320, 135);
        clipTxtTitle.setArcHeight(40);
        clipTxtTitle.setArcWidth(40);
        paneDescription.setClip(clipTxtTitle);

        btnPrevious.setDisable(true);
    }
    
    /**
     * Carga una página específica de juegos en el panel de cuadrícula.
     * <p>
     * Este método limpia el contenido actual de la cuadrícula, obtiene una lista de juegos a partir
     * de la API y añade cada juego a la cuadrícula utilizando un contenedor definido en un archivo FXML.
     * </p>
     *
     * @param page el número de página a cargar
     */
    public void loadPage(int page) {
        currentPage = page;
        gridGameContainer.getChildren().clear();
        
        List<Game> games = ApiUtils.fetchGames(page, gamesPerPage);
        
        if (games != null && !games.isEmpty()) {
            int column = 0;
            int row = 0;
            for (Game game : games) {
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
            btnNext.setDisable(games.size() < gamesPerPage);
        } else {
            btnNext.setDisable(true);
        }
    }
    
    /**
     * Obtiene el número de la página actual.
     *
     * @return el número de la página que se está mostrando
     */
    public int getCurrentPage() {
        return currentPage;
    }
    
    /**
     * Carga la página anterior de juegos.
     * <p>
     * Este método se invoca cuando el usuario hace clic en el botón de "anterior". Decrementa la
     * página actual y actualiza la visualización de juegos acorde a la modalidad (búsqueda, filtros, etc.).
     * </p>
     */
    @FXML
    public void loadPreviousPage() {
        if (currentPage > 1) {
            currentPage--;
            loadCurrentModePage();
        }
    }
    
    /**
     * Carga la página siguiente de juegos.
     * <p>
     * Este método se invoca cuando el usuario hace clic en el botón de "siguiente". Incrementa la
     * página actual y actualiza la visualización de juegos acorde a la modalidad (búsqueda, filtros, etc.).
     * </p>
     */
    @FXML
    public void loadNextPage() {
        currentPage++;
        loadCurrentModePage();
    }
    
    /**
     * Carga la página actual de juegos según la modalidad activa (búsqueda y/o filtros).
     * <p>
     * Este método determina qué consulta realizar a la API en función de si hay una consulta de búsqueda
     * y/o filtros de géneros y plataformas activos, y actualiza la cuadrícula con los juegos obtenidos.
     * También se ajustan los botones de navegación según el número de juegos obtenidos.
     * </p>
     */
    private void loadCurrentModePage() {
        gridGameContainer.getChildren().clear();
        List<Game> games = null;

        if ((currentSearchQuery != null && !currentSearchQuery.isEmpty())
                && ((currentGenres != null && !currentGenres.isEmpty()) || (currentPlatforms != null && !currentPlatforms.isEmpty()))) {
            games = ApiUtils.fetchGamesByNameAndFilters(currentSearchQuery, currentGenres, currentPlatforms, currentPage, gamesPerPage);
        } else if (currentSearchQuery != null && !currentSearchQuery.isEmpty()) {
            games = ApiUtils.fetchGamesByName(currentSearchQuery, currentPage, gamesPerPage);
        } else if ((currentGenres != null && !currentGenres.isEmpty()) || (currentPlatforms != null && !currentPlatforms.isEmpty())) {
            games = ApiUtils.fetchGamesByFilters(currentGenres, currentPlatforms, currentPage, gamesPerPage);
        } else {
            games = ApiUtils.fetchGames(currentPage, gamesPerPage);
        }

        if (games != null && !games.isEmpty()) {
            int column = 0;
            int row = 0;
            for (Game game : games) {
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
            btnNext.setDisable(games.size() < gamesPerPage);
            btnPrevious.setDisable(currentPage == 1);
        } else {
            btnNext.setDisable(true);
            btnPrevious.setDisable(true);
        }
    }

    /**
     * Realiza una búsqueda de juegos basada en la consulta proporcionada y actualiza la cuadrícula.
     * <p>
     * Se reinicia la paginación (se vuelve a la primera página), se limpia la cuadrícula y se obtienen
     * los juegos que coinciden con la consulta. Si hay filtros activos (géneros o plataformas), se aplican
     * en combinación con la búsqueda.
     * </p>
     *
     * @param query la cadena de búsqueda para filtrar juegos por nombre
     */
    public void searchGames(String query) {
        currentSearchQuery = query;
        currentPage = 1;
        gridGameContainer.getChildren().clear();
        List<Game> games = null;
        
        if ((currentGenres != null && !currentGenres.isEmpty()) || (currentPlatforms != null && !currentPlatforms.isEmpty())) {
            games = ApiUtils.fetchGamesByNameAndFilters(query, currentGenres, currentPlatforms, currentPage, gamesPerPage);
        } else {
            games = ApiUtils.fetchGamesByName(query, currentPage, gamesPerPage);
        }
        
        if (games != null && !games.isEmpty()) {
            int column = 0;
            int row = 0;
            for (Game game : games) {
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
            btnNext.setDisable(games.size() < gamesPerPage);
            btnPrevious.setDisable(true);
        } else {
            System.out.println("No se encontraron juegos que coincidan con: " + query);
            btnNext.setDisable(true);
            btnPrevious.setDisable(true);
        }
    }
    
    /**
     * Aplica filtros de géneros y plataformas para mostrar juegos que cumplan los criterios.
     * <p>
     * Se reinicia la paginación (se vuelve a la primera página) y se limpia la cuadrícula. Si hay una
     * consulta de búsqueda activa, se combinan con los filtros. Luego se obtienen y muestran los juegos
     * que cumplen los criterios.
     * </p>
     *
     * @param genres    cadena que representa los géneros seleccionados para filtrar
     * @param platforms cadena que representa las plataformas seleccionadas para filtrar
     */
    public void filterGames(String genres, String platforms) {
        currentGenres = genres;
        currentPlatforms = platforms;
        currentPage = 1;
        gridGameContainer.getChildren().clear();
        List<Game> games = null;
        
        if (currentSearchQuery != null && !currentSearchQuery.isEmpty()) {
            games = ApiUtils.fetchGamesByNameAndFilters(currentSearchQuery, genres, platforms, currentPage, gamesPerPage);
        } else {
            games = ApiUtils.fetchGamesByFilters(genres, platforms, currentPage, gamesPerPage);
        }
        
        if (games != null && !games.isEmpty()) {
            int column = 0;
            int row = 0;
            for (Game game : games) {
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
            btnNext.setDisable(games.size() < gamesPerPage);
            btnPrevious.setDisable(true);
        } else {
            System.out.println("No se encontraron juegos con los filtros aplicados.");
            btnNext.setDisable(true);
            btnPrevious.setDisable(true);
        }
    }
    
    /**
     * Limpia cualquier búsqueda o filtro activo y recarga la página actual de juegos.
     */
    public void clearSearchAndFilters() {
        currentSearchQuery = "";
        currentGenres = "";
        currentPlatforms = "";
        loadPage(currentPage);
    }
}
