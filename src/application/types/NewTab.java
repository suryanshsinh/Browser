package application.types;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

public class NewTab extends Tab {
	NewTab next;
	NewTab previous;
	boolean selected;
	static int data = 0;
	TabsLL listObj;
	NewTabController controller;
	
	NewTab(TabsLL listObj) throws IOException {
		super("New Tab");
		this.previous = null;
		this.next = null;
		this.selected = false;
		this.listObj = listObj;
		this.getStyleClass().add("tab");
		
		this.setOnSelectionChanged(event -> {
			this.listObj.deSelectAll();
			this.selected = true;
		});
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/newtab.fxml"));
		AnchorPane pageView = loader.load();
		controller = loader.getController();
		controller.setTab(this);
        this.setContent(pageView);
	}
}
