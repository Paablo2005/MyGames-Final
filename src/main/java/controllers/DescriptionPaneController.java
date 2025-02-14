package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import utils.ApiUtils;
import utils.HibernateUtil;
import models.Game;
import models.GameApi;
import java.util.ArrayList;
import java.util.List;
import dao.GameDaoImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;


public class DescriptionPaneController {

    @FXML
    private ImageView gameImage;
    @FXML
    private Label gameTitle;
    @FXML
    private Label gameDeveloper;
    @FXML
    private Label gameGenre;
    @FXML
    private ImageView screenshot1;
    @FXML
    private ImageView screenshot2;
    @FXML
    private ImageView screenshot3;
    
    @FXML
    private TextArea gameDescription;

    @FXML
    private VBox descriptionContainer;
    
    @FXML
    private Button btnAdd;
    
    private Game currentGame;
    private GameDaoImpl gameDao;
    private EntityManagerFactory emf;

    public void initialize() {
        loadGameData();
        adjustTextAreaHeight();
        
        // Agregar un listener para que el TextArea cambie su tamaño dinámicamente cuando el texto cambie
        gameDescription.textProperty().addListener((observable, oldValue, newValue) -> {
            adjustTextAreaHeight();
        });
    }

    private void loadGameData() {
        // Obtener lista de juegos desde la API
      List<GameApi> games = new ArrayList<>();
      for (Game game : ApiUtils.fetchGames(1, 1)) {
          games.add(new GameApi(game.getName(), game.getPrincipalImg()));
      }

        if (!games.isEmpty()) {
            GameApi game = games.get(0); // Tomamos el primer juego

            // Actualizar el texto en la interfaz
            gameTitle.setText(game.getName());
            gameDescription.setText("Metacritic: " + game.getMetacritic() + " | Playtime: " + game.getPlaytime() + " hrs");
            gameDeveloper.setText("Plataformas: " + game.getPlatforms());
            gameGenre.setText("Géneros: " + game.getGenres());

            // Cargar imagen principal del juego
            if (game.getImage() != null && !game.getImage().isEmpty()) {
                gameImage.setImage(new Image(game.getImage()));
            }

            // Cargar imágenes de capturas de pantalla (ejemplo usando la misma imagen, ya que GameApi no tiene screenshots)
            if (game.getImage() != null) {
                screenshot1.setImage(new Image(game.getImage()));
                screenshot2.setImage(new Image(game.getImage()));
                screenshot3.setImage(new Image(game.getImage()));
            }
        } else {
            System.out.println("No se encontraron juegos en la API.");
        }
    }
    
    private void adjustTextAreaHeight() {
      // Ajustar la altura en base al contenido del texto
      gameDescription.setPrefHeight(gameDescription.getText().split("\n").length * 20 + 100);
  }


    public void setGameDetails(Game game) {
      if (game == null) {
          System.out.println("Error: El juego recibido es nulo.");
          return;
      }

      this.currentGame = game;

      // Asignar los valores del juego
      gameTitle.setText(game.getName());
      gameDescription.setText(game.getDescription() != null ? game.getDescription() : "Descripción no disponible.");
      gameDeveloper.setText("Plataformas: " + (game.getPlatforms() != null ? game.getPlatforms() : "Desconocidas"));
      gameGenre.setText("Géneros: " + (game.getGenres() != null ? game.getGenres() : "No disponible"));

      // Cargar la imagen principal
      if (game.getPrincipalImg() != null && !game.getPrincipalImg().isEmpty()) {
          gameImage.setImage(new Image(game.getPrincipalImg()));
      }

      //  Cargar imágenes de capturas de pantalla correctamente
      if (game.getScreenshot1() != null && !game.getScreenshot1().isEmpty()) {
          screenshot1.setImage(new Image(game.getScreenshot1()));
      }
      if (game.getScreenshot2() != null && !game.getScreenshot2().isEmpty()) {
          screenshot2.setImage(new Image(game.getScreenshot2()));
      }
      if (game.getScreenshot3() != null && !game.getScreenshot3().isEmpty()) {
          screenshot3.setImage(new Image(game.getScreenshot3()));
      }

      //  Verificar si el juego ya está guardado y actualizar el botón
      EntityManager em = HibernateUtil.getSessionFactory().createEntityManager();
      boolean isSaved = em.createQuery("SELECT COUNT(g) FROM Game g WHERE g.name = :name", Long.class)
                          .setParameter("name", game.getName())
                          .getSingleResult() > 0;
      em.close();

      if (isSaved) {
          btnAdd.setText("✔");
          btnAdd.setDisable(true);
          System.out.println("El juego ya estaba guardado, botón bloqueado.");
      } else {
          btnAdd.setText("➕");
          btnAdd.setDisable(false);
          btnAdd.setOnAction(event -> addToCollection());
          System.out.println("El juego no estaba guardado, botón activo.");
      }
  }

  // Método para actualizar la UI basado en `GameApi`
  private void updateUIWithGameDetails(GameApi game) {
      gameTitle.setText(game.getName());
      gameDescription.setText("Metacritic: " + game.getMetacritic() + " | Playtime: " + game.getPlaytime() + " hrs");
      gameDeveloper.setText("Plataformas: " + game.getPlatforms());
      gameGenre.setText("Géneros: " + game.getGenres());

      // Cargar imagen principal
      if (game.getImage() != null && !game.getImage().isEmpty()) {
          gameImage.setImage(new Image(game.getImage()));
      }

      // Cargar imágenes de capturas de pantalla
      if (game.getImage() != null) {
          screenshot1.setImage(new Image(game.getImage()));
          screenshot2.setImage(new Image(game.getImage()));
          screenshot3.setImage(new Image(game.getImage()));
      }
      
   // Asignar acción al botón de añadir
      btnAdd.setOnAction(event -> addToCollection());
  }

  // Método para guardar el juego en la base de datos
  @FXML
  private void addToCollection() {
      if (currentGame == null) {
          System.out.println("Error: currentGame es null. No se puede añadir a la colección.");
          return;
      }

      EntityManager em = HibernateUtil.getSessionFactory().createEntityManager();
      try {
          em.getTransaction().begin();

          // Verificar si el juego ya existe en la base de datos
          Game existingGame = em.createQuery("SELECT g FROM Game g WHERE g.name = :name", Game.class)
                                .setParameter("name", currentGame.getName())
                                .getResultStream()
                                .findFirst()
                                .orElse(null);

          if (existingGame == null) {
              // ✅ Generar un ID único si el juego no tiene uno
              if (currentGame.getId() == null) {
                  currentGame.setId(generateGameId(em));
                  System.out.println("Se asignó el ID: " + currentGame.getId() + " al juego: " + currentGame.getName());
              }

              em.persist(currentGame);
              System.out.println("Juego guardado en la base de datos con ID: " + currentGame.getId());
          } else {
              System.out.println("El juego ya está en la colección, no se añadirá de nuevo.");
          }

          em.getTransaction().commit();

          // ✅ Actualizar UI
          Platform.runLater(() -> {
              btnAdd.setText("✔");
              btnAdd.setDisable(true);
              System.out.println("Botón actualizado en la UI.");
          });

      } catch (Exception e) {
          if (em.getTransaction().isActive()) {
              em.getTransaction().rollback();
          }
          e.printStackTrace();
      } finally {
          em.close();
      }
  }
  
  private Integer generateGameId(EntityManager em) {
    Integer maxId = em.createQuery("SELECT COALESCE(MAX(g.id), 0) FROM Game g", Integer.class).getSingleResult();
    return maxId + 1; // Generar un nuevo ID único
  }
}