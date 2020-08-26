package edu.uci.ics.lillih1.service.movies.core;

import edu.uci.ics.lillih1.service.movies.MovieService;
import edu.uci.ics.lillih1.service.movies.logger.ServiceLogger;
import edu.uci.ics.lillih1.service.movies.models.MovieAddRequestModel;
import edu.uci.ics.lillih1.service.movies.models.MovieAddResponseModel;
import edu.uci.ics.lillih1.service.movies.models.GenreModel;
import edu.uci.ics.lillih1.service.movies.models.VerifyPrivilegeResponseModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovieAddService {
    private static int lastMovieId = -1;

    public static MovieAddResponseModel addMovie(MovieAddRequestModel requestModel, String email) throws Exception {
        ServiceLogger.LOGGER.info("Searching for Movies...");

        // Verify that the requester has the correct privilege level to make the request
        VerifyPrivilegeResponseModel privilegeResponseModel = IDMPrivilegeService.isUserAllowedToMakeRequest(email, 3);
        boolean hasPrivilege = privilegeResponseModel != null
                && privilegeResponseModel.getResultCode() == 140;

        if (!hasPrivilege) {
            return new MovieAddResponseModel(141, "User has insufficient privilege.");
        }

        if (checkExistence(requestModel)) {
            return new MovieAddResponseModel(216, "Movie already exists.");
        }

        String movieId = generateNewMovieID();
        try {
            ServiceLogger.LOGGER.info("Inserting genres: ");
            List<Integer> genresIds = insertGenres(requestModel.getGenres());

            ServiceLogger.LOGGER.info("Inserting movie: ");
            insertMovie(movieId, requestModel);

            ServiceLogger.LOGGER.info("Inserting genres_in_movies: ");
            insertGenreInMovie(movieId, genresIds);

            ServiceLogger.LOGGER.info("Inserting rating: ");
            insertRating(movieId);

            return new MovieAddResponseModel(214, "Movie successfully added.", movieId, genresIds);

        } catch (Exception e) {
            e.printStackTrace();
            return new MovieAddResponseModel(215, "Could not add movie.");
        }

    }

    private static boolean checkExistence(MovieAddRequestModel requestModel) throws Exception
    {
        String query = "SELECT distinct id FROM movies ";

        query += "WHERE title = ? ";
        query += "AND director = ? ";
        query += "AND year = ? ";

        if (requestModel.getBackdrop_path() != null) {
            query += "AND backdrop_path = ? ";
        }

        if (requestModel.getBudget() != null) {
            query += "AND budget = ? ";
        }

        if (requestModel.getOverview() != null) {
            query += "AND overview = ? ";
        }

        if (requestModel.getPoster_path() != null) {
            query += "AND poster_path = ? ";
        }

        if (requestModel.getRevenue() != null) {
            query += "AND revenue = ? ";
        }

        query += "AND hidden = 0; ";

        PreparedStatement ps = MovieService.getCon().prepareStatement(query);

        ps.setString(1, requestModel.getTitle());
        ps.setString(2, requestModel.getDirector());
        ps.setInt(3, requestModel.getYear());

        int col = 4;

        if (requestModel.getBackdrop_path() != null) {
            ps.setString(col++, requestModel.getBackdrop_path());
        }

        if (requestModel.getBudget() != null) {
            ps.setInt(col++, requestModel.getBudget());
        }

        if (requestModel.getOverview() != null) {
            ps.setString(col++, requestModel.getOverview());
        }

        if (requestModel.getPoster_path() != null) {
            ps.setString(col++, requestModel.getPoster_path());
        }

        if (requestModel.getRevenue() != null) {
            ps.setInt(col++, requestModel.getRevenue());
        }

        ServiceLogger.LOGGER.info("Query: " + ps.toString());

        ResultSet rs = ps.executeQuery();

        return rs.next();

    }


    public static void insertMovie(String movieId, MovieAddRequestModel requestModel) throws Exception
    {
        ServiceLogger.LOGGER.info("Insert a movie: " + movieId);

        try
        {
            String nameStr = "INSERT INTO movies (id";
            String valStr = ") VALUES ('" + movieId + "'";

            if (requestModel.getTitle() != null) {
                nameStr += ",title";
                valStr += ",?";
            }

            if (requestModel.getYear() != null)
            {
                nameStr += ",year";
                valStr += ",?";
            }

            if (requestModel.getDirector() != null)
            {
                nameStr += ",director";
                valStr += ",?";
            }

            if (requestModel.getBackdrop_path() != null )
            {
                nameStr += ",backdrop_path";
                valStr += ",?";
            }

            if (requestModel.getBudget() != null)
            {
                nameStr += ",budget";
                valStr += ",?";
            }

            if (requestModel.getOverview() != null)
            {
                nameStr += ",overview";
                valStr += ",?";
            }

            if (requestModel.getPoster_path() != null)
            {
                nameStr += ",poster_path";
                valStr += ",?";
            }

            if (requestModel.getRevenue() != null)
            {
                nameStr += ",revenue";
                valStr += ",?";
            }

            valStr += ");";

            String query = nameStr + valStr;

            ServiceLogger.LOGGER.info("Query: " + query);

            PreparedStatement ps = MovieService.getCon().prepareStatement(query);

            int col = 1;

            if (requestModel.getTitle() != null) {
                ps.setString(col++, requestModel.getTitle());
            }

            if (requestModel.getYear() != null)
            {
                ps.setInt(col++, requestModel.getYear());
            }

            if (requestModel.getDirector() != null)
            {
                ps.setString(col++, requestModel.getDirector());
            }

            if (requestModel.getBackdrop_path() != null )
            {
                ps.setString(col++, requestModel.getBackdrop_path());
            }

            if (requestModel.getBudget() != null)
            {
                ps.setInt(col++, requestModel.getBudget());
            }

            if (requestModel.getOverview() != null)
            {
                ps.setString(col++, requestModel.getOverview());
            }

            if (requestModel.getPoster_path() != null)
            {
                ps.setString(col++, requestModel.getPoster_path());
            }

            if (requestModel.getRevenue() != null)
            {
                ps.setInt(col++, requestModel.getRevenue());
            }

//            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ps.execute();

            ServiceLogger.LOGGER.info("Insert executed: ");

        }
        catch(SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: Unable to insert movie records.");
            e.printStackTrace();
            throw e;
        }
        catch(Exception e)
        {
            ServiceLogger.LOGGER.warning("Exception occurs");
            e.printStackTrace();
            throw e;
        }
    }

    private static String generateNewMovieID() throws Exception
    {
        if (lastMovieId != -1)
        {
            String formattedNumber = String.format("%07d", ++lastMovieId);
            return "cs" + formattedNumber;
        }

        String query = "select id from movies where id like 'CS%' order by id desc LIMIT 1;";
        PreparedStatement ps = MovieService.getCon().prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        if (rs.next())
        {
            String last = rs.getString("id");
            lastMovieId = Integer.parseInt(last.substring(2));

            String formattedNumber = String.format("%07d", ++lastMovieId);
            return "cs" + formattedNumber;
        }
        else
        {
            return "cs0000001";
        }

    }

    public static List<Integer> insertGenres(List<GenreModel> genres) throws Exception
    {
        List<Integer> ids = new ArrayList<Integer>();

        for (GenreModel genre : genres)
        {
            String query1 = "select id, name from genres where name = ?;";
            PreparedStatement ps = MovieService.getCon().prepareStatement(query1);
            ps.setString(1, genre.getName());
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                String query2 = "INSERT INTO genres(name) VALUES(?);";

                ps = MovieService.getCon().prepareStatement(query2);
                ps.setString(1, genre.getName());
                ps.execute();

                ServiceLogger.LOGGER.info("Inserted genre");


                String query3 = "select id from genres where name = ?; ";

                ps = MovieService.getCon().prepareStatement(query3);
                ps.setString(1, genre.getName());

                rs = ps.executeQuery();

                rs.next();
                int newId = rs.getInt("id");
                ids.add(newId);
                ServiceLogger.LOGGER.info("Inserted id: " + newId);
            }
            else
            {
                int fetchedId = rs.getInt("id");
                ids.add(fetchedId);
//                ServiceLogger.LOGGER.info("Fetched id: " + fetchedId);
            }
        }

//        for (int id : ids)
//        {
//            ServiceLogger.LOGGER.info("Final IDs: " + id);
//        }

        return ids;
    }

    public static void insertGenreInMovie(String movieId, List<Integer> genresIdList) throws Exception
    {
        for (int genreId :  genresIdList) {
            String query = "INSERT INTO genres_in_movies (genreId, movieId) VALUES (?, ?);";

            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setInt(1, genreId);
            ps.setString(2, movieId);

//            ServiceLogger.LOGGER.info("Query: " + ps.toString());

            ps.execute();
        }
    }


    public static List<Integer> getGenreIds(MovieAddRequestModel requestModel)
    {
        List<Integer> genresList = new ArrayList<Integer>();
        for(GenreModel genre: requestModel.getGenres())
        {
            genresList.add(genre.getId());
        }

        return genresList;
    }


    private static void insertRating(String movieId) throws Exception
    {
        String query = "INSERT INTO ratings(movieId,rating,numVotes) VALUES (?,?,?); ";
        PreparedStatement ps = MovieService.getCon().prepareStatement(query);
        ps.setString(1, movieId);
        ps.setFloat(2,0.0f);
        ps.setInt(3,0);
        ps.execute();
    }
}
