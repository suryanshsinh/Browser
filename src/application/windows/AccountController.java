package application.windows;

import java.net.URL;
import java.util.ResourceBundle;
import application.database.Database;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AccountController implements Initializable {
	@FXML
	private Label nameLabel;
	@FXML
	private Label emailLabel;
	
	public Stage stage;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		nameLabel.setText("Name: " + Database.name);
		emailLabel.setText("Email: " + Database.email);
	}
	
	public void deleteAccount() throws Exception {
		if (Database.newConfirmationAlert("Are you sure?", "Are you sure you want to delete this account? This change is irreversible.")) {
			Database.deleteAccount();
			Database.newInformationAlert("Account deleted", "You account has successfully been deleted from the database.");
			stage.close();
		}
	}
}
