package application.windows;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginWindow {
	private FXMLLoader loader;
	
	public LoginWindow() throws IOException {
		Stage loginWindow = new Stage();
		loginWindow.setTitle("Login");
		loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
		AnchorPane root = loader.load();
		Scene scene = new Scene(root);
		loginWindow.setScene(scene);
		loginWindow.setResizable(false);
		loginWindow.show();
		LoginController controller = loader.getController();
		controller.stage = loginWindow;
	}
}
