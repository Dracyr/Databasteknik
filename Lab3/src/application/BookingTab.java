package application;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;


public class BookingTab {
	// top context message
	@FXML private Text topContext;
	// bottom message
	@FXML private Text bookMsg;
	
	// table references
	@FXML private ListView<String> moviesList;
	@FXML private ListView<String> datesList;
	
	// show info references
	@FXML private Label showTitle;
	@FXML private Label showDate;
	@FXML private Label showVenue;
	@FXML private Label showFreeSeats;
	
	// booking button
	@FXML private Button bookTicket;
	
	private Database db;
	private Show crtShow = new Show();
	
	public void initialize() {
		System.out.println("Initializing BookingTab");
				

		// clear fields
		// NOTE! at this point db==null, so do not fill the movie name list
		fillShow(null,null);
		
		// set up listeners for the movie list selection
		moviesList.getSelectionModel().selectedItemProperty().addListener(
				(obs, oldV, newV) -> {
					// need to update the date list according to the selected movie
					// update also the details on the right panel
					String movie = newV;
					fillDatesList(newV);
					fillShow(movie,null);
				});
		
		// set up listeners for the date list selection
		datesList.getSelectionModel().selectedItemProperty().addListener(
				(obs, oldV, newV) -> {
					// need to update the details according to the selected date
					String movie = moviesList.getSelectionModel().getSelectedItem();
					String date = newV;
				    fillShow(movie, date);
				});

		// set up booking button listener
		// one can either use this method (setup a handler in initialize)
		// or directly give a handler name in the fxml, as in the LoginTab class
		bookTicket.setOnAction(
				(event) -> {
					String movie = moviesList.getSelectionModel().getSelectedItem();
					String date = datesList.getSelectionModel().getSelectedItem();
					int bookingNumber = 0;
					try {
						bookingNumber = db.book(movie, date, CurrentUser.instance().getCurrentUserId());
					} catch (SQLException e) {
						e.printStackTrace();
					}
					/* --- TODO: should attempt to book a ticket via the database --- */
					/* --- do not forget to report booking number! --- */
					/* --- update the displayed details (free seats) --- */
					if (bookingNumber > 0) {
						report("Booked one ticket to "+movie+" on "+date + " with number " + bookingNumber);
						fillShow(movie, date);
					} else {
						report("Could not book ticket to "+movie+" on "+date);
					}
				});
		
		report("Ready.");
	}
	
	// helpers	
	// updates user display
	private void fillStatus(String usr) {
		if(usr.isEmpty()) topContext.setText("You must log in as a known user!");
		else topContext.setText("Currently logged in as " + usr);
	}
	
	private void report(String msg) {
		bookMsg.setText(msg);
	}
	
	public void setDatabase(Database db) {
		this.db = db;
	}
	
	private void fillNamesList() {
		List<String> allmovies = new ArrayList<>();
		try {
			allmovies.addAll(db.getMovieData());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		moviesList.setItems(FXCollections.observableList(allmovies));
		// remove any selection
		moviesList.getSelectionModel().clearSelection();
	}

	private void fillDatesList(String movie) {
		List<String> alldates = new ArrayList<String>();
		if(movie!=null) {
			try {
				alldates.addAll(db.getDatesForMovie(movie));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		datesList.setItems(FXCollections.observableList(alldates));
		// remove any selection
		datesList.getSelectionModel().clearSelection();
	}
	
	private void fillShow(String movie, String date) {
		if(movie==null) // no movie selected
			crtShow = new Show();
		else if(date==null) // no date selected yet
			crtShow = new Show(movie);
		else // query the database via db
			try {
				crtShow = db.getPerformanceData(movie, date);
			} catch (SQLException e) {
				e.printStackTrace();
			}

		showTitle.setText(crtShow.title);
		showDate.setText(crtShow.date);
		showVenue.setText(crtShow.venue);
		if(crtShow.freeSeats >= 0) showFreeSeats.setText(crtShow.freeSeats.toString());
		else showFreeSeats.setText("-");
	}
		
	public void userChanged() {
		fillStatus(CurrentUser.instance().getCurrentUserName());
		fillNamesList();
		fillDatesList(null);
		fillShow(null,null);
	}
	
}
