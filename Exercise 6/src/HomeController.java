import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class HomeController implements Initializable { //ung nasa recording ni sir regarding initializable
    // Observable list to store user data
    ObservableList<User> mylist = FXCollections.observableArrayList();

    @FXML
    private TextField usernameTF, passwordTF, emailTF, latestusernameTF; //for create, update, delete
    @FXML
    private Button createbutton, deletebutton, updatebutton; //new ^^ 

    @FXML
    private Label nameLabel, usernamLabel, passwordLabel; //new ^^ 

    @FXML 
    private AnchorPane panehome; 

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableColumn<User, String> passwordColumn;

    @FXML
    private TableColumn<User, String> accountcreatedcol;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private TableView<User> hometable;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize columns and load data
        initializeCol();
        loadData();
    }

    // Method to initialize the columns in the table
    private void initializeCol() {
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("currentUsername"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        accountcreatedcol.setCellValueFactory(new PropertyValueFactory<>("accountCreated")); // Fixed
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
    }
    
    // Method to load data from the database into the table
    private void loadData() {
        mylist.clear(); // Clear the current list
        ResultSet result = DatabaseHandler.displayusers(); // Get user data from database
        try {
            while (result.next()) {
                // Extract user data from the ResultSet
                String username = result.getString("Username");
                String password = result.getString("Password");
                String email = result.getString("Email");
                String accountcreated = result.getString("AccountCreated");

                // Add the user data to the ObservableList
                mylist.add(new User(username, "", password, email, accountcreated));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        hometable.setItems(mylist); // Set the table's items to the ObservableList
        hometable.refresh();
    }

    private String currentUsername;

    // Method to display the username in the label
    public void displayName(String currentUsername) {
        nameLabel.setText("Welcome to Waze, " + currentUsername);
    }

    // Method to set the username and update the label
    public void setUsername(String currentUsername) {
        this.currentUsername = currentUsername;
        displayName(currentUsername);  // Call displayName to update the label
        System.out.println("Username set: " + this.currentUsername);
    }
    //DELETE BUTTON 
    @FXML
    private void deletebuttonHandler(ActionEvent event) throws IOException {
        String currentUsername = usernameTF.getText().trim().toLowerCase(); // Convert username to lowercase
        String password = passwordTF.getText().trim();
        String email = emailTF.getText().trim();
       // Validate inputs (username and password should not be empty)
       if (currentUsername.isEmpty() && password.isEmpty() && email.isEmpty()) {
        showError("Please fill all fields.");
        return;
    }
    // Validate email format
     if (!isValidEmail(email)) {
        showError("Please enter a valid email address (must be @gmail.com or @yahoo.com).");
        return;
    }
       
        // confirmation dialogue
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Account");
        alert.setHeaderText("Are you sure you want to permanently delete your account?");
        alert.setContentText("This action cannot be undone.");
        // Wait for user confirmation
        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
    
        if (result == ButtonType.OK) {
            if (DatabaseHandler.deleteAccount(currentUsername)) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Account Deleted");
                successAlert.setHeaderText("Your account has been successfully deleted.");
                successAlert.showAndWait();
                // // Close current window (Home Scene)
                // Stage stage = (Stage) deletebutton.getScene().getWindow();
                // stage.close();
                // // Close the database connection
                // DatabaseHandler.closeConnection();
                // Refresh the table data
                loadData();
            } else {
                //deletion fails
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Account deletion failed.");
                errorAlert.setContentText("Please try again later.");
                errorAlert.showAndWait();
            }
        }
    }
    //CREATE
     @FXML
     public void createbuttonHandler (ActionEvent event) throws IOException {
         // Get the username and password from the text fields, trimming any unnecessary
         // whitespace
         String username = usernameTF.getText().trim().toLowerCase(); // Convert username to lowercase
         String password = passwordTF.getText().trim();
         String email = emailTF.getText().trim();
 
         // Validate inputs (username and password should not be empty)
         if (username.isEmpty() && password.isEmpty() && email.isEmpty()) {
             showError("Please fill in all fields.");
             return;
         }

         // Validate email format
         if (!isValidEmail(email)) {
        showError("Please enter a valid email address (must be @gmail.com or @yahoo.com).");
        return;
    }
 
         // Connection and PreparedStatement objects to interact with the database
         Connection connection = null;
         PreparedStatement pInsert = null;
         PreparedStatement pCheckUserExists = null;
         ResultSet resultSet = null;
 
         try {
             // Establish the database connection
             connection = DriverManager.getConnection(
                     "jdbc:mysql://127.0.0.1:3306/my_db?useSSL=false&serverTimezone=UTC", "root",
                     "ilovecompsci");
 
             // Check if the user already exists in the database (case-insensitive)
             pCheckUserExists = connection.prepareStatement("SELECT * FROM WazerAcc WHERE LOWER(Username) = ?");
             pCheckUserExists.setString(1, username); // Bind the username to the query (already in lowercase)
             resultSet = pCheckUserExists.executeQuery();
 
             // Debugging output to check the result set
             System.out.println("Executing query to check if user exists...");
 
             if (resultSet.next()) {
                 // If the username exists, show an error message
                 System.out.println("User already exists: " + username);
                 showError("User already exists.");
             } else {
                 // If the username doesn't exist, insert the new user into the database
                 System.out.println("User does not exist, creating new user: " + username);
                 pInsert = connection
                         .prepareStatement("INSERT INTO WazerAcc (Username, Password, Email) VALUES (?, ?, ?)");
                 pInsert.setString(1, username);
                 pInsert.setString(2, password);
                 pInsert.setString(3, email);

                 pInsert.executeUpdate(); // Execute the insert query
 
                 loadData(); // Refresh the table data
             }
 
         } catch (SQLException e) {
             // Catch any SQL errors and display them
             e.printStackTrace();
             showError("Database error: " + e.getMessage());
         } finally {
             // Clean up database resources
             try {
                 if (resultSet != null)
                     resultSet.close();
                 if (pCheckUserExists != null)
                     pCheckUserExists.close();
                 if (pInsert != null)
                     pInsert.close();
                 if (connection != null)
                     connection.close();
             } catch (SQLException e) {
                 e.printStackTrace();
             }
         }
     }
     //UPDATE 
     @FXML
     public void updateUser(ActionEvent event) {
         String currentUsername = usernameTF.getText().trim();
         String password = passwordTF.getText().trim();
         String email = emailTF.getText().trim();
         String latestUsername = latestusernameTF.getText().trim();
 
         // Validate input fields
         if (currentUsername.isEmpty() && password.isEmpty() && email.isEmpty() && latestUsername.isEmpty()) {
             showAlert("Error", "All fields are required!", Alert.AlertType.ERROR);
             return;
         }
         // Validate email format
         if (!isValidEmail(email)) {
        showError("Please enter a valid email address (must be @gmail.com or @yahoo.com).");
        return;
    }
 
         try (Connection conn = DatabaseHandler.getDBConnection()) {
             conn.setAutoCommit(false); // Start transaction
 
             // Verify user credentials
             if (!authenticateUser(conn, currentUsername, password, email)) {
                 showAlert("Error", "Invalid username, password, or email!", Alert.AlertType.ERROR);
                 return;
             }
 
             // Check if new username already exists
             if (usernameExists(conn, latestUsername)) {
                 showAlert("Error", "New username already taken!", Alert.AlertType.ERROR);
                 return;
             }
 
             // Update only the username
             String updateQuery = "UPDATE WazerAcc SET Username = ? WHERE Username = ?";
             try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                 stmt.setString(1, latestUsername);
                 stmt.setString(2, currentUsername);
 
                 int rowsUpdated = stmt.executeUpdate();
                 if (rowsUpdated > 0) {
                     conn.commit(); // Commit transaction
                     showAlert("Success", "Username updated successfully!", Alert.AlertType.INFORMATION);
                     loadData(); // Refresh table data
                     hometable.refresh(); // Refresh UI table
                 } else {
                     showAlert("Error", "Update failed! User not found.", Alert.AlertType.ERROR);
                     conn.rollback();
                 }
             }
         } catch (SQLException e) {
             e.printStackTrace();
             showAlert("Error", "Database error!", Alert.AlertType.ERROR);
         } finally {
             try (Connection conn = DatabaseHandler.getDBConnection()) {
                 conn.setAutoCommit(true); // Reset auto-commit
             } catch (SQLException e) {
                 e.printStackTrace();
             }
         }
     }
     // Check if username, password, and email match
     private boolean authenticateUser(Connection conn, String username, String password, String email) throws SQLException {
         String query = "SELECT 1 FROM WazerAcc WHERE Username = ? AND Password = ? AND Email = ?";
         try (PreparedStatement stmt = conn.prepareStatement(query)) {
             stmt.setString(1, username);
             stmt.setString(2, password);
             stmt.setString(3, email);
             try (ResultSet rs = stmt.executeQuery()) {
                 return rs.next(); // True if credentials match
             }
         }
     }
     
     private boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9._%+-]+@(yahoo|gmail)\\.com$";
        return Pattern.matches(regex, email);
    }

     // Check if new username already exists
     private boolean usernameExists(Connection conn, String username) throws SQLException {
         String checkQuery = "SELECT 1 FROM WazerAcc WHERE Username = ?";
         try (PreparedStatement stmt = conn.prepareStatement(checkQuery)) {
             stmt.setString(1, username);
             try (ResultSet rs = stmt.executeQuery()) {
                 return rs.next(); // True if username exists
             }
         }
     }
 
     // Show alert messages
     private void showAlert(String title, String message, Alert.AlertType alertType) {
         Alert alert = new Alert(alertType);
         alert.setTitle(title);
         alert.setHeaderText(null);
         alert.setContentText(message);
         alert.showAndWait();
     }
 
     // Helper method to display error alerts
     private void showError(String message) {
         Alert alert = new Alert(Alert.AlertType.ERROR);
         alert.setContentText(message);
         alert.show();
     }
 
    
     }
 
 