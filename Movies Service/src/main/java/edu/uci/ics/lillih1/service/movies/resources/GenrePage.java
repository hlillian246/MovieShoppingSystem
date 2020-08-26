package edu.uci.ics.lillih1.service.movies.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.lillih1.service.movies.core.*;
import edu.uci.ics.lillih1.service.movies.logger.ServiceLogger;
import edu.uci.ics.lillih1.service.movies.models.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("genre")
public class GenrePage {

    private final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]+$";

    @Path("")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@Context HttpHeaders headers)
    {
        // Get the email and sessionID from the HTTP header
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        ServiceLogger.LOGGER.info("   email: " +  email);
        ServiceLogger.LOGGER.info("   sessionID: " + sessionID);
        ServiceLogger.LOGGER.info("   transactionID: " + transactionID);

        ObjectMapper mapper = new ObjectMapper();
        GenreGetAllResponseModel responseModel = null;

        try {
            responseModel = GenreService.GetAllGenres(email);

            String jsonText = mapper.writeValueAsString(responseModel);
            ServiceLogger.LOGGER.info("RESPONSE: " + jsonText);

            return Response.status(Response.Status.OK).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }
        catch(JsonParseException e)
        {
            e.printStackTrace();
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Parse Exception.");

            responseModel = new GenreGetAllResponseModel(-1,"Internal Server Error.");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }
        catch(JsonMappingException e)
        {
            e.printStackTrace();
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Mapping Exception.");

            responseModel = new GenreGetAllResponseModel(-1,"Internal Server Error.");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            ServiceLogger.LOGGER.info("RESPONSE: " + "Internal Server Error");

            responseModel = new GenreGetAllResponseModel(-1,"Internal Server Error.");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }
    }


    @Path("add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to add.");
        ServiceLogger.LOGGER.info("Request:\n" + jsonText);

        // Get the email and sessionID from the HTTP header
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        ServiceLogger.LOGGER.info("   email: " +  email);
        ServiceLogger.LOGGER.info("   sessionID: " + sessionID);
        ServiceLogger.LOGGER.info("   transactionID: " + transactionID);

        ObjectMapper mapper = new ObjectMapper();

        GenreAddRequestModel requestModel = null;
        GenreAddResponseModel responseModel = null;

        try
        {
            requestModel = mapper.readValue(jsonText, GenreAddRequestModel.class);
            responseModel = GenreService.addGenre(requestModel, email);

            String text = mapper.writeValueAsString(responseModel);
            ServiceLogger.LOGGER.info("RESPONSE: " + text);

            return Response.status(Response.Status.OK).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        } catch (JsonParseException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Parse Exception.");

            e.printStackTrace();
            responseModel = new GenreAddResponseModel(-3, "JSON Parse Exception.");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        } catch (JsonMappingException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Mapping Exception.");

            e.printStackTrace();
            responseModel = new GenreAddResponseModel(-2, "JSON Mapping Exception.");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        } catch (Exception e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "Internal Server Error.");

            e.printStackTrace();
            responseModel = new GenreAddResponseModel(-1,"Internal Server Error.");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }
    }


    @Path("{movieId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response movieID(@Context HttpHeaders headers, @PathParam("movieId") String movieId)
    {
        ServiceLogger.LOGGER.info("Received request for Movies ID.");
        ServiceLogger.LOGGER.info("   id: " + movieId);

        // Get the email and sessionID from the HTTP header
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        ServiceLogger.LOGGER.info("   email: " +  email);
        ServiceLogger.LOGGER.info("   sessionID: " + sessionID);
        ServiceLogger.LOGGER.info("   transactionID: " + transactionID);

        GenreGetByMovieIDResponseModel responseModel = null;

        try
        {
            responseModel = GenreService.getByMovieId(movieId, email);
            return Response.status(Response.Status.OK).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }
        catch(JsonParseException e)
        {
            e.printStackTrace();
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Parse Exception.");

            responseModel = new GenreGetByMovieIDResponseModel(-1,"Internal Server Error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }
        catch(JsonMappingException e)
        {
            e.printStackTrace();
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Mapping Exception.");

            responseModel = new GenreGetByMovieIDResponseModel(-1,"Internal Server Error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            ServiceLogger.LOGGER.info("RESPONSE: " + "Internal Server Error");

            responseModel = new GenreGetByMovieIDResponseModel(-1,"Internal Server Error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }
    }
}
