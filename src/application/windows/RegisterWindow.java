package application.windows;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class RegisterWindow {
	private FXMLLoader loader;
	
	public RegisterWindow() throws IOException {
		Stage registerWindow = new Stage();
		registerWindow.setTitle("Register");
		loader = new FXMLLoader(getClass().getResource("/fxml/register.fxml"));
		AnchorPane root = loader.load();
		Scene scene = new Scene(root);
		registerWindow.setScene(scene);
		registerWindow.setResizable(false);
		registerWindow.show();
		RegisterController controller = loader.getController();
		controller.stage = registerWindow;
	}
}
