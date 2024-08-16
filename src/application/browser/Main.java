package application.browser;

import java.io.IOException;
import application.database.Database;
import application.types.TabsLL;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class Main extends Application {
	static TabsLL tabsList;
	
	@Override
	public void start(Stage stage) throws Exception{
        new Database();
		AnchorPane root = new AnchorPane();
		TabPane tabs = FXMLLoader.load(getClass().getResource("/fxml/tabs.fxml"));
		tabsList = new TabsLL();
		tabsList.listObj = tabsList;
		Tab add = new Tab("+");
		add.setClosable(false);
		add.getStyleClass().add("add");
		
		add.setOnSelectionChanged(event -> {
			try {
				newTab(tabs, tabsList);
			} catch (IOException e) {
				System.out.println("Please reinstall the browser!");
			}
        });
		
		tabs.getTabs().add(tabsList.addNewTab());
		tabs.getTabs().add(add);
		
        AnchorPane.setTopAnchor(tabs, 0.0);
        AnchorPane.setBottomAnchor(tabs, 0.0);
        AnchorPane.setLeftAnchor(tabs, 0.0);
        AnchorPane.setRightAnchor(tabs, 0.0);
		root.getChildren().add(tabs);
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/styles/tabpane.css").toExternalForm());
		scene.getStylesheets().add(getClass().getResource("/styles/ribbon.css").toExternalForm());
		stage.setScene(scene);
        stage.setTitle("resworB");
		stage.setMaximized(true);
		stage.show();
		
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode() == KeyCode.TAB && event.isControlDown() && event.isShiftDown()) {
				tabs.getSelectionModel().select(tabsList.selectPrevious());
				event.consume();
            } else if (event.getCode() == KeyCode.TAB && event.isControlDown()) {
				tabs.getSelectionModel().select(tabsList.selectNext());
				event.consume();
            } else if (event.getCode() == KeyCode.T && event.isControlDown()) {
				try {
					newTab(tabs, tabsList);
				} catch (IOException e) {
					System.out.println("Please reinstall the browser!");
				}
				event.consume();
            } else if (event.getCode() == KeyCode.W && event.isControlDown()) {
				tabs.getTabs().remove(tabsList.closeSelected());
            } 
        });
//		MyThread mt = new MyThread(tabsList);
//		mt.start();
	}
	
	static void newTab(TabPane tabs, TabsLL tabsList) throws IOException {
        if (tabs.getTabs().size() > 1) {
			tabs.getTabs().add(tabs.getTabs().size() - 1, tabsList.addNewTab());
            tabs.getSelectionModel().select(tabs.getTabs().size() - 2);
        } else {
			tabsList.first = null;
            Platform.exit();
        }
	}

	public static void updateAuth() throws Exception {
		Database.updateAuth();
		tabsList.updateTabs();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

class MyThread extends Thread {
	TabsLL t;
	static int line;
	
	MyThread(TabsLL t) {
		super();
		this.t = t;
	}
	
	public void run() {
		while (true) {
			System.out.print(line++ + " -> ");
			t.display();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}
}