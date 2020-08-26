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
import java.util.Calendar;

@Path("star")
public class StarPage {

    private final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]+$";

    @Path("search")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response SearchRequest(@Context HttpHeaders headers,
                                  @QueryParam("name") String name,
                                  @QueryParam("birthYear") Integer birthYear,
                                  @QueryParam("movieTitle") String movieTitle,
                                  @DefaultValue("10") @QueryParam("limit") int limit,
                                  @DefaultValue("0") @QueryParam("offset") int offset,
                                  @DefaultValue("NAME") @QueryParam("orderby") String orderby,
                                  @DefaultValue("ASC") @QueryParam("direction") String direction)
    {
        ServiceLogger.LOGGER.info("Received request for search movies.");
        ServiceLogger.LOGGER.info("   name: " + name);
        ServiceLogger.LOGGER.info("   birthYear: " + birthYear);
        ServiceLogger.LOGGER.info("   movieTitle: " + movieTitle);
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

        if (!orderby.equalsIgnoreCase("NAME") &&
                !orderby.equalsIgnoreCase("BIRTHYEAR")) {
            orderby = "name";
        }

        if (!direction.equalsIgnoreCase("ASC") &&
                !direction.equalsIgnoreCase("DESC"))
        {
            direction = "asc";
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
        StarSearchResponseModel responseModel = null;

        try {
            StarSearchRequestModel requestModel = new StarSearchRequestModel(name, birthYear, movieTitle,
                    limit, offset, orderby, direction);

            responseModel = StarService.searchStars(requestModel, email);

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

            responseModel = new StarSearchResponseModel(-1,"Internal Server Error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }
        catch(JsonMappingException e)
        {
            e.printStackTrace();
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Mapping Exception.");

            responseModel = new StarSearchResponseModel(-1,"Internal Server Error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            ServiceLogger.LOGGER.info("RESPONSE: " + "Internal Server Error");

            responseModel = new StarSearchResponseModel(-1,"Internal Server Error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }
    }

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response starID(@Context HttpHeaders headers, @PathParam("id") String id)
    {
        ServiceLogger.LOGGER.info("Received request for star ID.");
        ServiceLogger.LOGGER.info("   id: " + id);

        // Get the email and sessionID from the HTTP header
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        ServiceLogger.LOGGER.info("   email: " +  email);
        ServiceLogger.LOGGER.info("   sessionID: " + sessionID);
        ServiceLogger.LOGGER.info("   transactionID: " + transactionID);

        StarIDResponseModel responseModel = null;

        try
        {
            responseModel = StarService.searchStarID(email, id);
            return Response.status(Response.Status.OK).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }
        catch(JsonParseException e)
        {
            e.printStackTrace();
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Parse Exception.");

            responseModel = new StarIDResponseModel(-1,"Internal Server Error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }
        catch(JsonMappingException e)
        {
            e.printStackTrace();
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Mapping Exception.");

            responseModel = new StarIDResponseModel(-1,"Internal Server Error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            ServiceLogger.LOGGER.info("RESPONSE: " + "Internal Server Error");

            responseModel = new StarIDResponseModel(-1,"Internal Server Error");
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
        ServiceLogger.LOGGER.info("Received request to add star.");
        ServiceLogger.LOGGER.info("Request:\n" + jsonText);

        // Get the email and sessionID from the HTTP header
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        ServiceLogger.LOGGER.info("   email: " +  email);
        ServiceLogger.LOGGER.info("   sessionID: " + sessionID);
        ServiceLogger.LOGGER.info("   transactionID: " + transactionID);

        ObjectMapper mapper = new ObjectMapper();

        StarAddRequestModel requestModel = null;
        StarAddResponseModel responseModel = null;

        try
        {
            requestModel = mapper.readValue(jsonText, StarAddRequestModel.class);
            if (requestModel.getBirthYear() != null)
            {
                int year = Calendar.getInstance().get(Calendar.YEAR);

                if (requestModel.getBirthYear() > year)
                {
                    requestModel.setBirthYear(null);
                }
            }

            responseModel = StarService.addStar(email, requestModel);

            String text = mapper.writeValueAsString(responseModel);
            ServiceLogger.LOGGER.info("RESPONSE: " + text);

            return Response.status(Response.Status.OK).entity(responseModel).build();

        } catch (JsonParseException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Parse Exception.");

            e.printStackTrace();
            responseModel = new StarAddResponseModel(-3, "JSON Parse Exception.");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        } catch (JsonMappingException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Mapping Exception.");

            e.printStackTrace();
            responseModel = new StarAddResponseModel(-2, "JSON Mapping Exception.");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            responseModel = new StarAddResponseModel(-1, "Internal Server Error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }
    }

    @Path("starsin")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response StarsinRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to add a star to a movie.");
        ServiceLogger.LOGGER.info("Request:\n" + jsonText);

        // Get the email and sessionID from the HTTP header
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        ServiceLogger.LOGGER.info("   email: " +  email);
        ServiceLogger.LOGGER.info("   sessionID: " + sessionID);
        ServiceLogger.LOGGER.info("   transactionID: " + transactionID);

        ObjectMapper mapper = new ObjectMapper();

        StarsinRequestModel requestModel = null;
        StarsinResponseModel responseModel = null;

        try
        {
            requestModel = mapper.readValue(jsonText, StarsinRequestModel.class);
            responseModel = StarService.starsinService(email, requestModel);

            String text = mapper.writeValueAsString(responseModel);
            ServiceLogger.LOGGER.info("RESPONSE: " + text);

            return Response.status(Response.Status.OK).entity(responseModel).build();
        } catch (JsonParseException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Parse Exception.");

            e.printStackTrace();
            responseModel = new StarsinResponseModel(-3, "JSON Parse Exception.");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        } catch (JsonMappingException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Mapping Exception.");

            e.printStackTrace();
            responseModel = new StarsinResponseModel(-2, "JSON Mapping Exception.");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            responseModel = new StarsinResponseModel(-1, "Internal Server Error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel)
                    .header("sessionID", sessionID)
                    .header("transactionID", transactionID)
                    .build();
        }
    }


}
