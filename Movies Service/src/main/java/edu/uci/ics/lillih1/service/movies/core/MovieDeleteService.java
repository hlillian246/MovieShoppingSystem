package edu.uci.ics.lillih1.service.movies.core;

import edu.uci.ics.lillih1.service.movies.MovieService;
import edu.uci.ics.lillih1.service.movies.logger.ServiceLogger;
import edu.uci.ics.lillih1.service.movies.models.MovieDeleteResponseModel;
import edu.uci.ics.lillih1.service.movies.models.VerifyPrivilegeResponseModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MovieDeleteService {

    public static MovieDeleteResponseModel deleteMovie(String email, String movieId) throws Exception
    {
        VerifyPrivilegeResponseModel privilegeResponseModel = IDMPrivilegeService.isUserAllowedToMakeRequest(email,3);
        boolean hasPrivilege = privilegeResponseModel != null
                && privilegeResponseModel.getResultCode() == 140;

        if (!hasPrivilege)
        {
            return new MovieDeleteResponseModel(141, "User has insufficient privilege.");
        }

        MovieDeleteResponseModel responseModel = removeMovieDB(movieId);
        return responseModel;
    }

    public static MovieDeleteResponseModel removeMovieDB(String movieId) throws Exception
    {
        try
        {
            String query = "SELECT id, hidden FROM movies WHERE id = ?;";

            ServiceLogger.LOGGER.info("Query: " + query);

            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, movieId);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            if (rs.next())
            {
                if (rs.getInt("hidden") == 0) {
                    String q1 = "UPDATE movies SET hidden = 1 WHERE id = ?;";

                    ServiceLogger.LOGGER.info("Query: " + q1);

                    ps = MovieService.getCon().prepareStatement(q1);
                    ps.setString(1, movieId);

                    ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
                    ps.execute();
                    ServiceLogger.LOGGER.info("Query succeeded.");

                    return new MovieDeleteResponseModel(240,"Movie successfully removed.");
                }
                else
                {
                    return new MovieDeleteResponseModel(242,"Movie has been already removed.");
                }

            }
            else {
                ServiceLogger.LOGGER.info("Movie doesn't exist for id: " + movieId);

                return new MovieDeleteResponseModel(241, "Could not remove movie.");
            }
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
}
