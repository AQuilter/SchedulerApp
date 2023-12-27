package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Controller.AddAppointmentsController;
import sample.DOA.*;

import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;
/**
 * Contains the main method for this scheduler application.
 */
public class Main extends Application {

    /**
     * Launches the Login screen.
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("View/Login.fxml")); // "View/Login.fxml"
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 397, 486));
        primaryStage.show();
    }

    /**
     * Makes connection to the database and launches the program. Sets the locale if you manually want to
     * test a specific language.
     * @param args
     */
    public static void main(String[] args) {
       // Locale.setDefault(new Locale("fr")); // -- uncomment to test for French.
        JDBC.makeConnection();

        launch(args);

        JDBC.closeConnection();
        System.exit(0);
    }
}





// DBCountries.checkDateConversion();
//        ResourceBundle rb = ResourceBundle.getBundle("login", Locale.getDefault());
//        if(Locale.getDefault().getLanguage().equals("fr")) {
//            System.out.println(rb.getString("PleaseLogin"));
//        }