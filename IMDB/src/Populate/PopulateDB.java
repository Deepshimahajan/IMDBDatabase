package Populate;
import FileOperation.FileRead;

import java.io.IOException;
import java.sql.*;

public class PopulateDB {
    public static void main(String args[]) throws IOException {
        PopulateDB popDB = new PopulateDB();
        popDB.run();
    }

    private void run() throws IOException {
        Connection connect = null;
        try {
            connect = openConnection();

            populateMovies(connect);
            populateTags(connect);
            populateMovie_Countries(connect);
            populateMovie_Locations(connect);
            populateMovie_Genres(connect);
            populateMovie_Tags(connect);
//            showTableContent(connect);
        } catch (SQLException e) {
            System.err.println("Errors occurs when communicating with the database server: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Cannot find the database driver");
        } finally {
            // Never forget to close database connection
            closeConnection(connect);
        }
    }

    /**
     * Using prepared statement that is faster than regular statement.
     * @param connect
     * @throws SQLException
     * @throws IOException
     */

    //populating the movies table
    private void populateMovies(Connection connect) throws SQLException {
        Statement stmt1 = connect.createStatement();
        stmt1.executeQuery("DELETE FROM movies");
        FileRead fR = new FileRead("/Users/deepshi/Desktop/IMDB Faceted Search/DatFiles/movies.dat");
        String[] str = null;
        try {
            str = fR.openFile();

            String[] words = null;

            PreparedStatement stmt = connect.prepareStatement("INSERT INTO movies VALUES(?,?,?,?,?,?,?,?,?)");
            //id,title,year,rtAllCriticsRating,rtAllCriticsNumReviews,rtTopCriticsRating,rtTopCriticsNumReviews,rtAudienceRating,rtAudienceNumRatings
            for(int i = 1; i < str.length; i++)
            {
                words = str[i].split("\\t");
                stmt.setString(1, words[0]);
                stmt.setString(2, words[1]);
                stmt.setString(3, words[4]);
                stmt.setString(4, words[6]);
                stmt.setString(5, words[7]);
                stmt.setString(6, words[11]);
                stmt.setString(7, words[12]);
                stmt.setString(8, words[16]);
                stmt.setString(9, words[17]);
                stmt.executeUpdate();
            }
            stmt.close();
            stmt1.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.err.println("Cannot close connection: " + e.getMessage());
        }
    }


    //populate the movie_tags table
    private void populateMovie_Tags(Connection connect) throws SQLException {
        Statement stmt1 = connect.createStatement();
        stmt1.executeQuery("DELETE FROM movie_tags");
        FileRead fR = new FileRead("/Users/deepshi/Desktop/IMDB Faceted Search/DatFiles/movie_tags.dat");
        String[] str = null;
        try {
            str = fR.openFile();

            String[] words = null;

            PreparedStatement stmt = connect.prepareStatement("INSERT INTO movie_tags VALUES(?,?,?)");
            for(int i = 1; i < str.length; i++)
            {
                words = str[i].split("\\s");
                for(int j = 3; j < words.length; j++)
                {
                    words[2] += " " + words[j] + " ";
                }

                if(words.length<3)
                {
                    stmt.setString(1, words[0]);
                    stmt.setString(2, " ");
                }
                else
                {
                    stmt.setString(1, words[0]);
                    stmt.setString(2, words[1]);
                    stmt.setString(3, words[2]);
                }
                stmt.executeUpdate();
            }
            stmt.close();
            stmt1.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.err.println("Cannot close connection: " + e.getMessage());
        }
    }



    //populating the tags table
    private void populateTags(Connection connect) throws SQLException {
        Statement stmt1 = connect.createStatement();
        stmt1.executeQuery("DELETE FROM tags");
        FileRead fR = new FileRead("/Users/deepshi/Desktop/IMDB Faceted Search/DatFiles/tags.dat");
        String[] str = null;
        try {
            str = fR.openFile();

            String[] words = null;

            PreparedStatement stmt = connect.prepareStatement("INSERT INTO tags VALUES(?,?)");
            for(int i = 1; i < str.length; i++)
            {
                words = str[i].split("\\s");
                for(int j = 2; j < words.length; j++)
                {
                    words[1] += " " + words[j] + " ";
                }
                if(words.length<2)
                {
                    stmt.setString(1, words[0]);
                    stmt.setString(2, " ");
                }
                else
                {
                    stmt.setString(1, words[0]);
                    stmt.setString(2, words[1]);
                }
                stmt.executeUpdate();
            }
            stmt.close();
            stmt1.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.err.println("Cannot close connection: " + e.getMessage());
        }
    }

    //populating the movie_countries table
    private void populateMovie_Countries(Connection connect) throws SQLException {
        Statement stmt1 = connect.createStatement();
        stmt1.executeQuery("DELETE FROM movie_countries");
        FileRead fR = new FileRead("/Users/deepshi/Desktop/IMDB Faceted Search/DatFiles/movie_countries.dat");
        String[] str = null;
        try {
            str = fR.openFile();

            String[] words = null;

            PreparedStatement stmt = connect.prepareStatement("INSERT INTO movie_countries VALUES(?,?)");
            for(int i = 1; i < str.length; i++)
            {
                words = str[i].split("\\s");
                for(int j = 2; j < words.length; j++)
                {
                    words[1] += " " + words[j] + " ";
                }
                if(words.length<2)
                {
                    stmt.setString(1, words[0]);
                    stmt.setString(2, " ");
                }
                else
                {
                    stmt.setString(1, words[0]);
                    stmt.setString(2, words[1]);
                }
                stmt.executeUpdate();
            }
            stmt.close();
            stmt1.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.err.println("Cannot close connection: " + e.getMessage());
        }
    }


