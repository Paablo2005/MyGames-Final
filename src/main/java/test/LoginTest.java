package test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import models.User;
import utils.HibernateUtil;
import utils.PasswordUtil;

public class LoginTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        // Carga de la vista de login (asegúrate de que la ruta sea correcta)
        Parent root = FXMLLoader.load(getClass().getResource("/views/LoginDataPane.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Antes de cada prueba se crea un usuario de prueba con email "test@example.com"
     * y password "password123". Así se podrá testear el login exitoso.
     */
    @BeforeEach
    public void setUp() throws Exception {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            // Se crea o actualiza el usuario de prueba
            User user = new User("test@example.com", PasswordUtil.hashPassword("password123"), "");
            user.setUsername("testuser");
            session.merge(user);
            tx.commit();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Después de cada prueba se elimina el usuario de prueba y se cierran las ventanas.
     */
    @SuppressWarnings("deprecation")
	@AfterEach
    public void tearDown() throws Exception {
        // Elimina el usuario de prueba
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            User user = session.createQuery("FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", "test@example.com")
                    .uniqueResult();
            if(user != null) {
                session.delete(user);
            }
            tx.commit();
        } catch(Exception e) {
            e.printStackTrace();
        }
        FxToolkit.hideStage();
        FxToolkit.cleanupStages();
    }

    /**
     * Caso 1: Credenciales vacías.
     * Se espera que se muestre una alerta con el mensaje "Invalid email or password."
     */
    @Test
    public void testEmptyCredentials() {
        WaitForAsyncUtils.sleep(3, TimeUnit.SECONDS);
        // No se ingresa nada en los campos
        clickOn("#btnLogin");

        WaitForAsyncUtils.waitForFxEvents();
        DialogPane dialogPane = lookup(".dialog-pane").query();
        // Espera 3 segundos para leer la alerta
        WaitForAsyncUtils.sleep(3, TimeUnit.SECONDS);
        assertTrue(dialogPane.getContentText().contains("Invalid email or password."));
        clickOn("OK");
    }

    /**
     * Caso 2: Email inexistente.
     */
    @Test
    public void testInvalidEmail() {
        clickOn("#textEmail").write("nonexistent@example.com");
        clickOn("#textPassword").write("password123");
        clickOn("#btnLogin");

        WaitForAsyncUtils.waitForFxEvents();
        DialogPane dialogPane = lookup(".dialog-pane").query();
        WaitForAsyncUtils.sleep(3, TimeUnit.SECONDS);
        assertTrue(dialogPane.getContentText().contains("Invalid email or password."));
        clickOn("OK");
    }

    /**
     * Caso 3: Contraseña incorrecta para un email existente.
     */
    @Test
    public void testInvalidPassword() {
        clickOn("#textEmail").write("test@example.com");
        clickOn("#textPassword").write("wrongpassword");
        clickOn("#btnLogin");

        WaitForAsyncUtils.waitForFxEvents();
        DialogPane dialogPane = lookup(".dialog-pane").query();
        WaitForAsyncUtils.sleep(3, TimeUnit.SECONDS);
        assertTrue(dialogPane.getContentText().contains("Invalid email or password."));
        clickOn("OK");
    }

    /**
     * Caso 4: Login exitoso.
     * Se espera que al ingresar credenciales correctas se abra la ventana principal ("Main Pane")
     * y se cierre la ventana de login.
     */
    @Test
    public void testValidLogin() {
        clickOn("#textEmail").write("test@example.com");
        clickOn("#textPassword").write("password123");
        clickOn("#btnLogin");

        // Espera a que se efectúen las transiciones de la interfaz
        WaitForAsyncUtils.waitForFxEvents();
        // Se verifica que se ha abierto una nueva ventana con el título "Main Pane"
        boolean mainPaneOpened = listTargetWindows().stream()
        	    .filter(window -> window instanceof Stage)
        	    .map(window -> (Stage) window)
        	    .anyMatch(stage -> "Main Pane".equals(stage.getTitle()));
        // Se espera 3 segundos para asegurar que la ventana se cargó correctamente
        WaitForAsyncUtils.sleep(3, TimeUnit.SECONDS);
        assertTrue(mainPaneOpened, "Main Pane should be opened on valid login");
    }
}
