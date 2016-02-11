package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.util.Map;


public class Main extends Application {

	private Database db = new Database();

	@Override
	public void start(Stage primaryStage) {
		try {

			// BorderPane root = new BorderPane();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("TopTab.fxml"));
			Parent root = loader.load();

			Scene scene = new Scene(root,600,440);
			scene.getStylesheets().add(getClass().getResource("login.css").toExternalForm());
			//			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			// obtain main controller
			TopTabView wc = (TopTabView) loader.getController();
			wc.setDatabase(db);

			// opening database connection
			Map<String, String> env = System.getenv();
			System.out.println(env.get("EDA216_DB_USER"));
			if(!db.openConnection(env.get("EDA216_DB_USER"), env.get("EDA216_DB_PASS"))) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Database error");
				alert.setHeaderText(null);
				alert.setContentText("Could not connect to the database! Check console for details.");
				alert.showAndWait();
			}
			
			// show the main window
			primaryStage.setTitle("Movie Booking System");
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void stop() {
		// close the database here
		db.closeConnection();

		try {
			super.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
