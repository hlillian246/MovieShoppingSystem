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

@Path("")
public class MoviePage {

    private final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]+$";

    @Path("search")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response SearchRequest(@Context HttpHeaders headers,
                                  @QueryParam("title") String title,
                                  @QueryParam("genre") String genre,
                                  @QueryParam("year") Integer year,
                                  @QueryParam("director") String director,
                                  @QueryParam("hidden") Boolean hidden,
                                  @DefaultValue("0") @QueryParam("offset") int offset,
                                  @DefaultValue("10") @QueryParam("limit") int limit,
                                  @DefaultValue("RATING") @QueryParam("orderby") String orderby,
                                  @DefaultValue("DESC") @QueryParam("direction") String direction)
    {


        ServiceLogger.LOGGER.info("Received request for search movies.");
        ServiceLogger.LOGGER.info("   title: " + title);
        ServiceLogger.LOGGER.info("   genre: " + genre);
        ServiceLogger.LOGGER.info("   year: " + year);
        ServiceLogger.LOGGER.info("   director: " + director);
        ServiceLogger.LOGGER.info("   hidden: " + hidden);
        ServiceLogger.LOGGER.info("   offset: " + offset);
        ServiceLogger.LOGGER.info("   limit: " + limit);
        ServiceLogger.LOGGER.info("   orderby: " + orderby);
        ServiceLogger.LOGGER.info("   direction: " + direction);

        if (limit != 10 && limit != 25 && limit != 50 && limit != 100)
        {
            limit = 10;
        }

        if (offset != 0 && ((offset % limit) != 0))
        {
            offset = 0;
        }

        if (!orderby.equalsIgnoreCase("RATING") &&
                !orderby.equalsIgnoreCase("TITLE")) {
            orderby = "rating";
        }

        if (!direction.equalsIgnoreCase("ASC") &&
                !direction.equalsIgnoreCase("DESC"))
        {
            direction = "desc";
        }

        ServiceLogger.LOGGER.info("After validation: ");
        ServiceLogger.LOGGER.info("   offset: " + offset);
        ServiceLogger.LOGGER.info("   limit: " + limit);
        ServiceLogger.LOGGER.info("   orderby: " + orderby);
        ServiceLogger.LOGGER.info("   direction: " + direction);


        // Get the email and sessionID from the HTTP header
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        ServiceLogger.LOGGER.info("   email: " +  email);
        ServiceLogger.LOGGER.info("   sessionID: " + sessionID);
        ServiceLogger.LOGGER.info("   transactionID: " + transactionID);

        ObjectMapper mapper = new ObjectMapper();
        MovieSearchResponseModel responseModel = null;

        try {
            MovieSearchRequestModel requestModel = new MovieSearchRequestModel(title, genre, year, director, hidden,
                    limit, offset, orderby, direction);

            responseModel = MovieSearchService.searchMovies(requestModel, email);

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

            responseModel = new MovieSearchResponseModel(-1,"Internal Server Error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }
        catch(JsonMappingException e)
        {
            e.printStackTrace();
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Mapping Exception.");

            responseModel = new MovieSearchResponseModel(-1,"Internal Server Error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            ServiceLogger.LOGGER.info("RESPONSE: " + "Internal Server Error");

            responseModel = new MovieSearchResponseModel(-1,"Internal Server Error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }
    }

    @Path("get/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response movieID(@Context HttpHeaders headers, @PathParam("id") String id)
    {
        ServiceLogger.LOGGER.info("Received request for Movies ID.");
        ServiceLogger.LOGGER.info("   id: " + id);

        // Get the email and sessionID from the HTTP header
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        ServiceLogger.LOGGER.info("   email: " +  email);
        ServiceLogger.LOGGER.info("   sessionID: " + sessionID);
        ServiceLogger.LOGGER.info("   transactionID: " + transactionID);

        MovieIDResponseModel responseModel = null;

        try
        {
            responseModel = MovieIDService.searchMovieIDs(email, id);
            return Response.status(Response.Status.OK).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }
        catch(JsonParseException e)
        {
            e.printStackTrace();
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Parse Exception.");

            responseModel = new MovieIDResponseModel(-1,"Internal Server Error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }
        catch(JsonMappingException e)
        {
            e.printStackTrace();
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Mapping Exception.");

            responseModel = new MovieIDResponseModel(-1,"Internal Server Error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            ServiceLogger.LOGGER.info("RESPONSE: " + "Internal Server Error");

            responseModel = new MovieIDResponseModel(-1,"Internal Server Error");
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
    public Response AddRequest(@Context HttpHeaders headers, String jsonText) {
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

        MovieAddRequestModel requestModel = null;
        MovieAddResponseModel responseModel = null;

        try
        {
            requestModel = mapper.readValue(jsonText, MovieAddRequestModel.class);

            responseModel = MovieAddService.addMovie(requestModel, email);
            String text = mapper.writeValueAsString(responseModel);
            ServiceLogger.LOGGER.info("RESPONSE: " + text);

            return Response.status(Response.Status.OK).entity(responseModel).build();

        } catch (JsonParseException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Parse Exception.");

            e.printStackTrace();
            responseModel = new MovieAddResponseModel(-3, "JSON Parse Exception.");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        } catch (JsonMappingException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Mapping Exception.");

            e.printStackTrace();
            responseModel = new MovieAddResponseModel(-2, "JSON Mapping Exception.");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            responseModel = new MovieAddResponseModel(-1, "Internal Server Error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }
    }


    @Path("delete/{movieId}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@Context HttpHeaders headers, @PathParam("movieId") String movieId)
    {
        ServiceLogger.LOGGER.info("Received request to remove.");
        ServiceLogger.LOGGER.info("movieId: " + movieId);

        // Get the email and sessionID from the HTTP header
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        ServiceLogger.LOGGER.info("   email: " +  email);
        ServiceLogger.LOGGER.info("   sessionID: " + sessionID);
        ServiceLogger.LOGGER.info("   transactionID: " + transactionID);

        MovieDeleteResponseModel responseModel = null;
        ObjectMapper mapper = new ObjectMapper();

        try
        {
            responseModel = MovieDeleteService.deleteMovie(email, movieId);

            String text = mapper.writeValueAsString(responseModel);
            ServiceLogger.LOGGER.info("RESPONSE: " + text);

            return Response.status(Response.Status.OK).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }
        catch(JsonParseException e)
        {
            e.printStackTrace();
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Parse Exception.");

            responseModel = new MovieDeleteResponseModel(-1,"Internal Server Error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }
        catch(JsonMappingException e)
        {
            e.printStackTrace();
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Mapping Exception.");

            responseModel = new MovieDeleteResponseModel(-1,"Internal Server Error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            ServiceLogger.LOGGER.info("RESPONSE: " + "Internal Server Error");

            responseModel = new MovieDeleteResponseModel(-1,"Internal Server Error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }
    }


    @Path("rating")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response ratingRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request for rating movie.");
        ServiceLogger.LOGGER.info("Request:\n" + jsonText);

        // Get the email and sessionID from the HTTP header
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        ServiceLogger.LOGGER.info("   email: " +  email);
        ServiceLogger.LOGGER.info("   sessionID: " + sessionID);
        ServiceLogger.LOGGER.info("   transactionID: " + transactionID);

        ObjectMapper mapper = new ObjectMapper();

        MovieRatingRequestModel requestModel = null;
        MovieRatingResponseModel responseModel = null;

        try
        {
            requestModel = mapper.readValue(jsonText, MovieRatingRequestModel.class);
            responseModel = MovieRatingService.addRatings(requestModel);

            String text = mapper.writeValueAsString(responseModel);
            ServiceLogger.LOGGER.info("RESPONSE: " + text);

            return Response.status(Response.Status.OK).entity(responseModel).build();

        } catch (JsonParseException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Parse Exception.");

            e.printStackTrace();
            responseModel = new MovieRatingResponseModel(-3, "JSON Parse Exception.");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        } catch (JsonMappingException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Mapping Exception.");

            e.printStackTrace();
            responseModel = new MovieRatingResponseModel(-2, "JSON Mapping Exception.");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            responseModel = new MovieRatingResponseModel(-1, "Internal Server Error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }
    }

}
