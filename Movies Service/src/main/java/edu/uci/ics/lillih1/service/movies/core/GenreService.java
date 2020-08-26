package edu.uci.ics.lillih1.service.movies.core;

import edu.uci.ics.lillih1.service.movies.MovieService;
import edu.uci.ics.lillih1.service.movies.logger.ServiceLogger;
import edu.uci.ics.lillih1.service.movies.models.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GenreService {

    public static GenreGetAllResponseModel GetAllGenres(String email) throws Exception
    {
//        VerifyPrivilegeResponseModel privilegeResponseModel = IDMPrivilegeService.isUserAllowedToMakeRequest(email, 3);
//        boolean hasPrivilege = privilegeResponseModel != null
//                && privilegeResponseModel.getResultCode() == 140;
//
//        if (!hasPrivilege) {
//            return new GenreGetAllResponseModel(141, "User has insufficient privilege.");
//        }

        List<GenreModel> genresList = fetchAllGenreRecords();

        return new GenreGetAllResponseModel(219,"Genres successfully retrieved", genresList);
    }

    public static List<GenreModel> fetchAllGenreRecords() throws Exception
    {

        String query = "Select id, name from genres;";
        PreparedStatement ps = MovieService.getCon().prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        List<GenreModel> genresList = new ArrayList<GenreModel>();
        while(rs.next())
        {
            GenreModel genre = new GenreModel(rs.getInt("id"), rs.getString("name"));
            genresList.add(genre);
        }
        return genresList;
    }

    public static GenreAddResponseModel addGenre(GenreAddRequestModel requestModel, String email) throws Exception
    {
        GenreAddResponseModel responseModel = null;
        VerifyPrivilegeResponseModel privilegeResponseModel = IDMPrivilegeService.isUserAllowedToMakeRequest(email, 3);
        boolean hasPrivilege = privilegeResponseModel != null
                && privilegeResponseModel.getResultCode() == 140;

        if (!hasPrivilege) {
            return new GenreAddResponseModel(141, "User has insufficient privilege.");
        }

        responseModel = insertGenre(requestModel);
        return responseModel;

    }

    public static GenreAddResponseModel insertGenre(GenreAddRequestModel requestModel) throws Exception
    {
        String query = "Select name from genres where name = ?;";

        PreparedStatement ps = MovieService.getCon().prepareStatement(query);
        ps.setString(1, requestModel.getName());
        ResultSet rs = ps.executeQuery();

        if (rs.next())
        {
            ServiceLogger.LOGGER.info("Genre already exists.");
            return new GenreAddResponseModel(218, "Genre could not be added.");
        }

        String query2 = "Insert into genres(name) values(?);";
        ps = MovieService.getCon().prepareStatement(query2);
        ps.setString(1,requestModel.getName());
        ps.execute();

        ServiceLogger.LOGGER.info("Genre inserted.");
        return new GenreAddResponseModel(217,"Genre successfully added.");
    }

    public static GenreGetByMovieIDResponseModel getByMovieId(String movieId, String email) throws Exception
    {
        VerifyPrivilegeResponseModel privilegeResponseModel = IDMPrivilegeService.isUserAllowedToMakeRequest(email, 3);
        boolean hasPrivilege = privilegeResponseModel != null
                && privilegeResponseModel.getResultCode() == 140;

        if (!hasPrivilege) {
            return new GenreGetByMovieIDResponseModel(141, "User has insufficient privilege.");
        }

        String query = "select id from movies where id = ?;";
        PreparedStatement ps = MovieService.getCon().prepareStatement(query);
        ps.setString(1, movieId);
        ResultSet rs = ps.executeQuery();

        if(!rs.next())
        {
            return new GenreGetByMovieIDResponseModel(211,"No movies found with search parameters.");
        }

        List<GenreModel> genresList = fetchGenreIdRecords(movieId);
        return new GenreGetByMovieIDResponseModel(219,"Genres successfully retrieved.", genresList);
    }

    public static List<GenreModel> fetchGenreIdRecords(String movieId) throws Exception
    {
        List<GenreModel> genresList = new ArrayList<GenreModel>();
        String query = "select g.id as id, g.name as name from genres g, genres_in_movies gm " +
                "where gm.genreId = g.id and gm.movieId = ?;";
        PreparedStatement ps = MovieService.getCon().prepareStatement(query);
        ps.setString(1, movieId);
        ResultSet rs = ps.executeQuery();

        while (rs.next())
        {
            GenreModel genre = new GenreModel(rs.getInt("id"), rs.getString("name"));
            genresList.add(genre);
        }
        return genresList;
    }
}
