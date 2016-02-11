package application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Database is a class that specifies the interface to the 
 * movie database. Uses JDBC and the MySQL Connector/J driver.
 */
public class Database {
    /** 
     * The database connection.
     */
    private Connection conn;
        
    /**
     * Create the database interface object. Connection to the database
     * is performed later.
     */
    public Database() {
        conn = null;
    }
        
    /** 
     * Open a connection to the database, using the specified user name
     * and password.
     *
     * @param userName The user name.
     * @param password The user's password.
     * @return true if the connection succeeded, false if the supplied
     * user name and password were not recognized. Returns false also
     * if the JDBC driver isn't found.
     */
    public boolean openConnection(String userName, String password) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection 
                ("jdbc:mysql://puccini.cs.lth.se/" + userName,
                 userName, password);
        }
        catch (SQLException e) {
            System.err.println(e);
            e.printStackTrace();
            return false;
        }
        catch (ClassNotFoundException e) {
            System.err.println(e);
            e.printStackTrace();
            return false;
        }
        return true;
    }
        
    /**
     * Close the connection to the database.
     */
    public void closeConnection() {
        try {
            if (conn != null)
                conn.close();
        }
        catch (SQLException e) {
        	e.printStackTrace();
        }
        conn = null;
        
        System.err.println("Database connection closed.");
    }
        
    /**
     * Check if the connection to the database has been established
     *
     * @return true if the connection has been established
     */
    public boolean isConnected() {
        return conn != null;
    }

    public List<String> getMovieData() throws SQLException {
        Statement stmt = null;
        String query = "SELECT name FROM movies";
        ArrayList<String> movieList = new ArrayList<>();
        try	{
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                movieList.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("Shit gone wrong :(");
            e.printStackTrace();
        } finally {
            if (stmt != null) { stmt.close(); }
        }
        return movieList;
    }

    public List<String> getDatesForMovie(String movie) throws SQLException {
        ArrayList<String> dateList = new ArrayList<>();
        Statement stmt = null;
        String query =
                "select performance_date from performances left join movies on movies.id = performances.movie_id where movies.name = '" + movie + "'";

        try	{
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                dateList.add(String.valueOf(rs.getDate("performance_date")));
            }
        } catch (SQLException e) {
            System.out.println("Shit gone wrong :(");
            e.printStackTrace();
        } finally {
            if (stmt != null) { stmt.close(); }
        }
        return dateList;
    }

    public Show getPerformanceData(String movie, String date) throws SQLException {
        Integer freeSeats = 0;
        String theater = "";

        Statement stmt = null;
        String query =
                "SELECT reserved_seats, max_seats, theater_name  " +
                "FROM seat_reservations " +
                "WHERE movie_name = '" + movie + "' AND " +
                "performance_date = '" + date + "'";

        try	{
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.first();
            theater = rs.getString("theater_name");
            freeSeats = rs.getInt("max_seats") - rs.getInt("reserved_seats");
        } catch (SQLException e) {
            System.out.println("Shit gone wrong :(");
            e.printStackTrace();
        } finally {
            if (stmt != null) { stmt.close(); }
        }

        return new Show(movie, date, theater, freeSeats);
    }

    public boolean login(String username) throws SQLException {
        Statement stmt = null;

        String query = "select count(*) as user_count from users where username = '" + username + "'";
        try	{
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.first();
            return rs.getInt("user_count") == 1;
        } catch (SQLException e) {
            System.out.println("Shit gone wrong :(");
            e.printStackTrace();
        } finally {
            if (stmt != null) { stmt.close(); }
        }
        return false;
    }
}

