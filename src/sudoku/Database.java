package sudoku;

import java.sql.*;
import java.util.*;

public class Database {

    // variable from connection class
    private Connection conn = null;

    /**
     * Connects to the database
     * @author Muhammad Khairala
     * @return connection/null
     */
    public Connection DBconnect() {
        try {
            // next 2 lines are used to connect the DB if connected return the connection else return NULL
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:SudokuDB.sqlite");
            return conn;
        } catch (ClassNotFoundException | SQLException e) {
            return null;
        }
    }

    /**
     * Select Function will be used to select from 2 tables (1) allSudoku (2) Load  
     * it will return a random soduko from the mode the user Specified and it will return null
     * if there wasn't and saved sudoku in Load table
     * @author Muhammad Khairala
     * @param Difficulty
     * @param choice
     * @return array of games
     * @throws SQLException 
     */
    public ArrayList<String> Select() throws SQLException {
        Statement stmt = conn.createStatement(); // variable from statement class used to write query in to be excuted
        String query = "Select *,allSudoku.Sudoku as original from Load JOIN allSudoku ON load.originalID = allSudoku.ID order by load.savingTime desc";  // get the saved sudoku from Load table
        // variable from result set class to take the result of the query 
        ResultSet result = stmt.executeQuery(query); //Query Excution
        ArrayList<String> arr = new ArrayList();    // array used to get the results
        while (result.next()) {
                arr.add(result.getInt("ID") + "," + result.getString("Sudoku") + "," + result.getInt("Timer")
                        + "," + result.getString("Diff") + "," + result.getString("savingTime") + "," + result.getString("original") + "," + result.getString("originalID")); // get all Sudokus in Load Table
        }
        if (arr.isEmpty()) {
            return null; //return null if the array is empty
        }else {
            return arr; //return the array which contains all the loaded sudoku
        }
    }
    
    public int saveOriginalSudoku(String sudoku, String level) throws SQLException{
        String query = "INSERT INTO allSudoku (Sudoku , Diff) Values (" + "\"" + sudoku + "\"" + "," + "\"" + level + "\"" +")";
        PreparedStatement statement = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
        statement.executeUpdate();
        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next()) {
            return generatedKeys.getInt(1);
        }
        return 0;
    }

    /**
     * @author Muhammad Khairala, Muhamamd Kamal
     * @param SU
     * @param Timer
     * @param originalId
     * @param oldId
     * @return
     * @throws SQLException 
     */
    public long saveGame(String SU, int Timer, int originalId, int oldId) throws SQLException {
        String query = null;
        query = "INSERT INTO Load (Sudoku , Timer , originalID) Values ( " + "\"" + SU + "\"" + "," + "\"" + Timer + "\"" + "," + "\"" + originalId + "\"" + ")";
        PreparedStatement statement = conn.prepareStatement(query,
                Statement.RETURN_GENERATED_KEYS);
        statement.executeUpdate();
        this.deleteGame(oldId);
        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next()) {
            return generatedKeys.getLong(1);
        }
        return 0;
    }

    /**
     * @author Muhammad Khairala, Muhammad Kamal
     * @param id
     * @throws SQLException 
     */
    public void deleteGame(int id) throws SQLException {
        Statement stmt = conn.createStatement(); // variable from statement class used to write query in to be excuted
        String query = "DELETE FROM LOAD WHERE ID = " + id;
        stmt.executeUpdate(query);
    }

    /**
     * @author Muhammad Khairala, Muhammad Kamal
     * @param name
     * @param time
     * @param Diff
     * @throws SQLException 
     */
    public void addNewScore(String name, int time, String Diff) throws SQLException {
        Statement stmt = conn.createStatement();
        String query = "INSERT INTO Dashboard (name , Diff , Time) Values ( " + "\"" + name + "\"" + "," + "\"" + Diff + "\"" + "," + "\"" + time + "\"" + ")";
        stmt.executeUpdate(query);
    }

    /**
     * @author Muhammad Khairala, Muhammad Kamal
     * @param Diff
     * @return
     * @throws SQLException 
     */
    public ArrayList<String> bestFiveTimes(String Diff) throws SQLException {
        Statement stmt = conn.createStatement();
        String query = "SELECT * FROM Dashboard where Diff = \"" + Diff + "\" order by time limit 5";
        ResultSet reuslt = stmt.executeQuery(query);
        ArrayList<String> x = new ArrayList<>();
        while (reuslt.next()) {
            x.add(reuslt.getString("Name") + "," + reuslt.getString("Diff") + "," + reuslt.getString("Time"));
        }
        return x;
    }
}
