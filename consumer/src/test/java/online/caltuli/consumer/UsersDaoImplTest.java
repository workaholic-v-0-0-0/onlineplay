package online.caltuli.consumer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Nested;

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
public class UsersDaoImplTest {

    @Mock
    private DaoFactory daoFactory;

    @Mock
    private Connection connection;

    @InjectMocks
    private UsersDaoImpl usersDaoImpl;

    @Nested
    public class AddTest {
        @Test
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
    }
}
