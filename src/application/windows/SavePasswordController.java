package application.windows;

import java.net.URL;
import java.util.ResourceBundle;
import application.database.Database;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SavePasswordController implements Initializable {
	@FXML
	private TextField titleText;
	@FXML
	private TextField urlText;
	@FXML
	private TextField usernameText;
	@FXML
	private TextField passwordText;
	
	public Stage stage;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}
	
	public void register() throws Exception {
		String title = titleText.getText();
		String url = urlText.getText();
		String username = usernameText.getText();
		String password = passwordText.getText();
		if (Database.userId != 0) {
			Database.newInformationAlert("An account is already logged in.", "Please log out to register for a different account.");
			stage.close();
		} else {
			if (!url.equals("")) {
				if (!username.equals("")) {
					if (!password.equals("")) {
						Database.savePassword(title, url, username, password);
						stage.close();
					} else {
						Database.newErrorAlert("Invalid password", "The password cannot be empty");
					}
				} else {
					Database.newErrorAlert("Invalid username", "The username cannot be empty");
				}
			} else {
				Database.newErrorAlert("Invalid url", "The url cannot be empty");
			}
		}
	}
}
