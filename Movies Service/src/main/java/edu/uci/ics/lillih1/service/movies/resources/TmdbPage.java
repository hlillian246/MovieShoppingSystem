package edu.uci.ics.lillih1.service.movies.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.lillih1.service.movies.core.PullMovieDetails;
import edu.uci.ics.lillih1.service.movies.core.TmdbService;
import edu.uci.ics.lillih1.service.movies.logger.ServiceLogger;
import edu.uci.ics.lillih1.service.movies.models.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.*;

@Path("tmdb")
public class TmdbPage {

    String MOVIE_ID_FILE = "movie_id_file.txt";

    @Path("readidlist")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response readIdList(@Context HttpHeaders headers, String text) {

        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        ServiceLogger.LOGGER.info("   sessionID: " + sessionID);
        ServiceLogger.LOGGER.info("   transactionID: " + transactionID);
        ServiceLogger.LOGGER.info("Text size: " + text.length());

        try {
            TmdbReadIdListResponseModel response = TmdbService.readIdLidt(text);
            return Response.status(Status.OK).entity(response).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }
    }

    @Path("getallmovies")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response getAllMovies(@Context HttpHeaders headers, String jsonText)
    {
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        ServiceLogger.LOGGER.info("   sessionID: " + sessionID);
        ServiceLogger.LOGGER.info("   transactionID: " + transactionID);

        ObjectMapper mapper = new ObjectMapper();

        TmdbGetAllRequestModel requestModel = null;
        TmdbGetAllResponseModel responseModel = null;

        try {
            requestModel = mapper.readValue(jsonText, TmdbGetAllRequestModel.class);

            PullMovieDetails work = new PullMovieDetails(requestModel.getStart(), requestModel.getLimit(), requestModel.getIndex());
            work.start();

//            TmdbGetAllResponseModel response = TmdbService.getMovies(requestModel);

            return Response.status(Status.OK).entity("Received").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }
    }

    @Path("getpullresult")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPullResult(@Context HttpHeaders headers)
    {
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        ServiceLogger.LOGGER.info("   sessionID: " + sessionID);
        ServiceLogger.LOGGER.info("   transactionID: " + transactionID);

        try {
            TmdbGetAllResponseModel response = TmdbService.pullResult();
            return Response.status(Status.OK).entity(response).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }
    }


    @Path("insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertMovies(@Context HttpHeaders headers, String jsonText)
    {
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        ServiceLogger.LOGGER.info("   sessionID: " + sessionID);
        ServiceLogger.LOGGER.info("   transactionID: " + transactionID);

        ObjectMapper mapper = new ObjectMapper();

        TmdbInsertRequestModel requestModel = null;
        TmdbInsertResponseModel responseModel = null;

        try {
            requestModel = mapper.readValue(jsonText, TmdbInsertRequestModel.class);

            TmdbInsertResponseModel response = TmdbService.insert(requestModel);

            return Response.status(Status.OK).entity(response).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }
    }

}
