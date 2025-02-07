import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable { //ung nasa recording ni sir regarding initializable
    // Observable list to store user data
    ObservableList<User> mylist = FXCollections.observableArrayList();

    @FXML
    private Label nameLabel;

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
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        accountcreatedcol.setCellValueFactory(new PropertyValueFactory<>("accountcreated"));
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
                String accountcreated = result.getString("AccountCreated");
                String email = result.getString("Email");

                // Add the user data to the ObservableList
                mylist.add(new User(username, password, accountcreated, email));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        hometable.setItems(mylist); // Set the table's items to the ObservableList
    }

    private String username;

    // Method to display the username in the label
    public void displayName(String username) {
        nameLabel.setText("Welcome to Waze, " + username);
    }

    // Method to set the username and update the label
    public void setUsername(String username) {
        this.username = username;
        displayName(username);  // Call displayName to update the label
        System.out.println("Username set: " + this.username);
    }
}

//   @FXML
// private void deletebuttonHandler(ActionEvent event) {
//     // confirmation dialogue
//     Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//     alert.setTitle("Delete Account");
//     alert.setHeaderText("Are you sure you want to permanently delete your account?");
//     alert.setContentText("This action cannot be undone.");
//     // Wait for user confirmation
//     ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

//     if (result == ButtonType.OK) {
//         if (DatabaseHandler.deleteAccount(username)) {
//             Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
//             successAlert.setTitle("Account Deleted");
//             successAlert.setHeaderText("Your account has been successfully deleted.");
//             successAlert.showAndWait();
//             // Close current window (Home Scene)
//             Stage stage = (Stage) deletebutton.getScene().getWindow();
//             stage.close();
//             // Close the database connection
//             DatabaseHandler.closeConnection();
//         } else {
//             //deletion fails
//             Alert errorAlert = new Alert(Alert.AlertType.ERROR);
//             errorAlert.setTitle("Error");
//             errorAlert.setHeaderText("Account deletion failed.");
//             errorAlert.setContentText("Please try again later.");
//             errorAlert.showAndWait();
//         }
//     }
// }
// }
//          @FXML
//     private void loginbuttonhandler(ActionEvent event) throws IOException {

//         String password = passwordtextfield.getText();

//         if (DatabaseHandler.validateLogin(username, password)) {
            
//             // // Load Scene2.fxml when login button is clicked
//             // FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
//             // // Load Scene2Controller
//             //  root = loader.load();
//             // HomeController homecontroller = loader.getController();
//             // // Pass username from textfield to displayName() method
//             // homecontroller.displayName(username);

//             // // Load stage and scene
//             //  stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//             //  scene = new Scene(root);
//             // stage.setScene(scene);
//             // stage.show();
//         }
//         else{
//             System.out.println("Unsuccessful");
//          }
//     }
// }
// }
    

    