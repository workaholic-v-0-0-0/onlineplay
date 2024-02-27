package online.caltuli.model;

import online.caltuli.model.exceptions.user.UserException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.Collections;

@Tag("User")
@DisplayName("User class tests")
class UserTest {

    @Nested
    @Tag("UsernameValidation")
    @DisplayName("Username Validation Tests")
    public class setUsernameTest {

        private User user;

        @BeforeEach
        void setUp() {
            this.user = new User();
        }

        @Test
        @DisplayName("Should throw UserException for an empty username")
        void setUsername_ShouldThrowException_WhenUsernameIsEmpty() {
            // Arrange
            String emptyUsername = "";

            // Act & Assert
            assertThatThrownBy(() -> user.setUsername(emptyUsername))
                    .withFailMessage("Expected setUsername to throw UserException for empty username")
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining("Invalid username");
        }

        @ParameterizedTest
        @ValueSource(strings = {"us*", "us#", " ", "user name", "us!", "us@", "12", "aaaaaaaaaaaaaaaaaaaaa"}) // Replace "a".repeat(21) with the actual string
        @DisplayName("Should throw UserException for usernames with invalid characters or lengths")
        void setUsername_ShouldThrowException_WhenUsernameHasInvalidCharacters(String username) {
            User user = new User(); // Initialisation d'une instance User

            // Act & Assert
            assertThatThrownBy(() -> user.setUsername(username))
                    .withFailMessage("Expected setUsername to throw UserException for username with invalid characters or lengths: " + username)
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining("Invalid username");
        }

        @Test
        @DisplayName("Should not throw UserException for a valid username and set username correctly")
        void setUsername_ShouldSetUsernameCorrectly_WhenUsernameIsValid() {
            // Arrange
            String validUsername = "validUser123";

            // Act and Assert
            assertThatCode(() -> user.setUsername(validUsername))
                    .doesNotThrowAnyException();
            assertThat(user.getUsername()).isEqualTo(validUsername);
        }

        @ParameterizedTest
        @ValueSource(strings = {"user123", "valid_user", "valid-user-123"})
        @DisplayName("Should accept valid usernames and set them correctly")
        void setUsername_ShouldSetUsernameCorrectly_ForValidUsernames(String validUsername) {
            // Act and Assert
            assertThatCode(() -> user.setUsername(validUsername))
                    .doesNotThrowAnyException();
            assertThat(user.getUsername()).isEqualTo(validUsername);
        }
    }

    @Nested
    @Tag("PasswordHashValidation")
    @DisplayName("Password Hash Validation Tests")
    class SetPasswordHashTest {

        private User user;

        @BeforeEach
        void setUp() {
            this.user = new User();
        }

        @Test
        @DisplayName("Should throw UserException for an invalid BCrypt hash")
        void setPasswordHash_ShouldThrowException_WhenHashIsInvalid() {
            // Arrange
            String invalidHash = "someInvalidHash";

            // Act & Assert
            assertThatThrownBy(() -> user.setPasswordHash(invalidHash))
                    .withFailMessage("Expected setPasswordHash to throw UserException for invalid BCrypt hash")
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining("Invalid password hash");
        }

        @Test
        @DisplayName("Should accept a valid BCrypt hash and set password hash correctly")
        void setPasswordHash_ShouldSetPasswordHashCorrectly_WhenHashIsValid() {
            // Arrange
            String validHash = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZloKn5lHm";

            // Act & Assert
            assertThatCode(() -> user.setPasswordHash(validHash))
                    .withFailMessage("Expected setPasswordHash to not throw any exception for valid BCrypt hash")
                    .doesNotThrowAnyException();
            assertThat(user.getPasswordHash()).isEqualTo(validHash);
        }

        @ParameterizedTest
        @ValueSource(strings = {"$2a$10$invalidHashLength", "$1a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZloKn5lHm", "incorrectFormat"})
        @DisplayName("Should throw UserException for BCrypt hashes with invalid format or length")
        void setPasswordHash_ShouldThrowException_WhenBCryptHashFormatOrLengthIsInvalid(String hash) {
            // Act & Assert
            assertThatThrownBy(() -> user.setPasswordHash(hash))
                    .withFailMessage("Expected setPasswordHash to throw UserException for BCrypt hash with invalid format or length: " + hash)
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining("Invalid password hash");
        }
    }

    @Nested
    @Tag("EmailValidation")
    @DisplayName("Email Validation Tests")
    class SetEmailTest {

        private User user;

        @BeforeEach
        void setUp() {
            user = new User();
        }

        @Test
        @DisplayName("Should throw UserException for an invalid email format")
        void setEmail_ShouldThrowException_WhenEmailIsInvalid() {
            // Arrange
            String invalidEmail = "invalidEmail";

            // Act & Assert
            assertThatThrownBy(() -> user.setEmail(invalidEmail))
                    .withFailMessage("Expected setEmail to throw UserException for invalid email format")
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining("Invalid email format");
        }

        @ParameterizedTest
        @ValueSource(strings = {"user@example.com", "valid.email+123@example.co.uk", "user.name@example.info"})
        @DisplayName("Should not throw UserException for a valid email and set email correctly")
        void setEmail_ShouldSetEmailCorrectly_WhenEmailIsValid(String validEmail) {
            // Act & Assert
            assertThatCode(() -> user.setEmail(validEmail))
                    .doesNotThrowAnyException();
            assertThat(user.getEmail()).isEqualTo(validEmail);
        }
    }

    @Nested
    @Tag("MessageValidation")
    @DisplayName("Message Validation Tests")
    class SetMessageTest {

        private User user;

        @BeforeEach
        void setUp() {
            user = new User();
        }

        @Test
        @DisplayName("Should set message correctly for valid message")
        void setMessage_ShouldSetMessageCorrectly_WhenMessageIsValid() {
            // Arrange
            String validMessage = "This is a valid message.";

            // Act & Assert
            assertThatCode(() -> user.setMessage(validMessage))
                    .doesNotThrowAnyException();
            assertThat(user.getMessage()).isEqualTo(validMessage);
        }

        @Test
        @DisplayName("Should throw UserException for message with invalid characters")
        void setMessage_ShouldThrowException_WhenMessageHasInvalidCharacters() {
            // Arrange
            String invalidMessage = "<script>alert('Invalid');</script>";

            // Act & Assert
            assertThatThrownBy(() -> user.setMessage(invalidMessage))
                    .withFailMessage("Expected setMessage to throw UserException for message with invalid characters")
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining("Message contains invalid characters");
        }

        @Test
        @DisplayName("Should throw UserException for overly long message")
        void setMessage_ShouldThrowException_WhenMessageIsTooLong() {
            // Arrange
            String overlyLongMessage = String.join("", Collections.nCopies(255, "a"));

            // Act & Assert
            assertThatThrownBy(() -> user.setMessage(overlyLongMessage))
                    .withFailMessage("Expected setMessage to throw UserException for overly long message")
                    .isInstanceOf(UserException.class)
                    .hasMessageContaining("Message contains invalid characters or exceeds the maximum length of 254 characters.");
        }
    }
}