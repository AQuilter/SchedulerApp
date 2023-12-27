package sample.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import sample.DOA.DBReports;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

/**
 * ReportsController. Displays three different reports.
 */
public class ReportsController {

    public TextArea textArea;

    /**
     * Sets textArea to display the customerAppointments report.
     * @param actionEvent
     */
    public void customerAppointmentsBtnClicked (ActionEvent actionEvent) throws SQLException {
        textArea.setText(DBReports.getCustomerAppointments());
    }

    /**
     * Sets textArea to display the contactSchedule report.
     * @param actionEvent
     */
    public void contactScheduleBtnClicked(ActionEvent actionEvent) throws SQLException {
        textArea.setText(DBReports.getContactSchedule());
    }

    /**
     * Sets textArea to display the customerAppointments report.
     * @param actionEvent
     */
    public void numContactAppsBtnClicked(ActionEvent actionEvent) throws SQLException {
        textArea.setText(DBReports.getTotalAppsPerContact());
    }

    /**
     * Launches the Main Screen.
     * @param actionEvent
     */
    public void backBtnClicked(ActionEvent actionEvent) throws IOException {
        URL url = new File("src/sample/View/MainScreen.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 397, 486);
        stage.setTitle("Main Screen");
        stage.setScene(scene);
        stage.show();
    }

}
