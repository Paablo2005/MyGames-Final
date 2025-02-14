package test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.concurrent.TimeUnit;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import models.User;
import utils.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test; 
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

public class RegisterTest extends ApplicationTest {

	@Override
	public void start(Stage stage) throws Exception {
	    // Carga del FXML
	    Parent root = FXMLLoader.load(getClass().getResource("/views/RegisterDataPane.fxml"));
	    stage.setScene(new Scene(root));
	    stage.show();
	}

    /**
     * Verifica que al ingresar contraseñas diferentes se muestre la alerta correspondiente.
     */
    @Test
    public void testPasswordMismatch() {
        // Simula ingreso de datos
        clickOn("#textEmail").write("usuario@example.com");
        clickOn("#textPassword1").write("password123");
        clickOn("#textPassword2").write("passwordDiferente");
        clickOn("#textUsername").write("usuario");
        clickOn("#btnLogin");

        // Espera a que se muestre el Alert (diálogo)
        WaitForAsyncUtils.waitForFxEvents();

        // Verifica que se muestra una alerta con el mensaje esperado
        DialogPane dialogPane = lookup(".dialog-pane").query();
        assertTrue(dialogPane.getContentText().contains("Passwords do not match."));
        WaitForAsyncUtils.sleep(3, TimeUnit.SECONDS);
        // Cierra la alerta (buscando el botón 'OK')
        clickOn("OK");
    }

    /**
     * Verifica que al ingresar un email inválido se muestre la alerta correspondiente.
     */
    @Test
    public void testInvalidEmail() {
        clickOn("#textEmail").write("usuarioexample.com"); // falta '@'
        clickOn("#textPassword1").write("password123");
        clickOn("#textPassword2").write("password123");
        clickOn("#textUsername").write("usuario");
        clickOn("#btnLogin");

        WaitForAsyncUtils.waitForFxEvents();
        DialogPane dialogPane = lookup(".dialog-pane").query();
        assertTrue(dialogPane.getContentText().contains("Invalid email address."));
        WaitForAsyncUtils.sleep(3, TimeUnit.SECONDS);
        clickOn("OK");
    }

    /**
     * Verifica que al dejar el username vacío se muestre la alerta correspondiente.
     */
    @Test
    public void testEmptyUsername() {
        clickOn("#textEmail").write("usuario@example.com");
        clickOn("#textPassword1").write("password123");
        clickOn("#textPassword2").write("password123");
        // No se ingresa username
        clickOn("#btnLogin");

        WaitForAsyncUtils.waitForFxEvents();
        DialogPane dialogPane = lookup(".dialog-pane").query();
        assertTrue(dialogPane.getContentText().contains("Username cannot be empty."));
        WaitForAsyncUtils.sleep(3, TimeUnit.SECONDS);
        clickOn("OK");
    }

    /**
     * Verifica que al ingresar una contraseña muy corta se muestre la alerta correspondiente.
     */
    @Test
    public void testShortPassword() {
        clickOn("#textEmail").write("usuario@example.com");
        clickOn("#textPassword1").write("pass"); // menos de 6 caracteres
        clickOn("#textPassword2").write("pass");
        clickOn("#textUsername").write("usuario");
        clickOn("#btnLogin");

        WaitForAsyncUtils.waitForFxEvents();
        DialogPane dialogPane = lookup(".dialog-pane").query();
        assertTrue(dialogPane.getContentText().contains("Password must be at least 6 characters long."));
        WaitForAsyncUtils.sleep(3, TimeUnit.SECONDS);
        clickOn("OK");
    }

    /**
     * Verifica el registro exitoso.
     * Nota: Para que esta prueba sea confiable, se debe trabajar sobre una base de datos de test (por ejemplo, in-memory)
     * o limpiar los datos entre pruebas.
     */
    @Test
    public void testValidRegistration() {
        String email = "usuarioValido@example.com";
        eliminarUsuario(email);
        clickOn("#textEmail").write("usuarioValido@example.com");
        clickOn("#textPassword1").write("password123");
        clickOn("#textPassword2").write("password123");
        clickOn("#textUsername").write("usuarioValido");
        clickOn("#btnLogin");

        WaitForAsyncUtils.waitForFxEvents();
        DialogPane dialogPane = lookup(".dialog-pane").query();
        assertTrue(dialogPane.getContentText().contains("User created successfully!"));
        WaitForAsyncUtils.sleep(3, TimeUnit.SECONDS);
        clickOn("OK");
    }

    /**
     * Verifica el caso en el que se intenta registrar un email ya existente.
     * Para simular este caso, primero se realiza un registro exitoso y luego se intenta registrar de nuevo con el mismo email.
     */
    @Test
    public void testDuplicateEmail2() {
        String email = "duplicado@example.com";
        String password = "password123";
        String userName = "usuarioDuplicado";
        
        // Elimina el usuario si ya existe para asegurar un estado inicial limpio
        eliminarUsuario(email);
        
        // Inserta directamente en la base de datos un usuario con ese email
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            User user = new User();
            user.setEmail(email);
            user.setPassword(password); // En producción, usar hash
            user.setUsername(userName);
            session.persist(user);
            tx.commit();
            System.out.println("Usuario duplicado insertado manualmente.");
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        
        // Reinicia la interfaz para obtener un formulario limpio
        interact(() -> {
            try {
                Stage stage = (Stage) lookup("#textEmail").query().getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("/views/RegisterDataPane.fxml"));
                stage.getScene().setRoot(root);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        WaitForAsyncUtils.waitForFxEvents();
        WaitForAsyncUtils.sleep(2, TimeUnit.SECONDS);
        
        // Realiza el intento de registro con el mismo email usando la interfaz
        clickOn("#textEmail").write(email);
        clickOn("#textPassword1").write(password);
        clickOn("#textPassword2").write(password);
        // Usamos otro nombre de usuario para distinguirlo
        clickOn("#textUsername").write("otroUsuario");
        clickOn("#btnLogin");
        
        WaitForAsyncUtils.waitForFxEvents();
        DialogPane dialogPane = lookup(".dialog-pane").query();
        // Se espera que la alerta indique que el email ya existe
        assertTrue(dialogPane.getContentText().contains("A user with this email already exists."));
        WaitForAsyncUtils.sleep(3, TimeUnit.SECONDS);
        clickOn("OK");
    }
    
    /**
     * Método auxiliar que elimina de la base de datos el usuario con el email indicado, si existe.
     */
    @SuppressWarnings("deprecation")
    private void eliminarUsuario(String email) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            User user = session.createQuery("FROM User WHERE email = :email", User.class)
                               .setParameter("email", email)
                               .uniqueResult();
            if (user != null) {
                session.delete(user);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

}
