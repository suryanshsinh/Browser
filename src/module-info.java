module Browser {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;
	requires javafx.web;
	requires java.sql;
	requires java.base;
	
	opens application.browser to javafx.graphics, javafx.fxml;
	opens application.types;
	opens application.database;
	opens application.windows;
}
