import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class LoginController {
    @FXML
    private Label usernamelabel;
    @FXML
    private Label passwordlabel;
    @FXML
    private TextField usernametextfield;
    @FXML
    private TextField passwordtextfield;
    @FXML
    private Button loginbutton;
    @FXML
    private ImageView wazelogo;
    @FXML
    private ImageView wazetext;
    @FXML
    private Label faketext;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private void loginbuttonhandler(ActionEvent event) throws IOException {
        String username = usernametextfield.getText();
        String password = passwordtextfield.getText();
    
        System.out.println("username: " + username);
        System.err.println("password: " + password);
    
        if (DatabaseHandler.validateLogin(username, password)) {
            // Load homepage when 'login' is clicked
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));  // Adjusted path
            root = loader.load();
            
            HomeController homeController = loader.getController();
            homeController.displayName(username);
            homeController.setUsername(username);
    
            // Load stage and scene
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            showLoginError();
            System.out.println("Invalid username or password");
         
        }
    }
// Method to display an error pop-up
private void showLoginError() {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Login Error");
    alert.setHeaderText("Invalid credentials");
    alert.setContentText("The username or password you entered is incorrect. Please try again.");

    // Show the alert and wait for user to close it
    alert.showAndWait();
}
}
//     @FXML
//     private void createbuttonhandler(ActionEvent event) throws IOException {
//         // Load the CreateAccountUI.fxml file for the new scene
//         FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateAccountUI.fxml"));

//         // Check if CreateAccountUI.fxml was loaded successfully
//         Parent root = loader.load();

//         // Get the current window (Stage)
//         Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

//         // Create a new scene and set it to the current stage
//         Scene scene = new Scene(root, 650, 410); // You can adjust the width and height if needed
//         stage.setScene(scene);

//         // Show the new scene
//         stage.show();
//     }
