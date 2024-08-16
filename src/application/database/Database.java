package application.database;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.sql.*;
import java.util.Enumeration;
import java.util.Optional;
import java.util.Properties;

import application.browser.Main;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class Database {
	public static Connection con;
	public static String deviceId = getDeviceId();
	public static int userId;
	public static String name;
	public static String email;
	private static PreparedStatement pst;
	
	public Database () throws SQLException {
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/browser", "root", "");
		if (con == null) {
			System.out.println("Failed to connect to the database!");
		} else {
			Database.updateAuth();
		}
	}
	
	public static void login(String email, String password) throws Exception {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
    	byte[] hash = md.digest(password.getBytes());
    	String hashedPassword = "";
    	for (byte b : hash) {
    	    hashedPassword += String.format("%02x", b);
    	}
    	String fetchUser = "SELECT id, name, email FROM users WHERE email = ? and password = ?";
    	pst = con.prepareStatement(fetchUser);
    	pst.setString(1, email);
    	pst.setString(2, hashedPassword);
    	ResultSet rs = pst.executeQuery();
    	int count = 0;
    	while (rs.next()) {
    		userId = rs.getInt(1);
    		name = rs.getString(2);
    		email = rs.getString(3);
    		count++;
    	}
    	if (count == 0) {
    		Database.newErrorAlert("Invalid Credentials", "The credentials you provided could not be located in the database.");
    	} else {
			Database.newInformationAlert("Welcome " + name, "You have logged in successfully");
			String updateDeviceId = "UPDATE users SET device_id = ? WHERE id = ?";
			pst = con.prepareStatement(updateDeviceId);
			pst.setString(1, deviceId);
			pst.setInt(2, userId);
			pst.execute();
    		Main.updateAuth();
    	}
	}
	
	public static void register(String name, String email, String password) throws Exception {
		System.out.println(Database.name + Database.email + Database.userId);
		MessageDigest md = MessageDigest.getInstance("SHA-256");
    	byte[] hash = md.digest(password.getBytes());
    	String hashedPassword = "";
    	for (byte b : hash) {
    	    hashedPassword += String.format("%02x", b);
    	}
		
		String registerUser = "INSERT INTO users (name, email, password, device_id) VALUES (?, ?, ?, ?)";
		pst = con.prepareStatement(registerUser);
		pst.setString(1, name);
		pst.setString(2, email);
		pst.setString(3, hashedPassword);
		pst.setString(4, deviceId);
		try {
			pst.executeUpdate();
			Database.newInformationAlert("Account created successfully", "");
		} catch (Exception e) {
			if (e.toString().indexOf("Duplicate") >= 0) {
				Database.newErrorAlert("Email already exists", "A different account with the same email address already exists. Please login.");
			}
		}
		Main.updateAuth();
		System.out.println(Database.name + Database.email + Database.userId);
	}
	
	public static void logout() throws Exception {
		String logoutUser = "UPDATE users SET device_id = null where id = ?";
		pst = con.prepareStatement(logoutUser);
		pst.setInt(1, userId);
		pst.execute();
		Main.updateAuth();
	}
	
	public static void updateAuth() throws SQLException {
		String fetchUser = "SELECT id, name, email FROM users WHERE device_id = ?";
		pst = con.prepareStatement(fetchUser);
		pst.setString(1, deviceId);
		ResultSet rs = pst.executeQuery();
		while (rs.next()) {
			userId = rs.getInt(1);
			name = rs.getString(2);
			email = rs.getString(3);
		}
		userId = 0;
		name = "";
		email = "";
	}
	
	public static void addBookmark(String title, String url) throws SQLException {
		String insertBookmark = "INSERT INTO bookmarks (title, url, user_id) values (?, ?, ?)";
		pst = con.prepareStatement(insertBookmark);
		pst.setString(1, title);
		pst.setString(2, url);
		pst.setInt(3, userId);
		pst.execute();
		Database.newInformationAlert("Bookmark added!", "");
	}

	public static void deleteAccount() throws Exception {
		String deleteAccount = "DELETE FROM users WHERE id = ?";
		pst = con.prepareStatement(deleteAccount);
		pst.setInt(1, userId);
		pst.execute();
		Database.logout();
	}
	
	public static void savePassword(String title, String url, String username, String password) {
		
	}
	
	public static void newErrorAlert(String title, String description) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(description);
        alert.showAndWait();
	}
	
	public static void newInformationAlert(String title, String description) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(title);
        alert.setContentText(description);
        alert.showAndWait();
	}
	
	public static boolean newConfirmationAlert(String title, String description) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(title);
        alert.setContentText(description);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
	}
	
	public static boolean checkEmail(String email) {
		char c;
		for (int i = 0; i < email.length(); i++) {
			c = email.charAt(i);
			if (c != '.' && c != '_' && c != '-' && c != '@' && c < 'A' && c > 'Z' && c < 'a' && c > 'z' && c < '0' && c > '9') {
				return false;
			}
		}
		if (email.indexOf('@') == -1 || email.indexOf('@') >= email.length() - 3 || email.indexOf('@') < 1) {
			return false;
		} else if (email.lastIndexOf('.') - email.indexOf('@') < 2 || email.length() - email.lastIndexOf('.') < 3) {
			return false;
		} else {
			return true;
		}
	}

    public static String getDeviceId() {
        try {
			String deviceId = "";
			Properties properties = System.getProperties();
			deviceId += properties.getProperty("os.name");
			deviceId += properties.getProperty("os.arch");
			deviceId += properties.getProperty("os.version");
			InetAddress ip = InetAddress.getLocalHost();
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			
			if (network == null) {
				Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
				while (networks.hasMoreElements()) {
					network = networks.nextElement();
					if (network.getHardwareAddress() != null) {
						break;
					}
				}
			}
			
			byte[] macAddress = network.getHardwareAddress();
			if (macAddress != null) {
				for (byte b : macAddress) {
					deviceId += String.format("%02X", b);
				}
			}
			
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hash = md.digest(deviceId.getBytes());
			
			String hexString = "";
			for (byte b : hash) {
				hexString += String.format("%02x", b);
			}
			
			return hexString;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get device ID", e);
        }
    }
}
