package edu.uci.ics.lillih1.service.movies.core;

import edu.uci.ics.lillih1.service.movies.MovieService;
import edu.uci.ics.lillih1.service.movies.logger.ServiceLogger;
import edu.uci.ics.lillih1.service.movies.models.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StarService
{
    private static int lastStarId = -1;

    public static StarSearchResponseModel searchStars(StarSearchRequestModel requestModel, String email) throws Exception
    {

        ServiceLogger.LOGGER.info("Searching for stars...");

        // Verify that the requester has the correct privilege level to make the request
//        VerifyPrivilegeResponseModel privilegeResponseModel = IDMPrivilegeService.isUserAllowedToMakeRequest(email,5);
//        boolean hasPrivilege = privilegeResponseModel != null
//                && privilegeResponseModel.getResultCode() == 140;

        List<StarModel> stars = fetchStarRecords(requestModel);

        if (stars.size() > 0)
        {
            return new StarSearchResponseModel(212, "Found stars with search parameters.", stars);
        }
        else
        {
            return new StarSearchResponseModel(213, "No stars found with search parameters.");
        }
    }

    public static List<StarModel> fetchStarRecords(StarSearchRequestModel requestModel) throws Exception
    {
        try {

            String query = "SELECT distinct s.id as id, s.name as name, s.birthYear as birthYear FROM stars s ";

            if (requestModel.getMovieTitle() != null )
            {
                query += "join stars_in_movies sm on s.id = sm.starId ";
                query += "join movies m on sm.movieId = m.id ";
            }

            boolean flag = false;

            if (requestModel.getName() != null) {
                query += flag ? "AND s.name LIKE CONCAT('%', ?, '%') " : "WHERE s.name LIKE CONCAT('%', ?, '%') ";
                flag = true;
            }

            if (requestModel.getBirthYear() != null) {
                query += flag ? "AND s.birthYear = ? " : "WHERE s.birthYear = ? ";
                flag = true;
            }

            if (requestModel.getMovieTitle() != null) {
                query += flag ? "AND m.title LIKE CONCAT('%', ?, '%')  " : "WHERE m.title LIKE CONCAT('%', ?, '%') ";
                flag = true;
            }

            query += "ORDER BY " + requestModel.getOrderby() + " " + requestModel.getDirection();
            if (requestModel.getOrderby().equalsIgnoreCase("NAME"))
            {
                query += ", birthYear ASC ";
            }
            else
            {
                query += ", name ASC ";
            }

            query += "LIMIT " + requestModel.getLimit() + " OFFSET " + requestModel.getOffset() + "; ";

            ServiceLogger.LOGGER.info("Query: " + query);

            PreparedStatement ps = MovieService.getCon().prepareStatement(query);

            int col = 1;
            if (requestModel.getName() != null) {
                ps.setString(col++, requestModel.getName());
            }
            if (requestModel.getBirthYear() != null) {
                ps.setInt(col++, requestModel.getBirthYear());
            }
            if (requestModel.getMovieTitle() != null) {
                ps.setString(col++, requestModel.getMovieTitle());
            }

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            List<StarModel> stars = new ArrayList<StarModel>();
            while (rs.next())
            {
                StarModel star = new StarModel();
                star.setName(rs.getString("name"));
                star.setBirthYear(rs.getInt("birthYear"));
                star.setId(rs.getString("id"));

                stars.add(star);
            }

            return stars;
        }
        catch(SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: Unable to retrieve string records.");
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

    public static StarIDResponseModel searchStarID(String email, String id) throws Exception
    {
        String query = "select id, name, birthYear from stars where id = ?;";

        PreparedStatement ps = MovieService.getCon().prepareStatement(query);
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next())
        {
            StarModel star = new StarModel(rs.getString("id"),
                    rs.getString("name"), rs.getInt("birthYear"));
            return new StarIDResponseModel(212, "Found stars with search parameters.", star);
        }
        else
        {
            return new StarIDResponseModel(213,"No stars found with search parameters.");
        }
    }

    public static StarAddResponseModel addStar(String email, StarAddRequestModel requestModel) throws Exception
    {
        VerifyPrivilegeResponseModel privilegeResponseModel = IDMPrivilegeService.isUserAllowedToMakeRequest(email, 3);
        boolean hasPrivilege = privilegeResponseModel != null
                                    && privilegeResponseModel.getResultCode() == 140;

        if (!hasPrivilege) {
            return new StarAddResponseModel(141, "User has insufficient privilege.");
        }

        String query = "select name from stars where name = ?;";
        PreparedStatement ps = MovieService.getCon().prepareStatement(query);
        ps.setString(1, requestModel.getName());
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new StarAddResponseModel(222, "Star already exists.");
        }

        String starId = generateNewStarID();
        try {
            insertStar(starId, requestModel);
            ServiceLogger.LOGGER.info("Done add a star with id: " + starId);

            return new StarAddResponseModel(220,"Star successfully added.");
        } catch (Exception e) {
            return new StarAddResponseModel(221, "Could not add star.");
        }

    }

    public static void insertStar(String starId, StarAddRequestModel requestModel) throws Exception
    {
        try
        {
            String query = null;

            if (requestModel.getBirthYear() != null) {
                query = "INSERT INTO stars (id, name, birthYear) VALUES (?, ?, ?); ";
            }
            else
            {
                query = "INSERT INTO stars (id, name) VALUES (?, ?); ";
            }

            PreparedStatement ps = MovieService.getCon().prepareStatement(query);

            ServiceLogger.LOGGER.info("Query: " + query);

            ps.setString(1, starId);
            ps.setString(2, requestModel.getName());
            if (requestModel.getBirthYear() != null) {
                ps.setInt(3, requestModel.getBirthYear());
            }

            ServiceLogger.LOGGER.info("Query: " + ps.toString());
            ps.execute();
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

    public static String generateNewStarID() throws Exception
    {
        if (lastStarId != -1)
        {
            String formattedNumber = String.format("%07d", ++lastStarId);
            return "ss" + formattedNumber;
        }

        String query = "select id from stars where id like 'ss%' order by id desc LIMIT 1;";
        PreparedStatement ps = MovieService.getCon().prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        if (rs.next())
        {
            String last = rs.getString("id");
            lastStarId = Integer.parseInt(last.substring(2));

            String formattedNumber = String.format("%07d", ++lastStarId);
            return "ss" + formattedNumber;
        }
        else
        {
            return "ss0000001";
        }
    }


    public static StarsinResponseModel starsinService(String email, StarsinRequestModel requestModel) throws Exception
    {
        VerifyPrivilegeResponseModel privilegeResponseModel = IDMPrivilegeService.isUserAllowedToMakeRequest(email, 3);
        boolean hasPrivilege = privilegeResponseModel != null
                && privilegeResponseModel.getResultCode() == 140;

        if (!hasPrivilege) {
            return new StarsinResponseModel(141, "User has insufficient privilege.");
        }

        boolean inMovies = movieSearch(requestModel);
        if (!inMovies)
        {
            return new StarsinResponseModel(211,"No movies found with search parameters.");
        }

        boolean inStar = starSearch(requestModel);
        if (inStar)
        {
            return new StarsinResponseModel(232,"Star already exists in movie.");
        }

        try {
            insertStarMovies(requestModel);
            return new StarsinResponseModel(230,"Star successfully added to movie.");
        } catch (Exception e) {
            return new StarsinResponseModel(231, "Could not add star to movie.");
        }
    }

    private static boolean movieSearch (StarsinRequestModel requestModel) throws Exception
    {
        String query = "select id from movies where id = ?;";

        PreparedStatement ps = MovieService.getCon().prepareStatement(query);
        ps.setString(1,requestModel.getMovieid());
        ResultSet rs = ps.executeQuery();

        return rs.next();
    }

    private static boolean starSearch(StarsinRequestModel requestModel) throws Exception
    {
        String query = "select * from stars_in_movies where starId = ? and movieId = ?;";

        PreparedStatement ps = MovieService.getCon().prepareStatement(query);
        ps.setString(1,requestModel.getStarid());
        ps.setString(2,requestModel.getMovieid());
        ResultSet rs = ps.executeQuery();

        return rs.next();
    }


    public static void insertStarMovies(StarsinRequestModel requestModel) throws Exception
    {
        try
        {
            String query = "INSERT INTO stars_in_movies (starId, movieId) values(?,?)";
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1,requestModel.getStarid());
            ps.setString(2,requestModel.getMovieid());

            ServiceLogger.LOGGER.info("Query: " + ps.toString());

            ps.execute();
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
}
