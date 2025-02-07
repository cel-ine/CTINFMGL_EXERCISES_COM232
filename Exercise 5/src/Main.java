import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        
        // make this my main page - unang-una na makikita once na nirun 
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        
        // sets title of main page
        primaryStage.setTitle("China's Waze");

        //window size 
        primaryStage.setScene(new Scene(root, 600, 400));

        //displays the UI 
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}