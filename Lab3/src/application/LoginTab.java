package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.*;

import java.sql.SQLException;

// controller for both the top tabs and login tab!

public class LoginTab {
    @FXML private Text actiontarget;
    @FXML private TextField username;
    
    private BookingTab bookingTabCtrl;
    private Database db;
       
    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
    	
        if(!db.isConnected()) {
	        // inform the user that there is no check against the database
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Login fail");
	        alert.setHeaderText(null);
	        alert.setContentText("No database connection! Cannot check user credentials.");
	        alert.showAndWait();
        	
        } else {

            String uname = username.getText();

            try {
                int userId = db.login(uname);
                if(userId != 0) {
                    // setting the user name
                    CurrentUser.instance().loginAs(uname, userId);

                    // inform the user about logging in
                    actiontarget.setText("Sign in user "+uname);

                    // inform booking tab of user change
                    bookingTabCtrl.userChanged();
                } else {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Login fail");
                    alert.setHeaderText(null);
                    alert.setContentText("No user with that username!");
                    alert.showAndWait();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    public void initialize() {
    	System.out.println("Initializing LoginTab.");    	
    }
        
    // helpers
    // use this pattern to send data down to controllers at initialization
    public void setBookingTab(BookingTab bookingTabCtrl) {
    	System.out.println("LoginTab sets bookingTab:"+bookingTabCtrl);
    	this.bookingTabCtrl = bookingTabCtrl;
    	
    }
    
    public void setDatabase(Database db) {
    	this.db = db;
    }
    
}