package online.caltuli.consumer.dao.implementations;

import online.caltuli.consumer.dao.exceptions.DaoException;
import online.caltuli.consumer.dao.DaoFactory;
import online.caltuli.consumer.dao.LoggingExtension;

import online.caltuli.model.CurrentModel;
import online.caltuli.model.User;

import online.caltuli.model.exceptions.user.UserException;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

@ExtendWith(MockitoExtension.class)
@Tag("UsersDaoImpl")
@DisplayName("Succeeding in manipulating the users tables.")
public class UsersDaoImplTest {

    @Mock
    private DaoFactory daoFactory;
    @Mock
    private CurrentModel currentModel;
    @Mock
    private Connection connection;
    @InjectMocks
     private UsersDaoImpl usersDaoImpl;

    @Nested
    @Tag("AddUser")
    @DisplayName("Succeeding in adding a user")
    @ExtendWith(LoggingExtension.class)
    //@Disabled("Stopped because ... but I will fix it later")
    public class AddUserTest {
        private Logger logger;
        public void setLogger(Logger logger) {
            this.logger = logger;
        }

        @BeforeEach
        void nestedSetUp() {
            usersDaoImpl = new UsersDaoImpl(daoFactory, currentModel);
        }

        /* draft
        @Test
        @DisplayName("")
        void () throws {
        }
        */


        @Test
        @DisplayName("addUser_ShouldReturnNotNullUserInstance_WhenGivenValidUserDetails")
        @Disabled("next work to do")
        void addUser_ShouldReturnNotNullUserInstance_WhenGivenValidUserDetails() throws SQLException, UserException {

            // Arrange
            String username = "validUsername";
            String passwordHash = "validpasswordHash";
            String email = "valid@emailAddress";
            String message = "validMessage";

            Connection connection = mock(Connection.class);
            PreparedStatement preparedStatement = mock(PreparedStatement.class);
            ResultSet generatedKeys = mock(ResultSet.class);

            when(daoFactory.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1); // Simuler qu'une ligne a été affectée
            when(preparedStatement.getGeneratedKeys()).thenReturn(generatedKeys);
            when(generatedKeys.next()).thenReturn(true); // Simuler qu'une clé générée est disponible
            when(generatedKeys.getInt(1)).thenReturn(1); // Simuler la valeur de la clé générée

            // Act
            User user = null;
            try {
                user = usersDaoImpl.addUser(username, passwordHash, email, message);
            } catch (DaoException e) {
                // Gérer l'exception si nécessaire
            }

            // Assert
            assertThat(user)
                    .withFailMessage(
                            "Expected addUser to return not null user instance indicating successful user addition")
                    .isNotNull();
        }

        @Test
        @DisplayName("addUser_ShouldThrowException_WhenUsernameIsTheNullString")
        @Disabled("next work to do")
        void addUser_ShouldThrowException_WhenUsernameIsTheNullString() {
            // Arrange
            String theNullString = "";
            String passwordHash = "validPasswordHash";
            String email = "valid@email.com";
            String message = "validMessage";

            // Act & Assert
            assertThrows(DaoException.class, () -> {
                usersDaoImpl.addUser(theNullString, passwordHash, email, message);
            }, "Expected addUser to throw DaoException for empty string for username");
        }

        /* TEST
        @Test
        @DisplayName("Given a new user Leo, when registering, then he should be correctly added to the User table")
        void testLeoShouldBeRegistredInUserTable() throws DaoException, SQLException {
            PreparedStatement preparedStatement = mock(PreparedStatement.class);
            ResultSet generatedKeys = mock(ResultSet.class);

            // Configurer le comportement de Connection pour retourner le PreparedStatement mocké
            when(daoFactory.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);

            // Configurer le comportement de PreparedStatement pour simuler une exécution réussie et retourner le ResultSet mocké
            when(preparedStatement.executeUpdate()).thenReturn(1); // Simuler une ligne affectée
            when(preparedStatement.getGeneratedKeys()).thenReturn(generatedKeys);

            // Configurer le comportement de ResultSet pour simuler la récupération d'un ID généré
            when(generatedKeys.next()).thenReturn(true); // Simuler la présence d'une clé générée
            when(generatedKeys.getInt(1)).thenReturn(42); // Simuler l'id généré

            // Act: Appel de la méthode à tester
            String username = "Léo";
            String passwordHash = "$2a$10$lE4AMX1zCOkmFKEJ5kF/D.mEo73UMLdLc9s3zuCto0WKjqS8fIjLC";
            String email = "Leo@Leo.wouf";
            String message = "Wouf !";
            int userId = usersDaoImpl.addUser(username, passwordHash, email, message);

            // Assert: Vérifie que la méthode retourne un ID valide (différent de -1)
            assertThat(userId)
                    .withFailMessage("The user Léo has not been added correctly.")
                    .isNotEqualTo(-1);

            logger.info("coucou !");

            // Vérifications supplémentaires pour s'assurer que les méthodes mockées ont été appelées comme prévu
            verify(connection).prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS));
            verify(preparedStatement).setString(1, username);
            verify(preparedStatement).setString(2, passwordHash);
            verify(preparedStatement).setString(3, email);
            verify(preparedStatement).setString(4, message);
            verify(preparedStatement).executeUpdate();
            verify(generatedKeys).next();
            verify(generatedKeys).getInt(1);
        }
         */
    }
}
