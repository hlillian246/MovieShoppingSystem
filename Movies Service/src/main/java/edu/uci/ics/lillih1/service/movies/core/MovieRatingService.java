package edu.uci.ics.lillih1.service.movies.core;

import edu.uci.ics.lillih1.service.movies.MovieService;
import edu.uci.ics.lillih1.service.movies.logger.ServiceLogger;
import edu.uci.ics.lillih1.service.movies.models.MovieRatingRequestModel;
import edu.uci.ics.lillih1.service.movies.models.MovieRatingResponseModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MovieRatingService {

    public static MovieRatingResponseModel addRatings(MovieRatingRequestModel requestModel) throws Exception
    {
        if (requestModel.getRating() < 0.0 || requestModel.getRating() > 10.0)
        {
            return new MovieRatingResponseModel(251,"Could not update rating.");
        }

        String query = "select id from movies where id = ?;";
        PreparedStatement ps = MovieService.getCon().prepareStatement(query);
        ps.setString(1, requestModel.getId());

        ServiceLogger.LOGGER.info("Query: " + ps.toString());
        ResultSet rs = ps.executeQuery();

        if (!rs.next())
        {
            return new MovieRatingResponseModel(211,"No movies found with search parameters.");
        }

        try
        {
            updateRating(requestModel);
            return new MovieRatingResponseModel(250,"Rating successfully updated.");
        }
        catch(SQLException e)
        {
            return new MovieRatingResponseModel(251,"Could not update rating.");
        }
    }

    private static void updateRating(MovieRatingRequestModel requestModel) throws Exception
    {
        try {
            String query = "select rating, numVotes from ratings where movieId = ?;";
            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, requestModel.getId());

            ServiceLogger.LOGGER.info("Query: " + ps.toString());
            ResultSet rs = ps.executeQuery();

            if (rs.next())
            {
                int currNumVotes = rs.getInt("numVotes");
                int numVotes = currNumVotes + 1;

                float currRating = rs.getFloat("rating");
                float rating = (float) (currRating * currNumVotes + requestModel.getRating()) / (float) (numVotes);

                String query1 = "update ratings set rating = ?, numVotes = ? where movieId = ?; ";
                ps = MovieService.getCon().prepareStatement(query1);

                ps.setFloat(1, rating);
                ps.setInt(2, numVotes);
                ps.setString(3, requestModel.getId());

                ServiceLogger.LOGGER.info("Query: " + ps.toString());
                ps.execute();
            }
            else
            {
                String query1 = "INSERT INTO ratings(movieId,rating,numVotes) VALUES(?,?,?); ";
                ps = MovieService.getCon().prepareStatement(query1);

                ps.setString(1, requestModel.getId());
                ps.setFloat(2, (float) requestModel.getRating());
                ps.setInt(3, 1);

                ServiceLogger.LOGGER.info("Query: " + ps.toString());
                ps.execute();
            }
        }
        catch(SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: Unable to updating rating.");
            e.printStackTrace();
            throw e;
        }
    }
}
