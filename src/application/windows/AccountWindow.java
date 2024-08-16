package application.windows;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AccountWindow {
	private FXMLLoader loader;
	
	public AccountWindow() throws IOException {
		Stage accountWindow = new Stage();
		accountWindow.setTitle("Account details");
		loader = new FXMLLoader(getClass().getResource("/fxml/account.fxml"));
		AnchorPane root = loader.load();
		Scene scene = new Scene(root);
		accountWindow.setScene(scene);
		accountWindow.setResizable(false);
		accountWindow.show();
		AccountController controller = loader.getController();
		controller.stage = accountWindow;
	}
}
