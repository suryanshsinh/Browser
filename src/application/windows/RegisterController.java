package application.windows;

import java.net.URL;
import java.util.ResourceBundle;
import application.database.Database;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController implements Initializable {
	@FXML
	private TextField nameText;
	@FXML
	private TextField emailText;
	@FXML
	private TextField passwordText;
	
	public Stage stage;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}
	
	public void register() throws Exception {
		String name = nameText.getText();
		String email = emailText.getText();
		String password = passwordText.getText();
		if (Database.userId != 0) {
			Database.newInformationAlert("An account is already logged in.", "Please log out to register for a different account.");
			stage.close();
		} else {
			if (!name.equals("")) {
				if (Database.checkEmail(email)) {
					if (!password.equals("")) {
						Database.register(name, email, password);
						stage.close();
					} else {
						Database.newErrorAlert("Invalid password", "The password cannot be empty");
					}
				} else {
					Database.newErrorAlert("Invalid email", "The email address you provided is invalid");
				}
			} else {
				Database.newErrorAlert("Invalid name", "The name cannot be empty");
			}
		}
	}
}
