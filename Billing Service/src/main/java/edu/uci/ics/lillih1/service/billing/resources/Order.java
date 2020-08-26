package edu.uci.ics.lillih1.service.billing.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.base.rest.PayPalRESTException;
import edu.uci.ics.lillih1.service.billing.core.OrderService;
import edu.uci.ics.lillih1.service.billing.logger.ServiceLogger;
import edu.uci.ics.lillih1.service.billing.models.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.sql.ResultSet;

@Path("order")
public class Order {

    private final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]+$";

    @Path("place")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insert(String jsonText) {
        ServiceLogger.LOGGER.info("Received order place request");
        ServiceLogger.LOGGER.info("Request:\n" + jsonText);

        OrderPlaceRequestModel requestModel = null;
        OrderPlaceResponseModel responseModel = null;

        ObjectMapper mapper = new ObjectMapper();

        try {
            requestModel = mapper.readValue(jsonText, OrderPlaceRequestModel.class);
            responseModel = OrderService.orderPlaceService(requestModel);

            String text = mapper.writeValueAsString(responseModel);
            ServiceLogger.LOGGER.info("RESPONSE: " + text);

            return Response.status(Status.OK).entity(responseModel).build();
        } catch (JsonParseException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Parse Exception.");
            e.printStackTrace();

            responseModel = new OrderPlaceResponseModel(-3, "JSON Parse Exception.");
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        } catch (JsonMappingException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Mapping Exception.");
            e.printStackTrace();

            responseModel = new OrderPlaceResponseModel(-2, "JSON Mapping Exception.");
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        } catch (PayPalRESTException e){
            responseModel = new OrderPlaceResponseModel(342,"Create payment failed.");
            return Response.status(Status.OK).entity(responseModel).build();
        }

        catch (Exception e) {
            ServiceLogger.LOGGER.info("Oops! " + "Internal server error occurred");
            e.printStackTrace();
            responseModel = new OrderPlaceResponseModel(-1, "Internal Server Error");
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
        }
    }

    @Path("complete")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response complete ( @QueryParam("paymentId") String paymentId,
                               @QueryParam("token") String token,
                               @QueryParam("PayerID") String payerId)
    {
        OrderCompleteResponseModel responseModel = null;
        try{
            responseModel = OrderService.orderCompleteService(token, paymentId, payerId);

            ObjectMapper mapper = new ObjectMapper();
            String text = mapper.writeValueAsString(responseModel);
            ServiceLogger.LOGGER.info("RESPONSE: " + text);
            return Response.status(Status.OK).entity(responseModel).build();
        }
        catch(PayPalRESTException e)
        {
            responseModel = new OrderCompleteResponseModel(3422,"Payment can not be completed.");
            return Response.status(Status.OK).entity(responseModel).build();
        }
        catch (Exception e) {
            ServiceLogger.LOGGER.info("Oops! " + "Internal server error occurred");
            e.printStackTrace();
            responseModel = new OrderCompleteResponseModel(-1, "Internal Server Error");
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
        }

    }


    @Path("retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieve(@QueryParam("email") String email) {
        ServiceLogger.LOGGER.info("Received cart retrieve request");
        ServiceLogger.LOGGER.info("Email: " + email);

        OrderRetrieveResponseModel responseModel = null;

        ObjectMapper mapper = new ObjectMapper();

        try {
            responseModel = OrderService.orderRetrieveService(email);

            String text = mapper.writeValueAsString(responseModel);
            ServiceLogger.LOGGER.info("RESPONSE: " + text);

            return Response.status(Status.OK).entity(responseModel).build();
        } catch (JsonParseException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Parse Exception.");
            e.printStackTrace();

            responseModel = new OrderRetrieveResponseModel(-3, "JSON Parse Exception.");
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        } catch (JsonMappingException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Mapping Exception.");
            e.printStackTrace();

            responseModel = new OrderRetrieveResponseModel(-2, "JSON Mapping Exception.");
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        } catch (Exception e) {
            ServiceLogger.LOGGER.info("Oops! " + "Internal server error occurred");
            e.printStackTrace();
            responseModel = new OrderRetrieveResponseModel(-1, "Internal Server Error");
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
        }
    }
}
