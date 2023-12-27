package sample.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * MainScreenController. Displays the main screen.
 */
public class MainScreenController {

    /**
     * Launches the Appointments screen.
     * @param actionEvent
     */
    public void appointmentsBtnClicked(ActionEvent actionEvent) throws IOException {
        URL url = new File("src/sample/View/Appointments.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1046, 564);
        stage.setTitle("Appointments");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Launches the Customers screen.
     * @param actionEvent
     */
    public void customersBtnClicked(ActionEvent actionEvent) throws IOException {
        URL url = new File("src/sample/View/Customer.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 700, 400);
        stage.setTitle("Customers");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Launches the Reports screen.
     * @param actionEvent
     */
    public void reportsBtnClicked(ActionEvent actionEvent) throws IOException {
        URL url = new File("src/sample/View/Reports.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 728, 495);
        stage.setTitle("Reports");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Exits the program if the cancel button is clicked.
     */
    public void exitBtnClicked() {
        System.exit(0);
    }
}