    //populating the movie_locations table
    private void populateMovie_Locations(Connection connect) throws SQLException {
        Statement stmt1 = connect.createStatement();
        stmt1.executeQuery("DELETE FROM movie_locations");
        System.out.print("Populating movie Location table");
        FileRead fR = new FileRead("/Users/deepshi/Desktop/IMDB Faceted Search/DatFiles/movie_locations.dat");
        String[] str = null;
        try {
            str = fR.openFile();

            String[] words = null;

            PreparedStatement stmt = connect.prepareStatement("INSERT INTO movie_locations VALUES(?,?,?,?,?)");
            for(int i = 1; i < str.length; i++)
            {
                words = str[i].split("\\s");

                if(words.length<2)
                {
                    stmt.setString(1, words[0]);
                    stmt.setString(2, " ");
                    stmt.setString(3, " ");
                    stmt.setString(4, " ");
                    stmt.setString(5, " ");
                }
                else if (words.length < 3)
                {
                    stmt.setString(1, words[0]);
                    stmt.setString(2, words[1]);
                    stmt.setString(3, " ");
                    stmt.setString(4, " ");
                    stmt.setString(5, " ");
                }
                else if (words.length < 4)
                {
                    stmt.setString(1, words[0]);
                    stmt.setString(2, words[1]);
                    stmt.setString(3, words[2]);
                    stmt.setString(4, " ");
                    stmt.setString(5, " ");
                }
                else if (words.length < 5)
                {
                    stmt.setString(1, words[0]);
                    stmt.setString(2, words[1]);
                    stmt.setString(3, words[2]);
                    stmt.setString(4, words[3]);
                    stmt.setString(5, " ");
                }
                else
                {
                    stmt.setString(1, words[0]);
                    stmt.setString(2, words[1]);
                    stmt.setString(3, words[2]);
                    stmt.setString(4, words[3]);
                    stmt.setString(5, words[4]);
                }

                stmt.executeUpdate();
            }
            stmt.close();
            stmt1.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.err.println("Cannot close connection: " + e.getMessage());
        }
    }


    //populating the movie genre table
    private void populateMovie_Genres(Connection connect) throws SQLException {
        Statement stmt1 = connect.createStatement();
        stmt1.executeQuery("DELETE FROM movie_genres");
        FileRead fR = new FileRead("/Users/deepshi/Desktop/IMDB Faceted Search/DatFiles/movie_genres.dat");
        String[] str = null;
        try {
            str = fR.openFile();

            String[] words = null;

            PreparedStatement stmt = connect.prepareStatement("INSERT INTO movie_genres VALUES(?,?)");
            for(int i = 1; i < str.length; i++)
            {
                words = str[i].split("\\s");
                for(int j = 2; j < words.length; j++)
                {
                    words[1] += " " + words[j] + " ";
                }
                if(words.length<2)
                {
                    stmt.setString(1, words[0]);
                    stmt.setString(2, " ");
                }
                else
                {
                    stmt.setString(1, words[0]);
                    stmt.setString(2, words[1]);
                }
                stmt.executeUpdate();
            }
            stmt.close();
            stmt1.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.err.println("Cannot close connection: " + e.getMessage());
        }
    }


//    private void showTableContent(Connection connect) throws SQLException {
//        Statement stmt = connect.createStatement();
//        ResultSet result = stmt.executeQuery("SELECT * FROM USER_TAGGEDMOVIES");
//
//           /*
//           We use ResultSetMetaData.getColumnCount() to know the number columns
//           that are contained.
//           */
//        ResultSetMetaData meta = result.getMetaData();
//        for (int col = 1; col <= meta.getColumnCount(); col++) {
//            System.out.println("Column" + col + ": " + meta.getColumnName(col) +
//                    "\t, Type: " + meta.getColumnTypeName(col));
//        }
//
//           /*
//           Every time we call ResultSet.next(), its internal cursor will advance
//           one tuple. By calling this method continuously, we can iterate through
//           the whole ResultSet.
//           */
//        while (result.next()) {
//            for (int col = 1; col <= meta.getColumnCount(); col++) {
//                System.out.print("\"" + result.getString(col) + "\",");
//            }
//            System.out.println();
//        }
//
//          /*
//          It is always a good practice to close a statement as soon as we
//          no longer use it.
//          */
//        stmt.close();
//    }

    /**
     *
     * @return a database connection
     * @throws java.sql.SQLException when there is an error when trying to connect database
     * @throws ClassNotFoundException when the database driver is not found.
     */
    private Connection openConnection() throws SQLException, ClassNotFoundException {
        // Load the Oracle database driver
        DriverManager.registerDriver(new oracle.jdbc.OracleDriver());

          /*
          Here is the information needed when connecting to a database
          server. These values are now hard-coded in the program. In
          general, they should be stored in some configuration file and
          read at run time.
          */
        String host = "localhost";
        String port = "1521";
        String dbName = "orcl";
        String userName = "scott";
        String password = "oracle";

        // Construct the JDBC URL
        String dbURL = "jdbc:oracle:thin:@" + host + ":" + port + "/" + dbName;
        return DriverManager.getConnection(dbURL, userName, password);
    }

    /**
     * Close the database connection
     * @param connect
     */
    private void closeConnection(Connection connect) {
        try {
            connect.close();
        } catch (SQLException e) {
            System.err.println("Cannot close connection: " + e.getMessage());
        }
    }
}