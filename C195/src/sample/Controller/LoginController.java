package sample.Controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.DOA.DBAppointments;
import sample.DOA.DBLogin;
import sample.DOA.DBUsers;
import sample.Model.Appointments;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * LoginController. Handles authentication.
 */
public class LoginController implements Initializable {

    public TextField usernameTextField;
    public TextField passwordTextField;
    public Button loginBtn;
    public Button cancelBtn;
    public Label errorLabel;
    public Label locationLabel;
    public Label userNameLabel;
    public Label passwordLabel;
    public Label pleaseLogInLabel;
    public Label helloLabel;
    public Label yourLocationLabel;


    /**
     * Displays language based on user's default Locale.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {

            Locale locale = Locale.getDefault(); // might not need this
            Locale.setDefault(locale); // might not need this
            ZoneId zoneId = ZoneId.systemDefault();

            locationLabel.setText(String.valueOf(zoneId));

            resourceBundle = ResourceBundle.getBundle("login", Locale.getDefault());

            helloLabel.setText(resourceBundle.getString("Hello"));
            yourLocationLabel.setText(resourceBundle.getString("YourLocation"));
            loginBtn.setText(resourceBundle.getString("Login"));
            cancelBtn.setText(resourceBundle.getString("Cancel"));
            userNameLabel.setText(resourceBundle.getString("Username"));
            passwordLabel.setText(resourceBundle.getString("Password"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Authenticate a user's login, checks if that user has upcoming appointments, and tracks
     * successful/unsuccessful login attempts to file login_activity.txt.
     * @param actionEvent
     */
    public void loginBtnClicked(ActionEvent actionEvent) throws IOException {

        try {
            // incorrect username/pass label
            ResourceBundle resourceBundle = ResourceBundle.getBundle("login", Locale.getDefault());
            errorLabel.setText(resourceBundle.getString("Error"));

            // activity log
            String filename = "login_activity.txt", log;
            FileWriter fileWriter = new FileWriter(filename, true);
            PrintWriter outputFile = new PrintWriter(fileWriter);

            // appointment check definitions
            boolean appointmentCheck = false;
            LocalDateTime plus15Mins = LocalDateTime.now().plusMinutes(15);
            LocalDateTime minus15Mins = LocalDateTime.now().minusMinutes(15);
            LocalDateTime time = null;
            LocalDateTime startTime;
            int appointmentId = 0;
            String errorMessage;
            // ObservableList<Appointments> allAppointmentsList = DBAppointments.getAllAppointments();


            // validating username & pass
            String userName = usernameTextField.getText();
            String password = passwordTextField.getText();
            boolean validate = DBLogin.validateLogin(userName, password);

            if (validate) {

                log = "User '" + userName + "' attempted to log in at " + Timestamp.valueOf(LocalDateTime.now()) +
                        " and was SUCCESSFUL.";
                outputFile.println(log);
                outputFile.close();

                URL url = new File("src/sample/View/MainScreen.fxml").toURI().toURL();
                Parent root = FXMLLoader.load(url);
                Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 375, 377);
                stage.setTitle("Main Menu");
                stage.setScene(scene);
                stage.show();


                // checking for appointments +/- 15 mins.
                int userIdByName = DBUsers.getUserIdByName(userName);
                ObservableList<Appointments> userAppointments = DBAppointments.getAppointmentsByUserID(userIdByName);

                System.out.println(userAppointments.size());

                for (Appointments app : userAppointments){
                    startTime = app.getStart();
                    if ((startTime.isAfter(minus15Mins) || startTime.isEqual(minus15Mins)) && (startTime.isBefore(plus15Mins)
                            || (startTime.isEqual(plus15Mins)))) {
                        appointmentCheck = true;
                        appointmentId = app.getAppointmentId();
                        time = startTime;
                    }
                }
                    if (appointmentCheck){
                        errorMessage = "Appointment #" + appointmentId + " starts at " + time + ".";
                        ButtonType buttonType = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                            Alert invalidInput = new Alert(Alert.AlertType.WARNING, errorMessage, buttonType);
                        invalidInput.setHeaderText("You have an appointment within 15 minutes!");
                        invalidInput.showAndWait();
                    } else {
                        errorMessage = "You have no upcoming appointments.";
                        ButtonType buttonType = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                        Alert invalidInput = new Alert(Alert.AlertType.WARNING, errorMessage, buttonType);
                        invalidInput.showAndWait();
                    }
            } else {
                resourceBundle.getString("Error");
                log = "User '" + userName + "' attempted to log in at " + Timestamp.valueOf(LocalDateTime.now()) +
                        " and FAILED.";
                outputFile.println(log);
                outputFile.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Closes the program when the user clicks cancel.
     */
    public void cancelBtnClicked(){
        System.exit(0);
    }


}
