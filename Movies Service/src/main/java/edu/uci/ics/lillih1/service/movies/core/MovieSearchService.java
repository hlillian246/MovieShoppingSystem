package edu.uci.ics.lillih1.service.movies.core;

import edu.uci.ics.lillih1.service.movies.MovieService;
import edu.uci.ics.lillih1.service.movies.logger.ServiceLogger;
import edu.uci.ics.lillih1.service.movies.models.MovieModel;
import edu.uci.ics.lillih1.service.movies.models.MovieSearchRequestModel;
import edu.uci.ics.lillih1.service.movies.models.MovieSearchResponseModel;
import edu.uci.ics.lillih1.service.movies.models.VerifyPrivilegeResponseModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovieSearchService {

    public static MovieSearchResponseModel searchMovies(MovieSearchRequestModel requestModel, String email) throws Exception
    {

        ServiceLogger.LOGGER.info("Searching for Movies...");
        // Verify that the requester has the correct privilege level to make the request

        VerifyPrivilegeResponseModel privilegeResponseModel = IDMPrivilegeService.isUserAllowedToMakeRequest(email,3);
        boolean hasPrivilege = privilegeResponseModel != null
                && privilegeResponseModel.getResultCode() == 140;

        if (!hasPrivilege) requestModel.setIncludeHidden(false);

        List<MovieModel> movies = fetchMovieRecords(requestModel, hasPrivilege);

        if (movies.size() > 0)
        {
            return new MovieSearchResponseModel(210, "Found movies with search parameters.", movies);
        }
        else
        {
            return new MovieSearchResponseModel(211, "No movies found with search parameters.");
        }

    }

    public static List<MovieModel> fetchMovieRecords(MovieSearchRequestModel requestModel, boolean hasPrivilege) throws Exception
    {
        try {

            String query = "SELECT distinct m.id as id, m.title as title, m.director as director, m.year as year, " +
                    "m.hidden as hidden, r.movieId as rid, r.rating as rating, r.numVotes as numVotes FROM movies m" +
                    " left join ratings r on m.id = r.movieId ";

            if (requestModel.getGenre() != null )
            {
                query += "join genres_in_movies gm on m.id = gm.movieId ";
                query += "join genres g on gm.genreId = g.id ";
            }

            boolean flag = false;

            if (requestModel.getTitle() != null) {
                query += flag ? "AND title LIKE CONCAT('%', ?, '%') " : "WHERE title LIKE CONCAT('%', ?, '%') ";
                flag = true;
            }

            if (requestModel.getGenre() != null) {
                query += flag ? "AND g.name LIKE CONCAT('%', ?, '%') " : "WHERE g.name LIKE CONCAT('%', ?, '%') ";
                flag = true;
            }

            if (requestModel.getYear() != null) {
                query += flag ? "AND year = ? " : "WHERE year = ? ";
                flag = true;
            }

            if (requestModel.getDirector() != null) {
                query += flag ? "AND director LIKE CONCAT('%', ?, '%') " : "WHERE director LIKE CONCAT('%', ?, '%') ";
                flag = true;
            }

            if (requestModel.getIncludeHidden() != null) {
                query += flag ? "AND hidden = ? " : "WHERE hidden = ? ";
            }

            query += "ORDER BY " + requestModel.getOrderby() + " " + requestModel.getDirection();
            if (requestModel.getOrderby().equalsIgnoreCase("RATING"))
            {
                query += ", title ASC ";
            }
            else
            {
                query += ", rating DESC ";
            }

            query += "LIMIT " + requestModel.getLimit() + " OFFSET " + requestModel.getOffset() + "; ";

            ServiceLogger.LOGGER.info("Query: " + query);

            PreparedStatement ps = MovieService.getCon().prepareStatement(query);

            int col = 1;

            if (requestModel.getTitle() != null) {
                ps.setString(col++, requestModel.getTitle());
            }
            if (requestModel.getGenre() != null) {
                ps.setString(col++, requestModel.getGenre());
            }
            if (requestModel.getYear() != null) {
                ps.setInt(col++, requestModel.getYear());
            }

            if (requestModel.getDirector() != null) {
                ps.setString(col++, requestModel.getDirector());
            }

            if (requestModel.getIncludeHidden() != null) {
                ps.setInt(col++, requestModel.getIncludeHidden() ? 1 : 0);
            }

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            List<MovieModel> movies = new ArrayList<MovieModel>();
            while (rs.next()) {
                boolean isHidden = rs.getInt("hidden") != 0;
                if (isHidden && !hasPrivilege) continue;

                MovieModel movie = new MovieModel();
                movie.setMovieId(rs.getString("id"));
                movie.setTitle(rs.getString("title"));
                movie.setDirector(rs.getString("director"));
                movie.setYear(rs.getInt("year"));

//                Float rating = rs.getFloat("rating");

                String rid = rs.getString("rid");
                if (rid != null) {
                    movie.setRating(rs.getFloat("rating"));
                    movie.setNumVotes(rs.getInt("numVotes"));
                }

                if (hasPrivilege)
                    movie.setHidden(rs.getInt("hidden") != 0);

                movies.add(movie);
            }
            return movies;
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
