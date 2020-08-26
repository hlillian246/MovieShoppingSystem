package edu.uci.ics.lillih1.service.movies.core;

import edu.uci.ics.lillih1.service.movies.MovieService;
import edu.uci.ics.lillih1.service.movies.logger.ServiceLogger;
import edu.uci.ics.lillih1.service.movies.models.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovieIDService {
    public static MovieIDResponseModel searchMovieIDs(String email, String id) throws Exception
    {
        VerifyPrivilegeResponseModel privilegeResponseModel = IDMPrivilegeService.isUserAllowedToMakeRequest(email,4);

        boolean hasPrivilege = privilegeResponseModel != null
                && privilegeResponseModel.getResultCode() == 140;

        MovieModel movie = fetchMovieRecords(id, hasPrivilege);

        if (movie == null) {
            return new MovieIDResponseModel(211, "No movies found with search parameters.");
        }

        if (!hasPrivilege && movie.getHidden()) {
            return new MovieIDResponseModel(141, "User has insufficient privilege.");
        }

        movie.setHidden(null);
        return new MovieIDResponseModel(210, "Found movies with search parameters.", movie);
    }

    public static MovieModel fetchMovieRecords(String id, boolean hasPrivilege) throws Exception
    {
        try
        {
            String query = "SELECT id, title, director, year, backdrop_path, budget, overview, " +
                    "poster_path, revenue, hidden FROM movies WHERE id = ?; ";

            ServiceLogger.LOGGER.info("Query: " + query);
            ServiceLogger.LOGGER.info("id: " + id);

            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, id);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            if (!rs.next()) return null;

            MovieModel movie = new MovieModel();
            movie.setMovieId(rs.getString("id"));
            movie.setTitle(rs.getString("title"));
            movie.setDirector(rs.getString("director"));
            movie.setYear(rs.getInt("year"));
            movie.setBackdrop_path(rs.getString("backdrop_path"));


            int budget = rs.getInt("budget");
            movie.setBudget(budget == 0 ? null : budget);

            movie.setOverview(rs.getString("overview"));
            movie.setPoster_path(rs.getString("poster_path"));

            int revenue = rs.getInt("revenue");
            movie.setRevenue(revenue == 0 ? null : revenue);

            movie.setHidden(rs.getInt("hidden") != 0);

            if (!hasPrivilege && movie.getHidden()) return movie;

            query = "SELECT rating, numVotes from ratings where movieId = ?;";

            ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, movie.getMovieId());
            rs = ps.executeQuery();

            if (rs.next())
            {
                movie.setRating(rs.getFloat("rating"));
                movie.setNumVotes(rs.getInt("numVotes"));
            }
            else
            {
                movie.setRating(0.0f);
                movie.setNumVotes(0);
            }

            query = "SELECT g.id as id, g.name as name from genres g, genres_in_movies gm where g.id = gm.genreId and gm.movieId = ?;";
            ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, movie.getMovieId());
            rs = ps.executeQuery();

            List<GenreModel> genres = new ArrayList<GenreModel>();
            while (rs.next())
            {
                GenreModel g = new GenreModel();
                g.setId(rs.getInt("id"));
                g.setName(rs.getString("name"));
                genres.add(g);
            }
            movie.setGenres(genres);

            query = "SELECT s.id as id, s.name as name, s.birthYear as birthYear from stars s, stars_in_movies sm where s.id = sm.starId and sm.movieId = ?;";
            ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, movie.getMovieId());
            rs = ps.executeQuery();

            List<StarModel> stars = new ArrayList<StarModel>();
            while (rs.next())
            {
                StarModel s = new StarModel();
                s.setId(rs.getString("id"));
                s.setName(rs.getString("name"));
                s.setBirthYear(rs.getInt("birthYear"));
                stars.add(s);
            }
            movie.setStars(stars);

            return movie;
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
