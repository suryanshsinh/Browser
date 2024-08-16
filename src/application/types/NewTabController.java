package application.types;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import application.database.Database;
import application.windows.AccountWindow;
import application.windows.LoginWindow;
import application.windows.RegisterWindow;
import application.windows.SavePasswordWindow;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;

public class NewTabController implements Initializable {
	
	@FXML
	private WebView webView;
	@FXML
	private TextField urlBar;
	@FXML
	private Button refreshButton;
	@FXML
	private ImageView refreshButtonIcon;
	@FXML
	private ImageView backwardsButtonIcon;
	@FXML
	private ImageView forwardsButtonIcon;
	@FXML 
	private ImageView homeButtonIcon;
	@FXML
	private ImageView bookmarkButtonIcon;
	@FXML
	private ImageView savePasswordButtonIcon;
	@FXML
	private ImageView menuButtonIcon;
	@FXML
	private MenuButton menuButton;
	
	private WebEngine engine;
	private NewTab currentTab;
	private WebHistory history;
	
	private int userId = Database.userId;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Image refreshIcon = new Image(getClass().getResourceAsStream("/assets/refresh.png"));
		Image refreshCancelIcon = new Image(getClass().getResourceAsStream("/assets/cross.png"));
		refreshButtonIcon.setImage(refreshIcon);
		Image backwardsIcon = new Image(getClass().getResourceAsStream("/assets/back.png"));
		backwardsButtonIcon.setImage(backwardsIcon);
		Image forwardsIcon = new Image(getClass().getResourceAsStream("/assets/forward.png"));
		forwardsButtonIcon.setImage(forwardsIcon);
		Image homeIcon = new Image(getClass().getResourceAsStream("/assets/home.png"));
		homeButtonIcon.setImage(homeIcon);
		Image bookmarkIcon = new Image(getClass().getResourceAsStream("/assets/bookmark.png"));
		bookmarkButtonIcon.setImage(bookmarkIcon);
		Image menuIcon = new Image(getClass().getResourceAsStream("/assets/bookmark.png"));
		menuButtonIcon.setImage(menuIcon);
		
		updateMenuButtons();
		
		engine = webView.getEngine();
        engine.locationProperty().addListener((observable, oldValue, newValue) -> {
            urlBar.setText(newValue);
        });
		
        engine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
        	if (currentTab != null) {
	            if (newValue == Worker.State.SUCCEEDED) {
	                String title = engine.getTitle();
	                currentTab.setText(title);
	            } else if (newValue == Worker.State.RUNNING) {
	            	currentTab.setText("Loading...");
	            } else if (newValue == Worker.State.FAILED) {
	            	currentTab.setText("Failed to load");
	            } else if (newValue == Worker.State.CANCELLED) {
	            	currentTab.setText("Cancelled");
	            } 
	            
	            if (newValue == Worker.State.RUNNING) {
            		refreshButtonIcon.setImage(refreshCancelIcon);
            		refreshButton.setOnAction(e -> {
            			engine.getLoadWorker().cancel();
            		});
	            } else {
            		refreshButtonIcon.setImage(refreshIcon);
            		refreshButton.setOnAction(e -> {
            			loadPage();
            		});
	            }
        	}
        });

        
        String homePage = "https://www.google.com/";
        engine.load(homePage);
	}
	
	public void updateMenuButtons() {
		userId = Database.userId;
		menuButton.getItems().clear();
		MenuItem loginButton = new MenuItem("Login");
		MenuItem registerButton = new MenuItem("Register");
		MenuItem viewAccount = new MenuItem("Account details");
		MenuItem savedPassword = new MenuItem("Saved Password");
		MenuItem viewBookmarks = new MenuItem("Bookmarks");
		MenuItem logoutButton = new MenuItem("Logout");

		loginButton.setOnAction(e -> {
			try {
				new LoginWindow();
			} catch (IOException e1) {
			}
		});
		registerButton.setOnAction(e -> {
			try {
				new RegisterWindow();
			} catch (IOException e1) {
			}
		});
		viewAccount.setOnAction(e -> {
			try {
				new AccountWindow();
			} catch (IOException e1) {
			}
		});
		savedPassword.setOnAction(e -> {
			
		});
		logoutButton.setOnAction(e -> {
			try {
				Database.logout();
				Database.newInformationAlert("Logout Successfull", "You have been logged out successfully.");
			} catch (Exception e1) {
			}
		});
		
		if (userId == 0) {
			menuButton.getItems().add(loginButton);
			menuButton.getItems().add(registerButton);
		} else {
			menuButton.getItems().add(viewAccount);
			menuButton.getItems().add(viewBookmarks);
			menuButton.getItems().add(logoutButton);
		}
	}

	public void loadPage() {
		String address = urlBar.getText();
		if (!address.startsWith("https://") && !address.startsWith("http://")) {
			if (address.contains(" ") || !address.contains(".")) {
				address = "https://www.google.com/search?q=" + address.replace(' ', '+');
			} else {
				address = "https://" + address;
			}
		}
		if (!address.equals(engine.getLocation())) {
			engine.load(address);
		} else {
			engine.reload();
		}
	}
	
	public void displayHistory() {
		history = engine.getHistory();
		ObservableList<WebHistory.Entry> entries = history.getEntries();
		for (WebHistory.Entry e : entries) {
			System.out.println(e.getTitle());
		}
	}

    public void undo() {
		history = engine.getHistory();
		try {
			history.go(-1);
		} catch (IndexOutOfBoundsException e) {
		}
    }

    public void redo() {
		history = engine.getHistory();
		try {
			history.go(1);
		} catch (IndexOutOfBoundsException e) {
		}
    }
    
	public void setTab(NewTab currentTab) {
		this.currentTab = currentTab;
	}
	
	public void bookmarkPage() throws SQLException {
		if (Database.userId == 0) {
			Database.newErrorAlert("User not logged in", "Please log in to save bookmarks");
		} else {
			Database.addBookmark(engine.getTitle(), urlBar.getText());
		}
	}
	
	public void savePassword() throws IOException {
		if (Database.userId == 0) {
			Database.newErrorAlert("User not logged in", "Please log in to save passwords");
		} else {
			new SavePasswordWindow(engine.getTitle(), urlBar.getText());
		}
	}
}
