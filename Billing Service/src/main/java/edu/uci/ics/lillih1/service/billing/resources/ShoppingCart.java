package edu.uci.ics.lillih1.service.billing.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.lillih1.service.billing.core.ShoppingCartService;
import edu.uci.ics.lillih1.service.billing.logger.ServiceLogger;
import edu.uci.ics.lillih1.service.billing.models.*;
import edu.uci.ics.lillih1.service.billing.utils.Utils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("cart")
public class ShoppingCart {

    private final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]+$";

    @Path("insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insert(String jsonText) {
        ServiceLogger.LOGGER.info("Received cart insert request");
        ServiceLogger.LOGGER.info("Request:\n" + jsonText);

        CartInsertRequestModel requestModel = null;
        CartInsertResponseModel responseModel = null;

        ObjectMapper mapper = new ObjectMapper();

        try {
            requestModel = mapper.readValue(jsonText, CartInsertRequestModel.class);

            String email = requestModel.getEmail();
            String movieId = requestModel.getMovieId();
            Integer quantity = requestModel.getQuantity();

            if (!Utils.validInputFormat(email, EMAIL_REGEX))
            {
                responseModel = new CartInsertResponseModel(-11, "Email address has invalid format.");
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            else if (email.length() > 50)
            {
                responseModel = new CartInsertResponseModel(-10, "Email address has invalid length.");
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            else if (quantity <= 0)
            {
                responseModel = new CartInsertResponseModel(33, "Quantity has invalid value.");
                return Response.status(Status.OK).entity(responseModel).build();
            }

            responseModel = ShoppingCartService.insertService(requestModel);

            String text = mapper.writeValueAsString(responseModel);
            ServiceLogger.LOGGER.info("RESPONSE: " + text);

            return Response.status(Response.Status.OK).entity(responseModel).build();
        } catch (JsonParseException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Parse Exception.");
            e.printStackTrace();

            responseModel = new CartInsertResponseModel(-3, "JSON Parse Exception.");
            return Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST).entity(responseModel).build();
        } catch (JsonMappingException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Mapping Exception.");
            e.printStackTrace();

            responseModel = new CartInsertResponseModel(-2, "JSON Mapping Exception.");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        } catch (Exception e) {
            ServiceLogger.LOGGER.info("Oops! " + "Internal server error occurred");
            e.printStackTrace();
            responseModel = new CartInsertResponseModel(-1, "Internal Server Error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
        }
    }

    @Path("update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(String jsonText) {
        ServiceLogger.LOGGER.info("Received cart update request");
        ServiceLogger.LOGGER.info("Request:\n" + jsonText);

        CartUpdateRequestModel requestModel = null;
        CartUpdateResponseModel responseModel = null;

        ObjectMapper mapper = new ObjectMapper();

        try {
            requestModel = mapper.readValue(jsonText, CartUpdateRequestModel.class);

            String email = requestModel.getEmail();
            String movieId = requestModel.getMovieId();
            Integer quantity = requestModel.getQuantity();

            if (!Utils.validInputFormat(email, EMAIL_REGEX))
            {
                responseModel = new CartUpdateResponseModel(-11, "Email address has invalid format.");
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            else if (email.length() > 50)
            {
                responseModel = new CartUpdateResponseModel(-10, "Email address has invalid length.");
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            else if (quantity <= 0)
            {
                responseModel = new CartUpdateResponseModel(33, "Quantity has invalid value.");
                return Response.status(Status.OK).entity(responseModel).build();
            }

            responseModel = ShoppingCartService.updateService(requestModel);

            String text = mapper.writeValueAsString(responseModel);
            ServiceLogger.LOGGER.info("RESPONSE: " + text);

            return Response.status(Response.Status.OK).entity(responseModel).build();
        } catch (JsonParseException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Parse Exception.");
            e.printStackTrace();

            responseModel = new CartUpdateResponseModel(-3, "JSON Parse Exception.");
            return Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST).entity(responseModel).build();
        } catch (JsonMappingException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Mapping Exception.");
            e.printStackTrace();

            responseModel = new CartUpdateResponseModel(-2, "JSON Mapping Exception.");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        } catch (Exception e) {
            ServiceLogger.LOGGER.info("Oops! " + "Internal server error occurred");
            e.printStackTrace();
            responseModel = new CartUpdateResponseModel(-1, "Internal Server Error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
        }
    }


    @Path("delete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(String jsonText) {
        ServiceLogger.LOGGER.info("Received cart delete request");
        ServiceLogger.LOGGER.info("Request:\n" + jsonText);

        CartDeleteRequestModel requestModel = null;
        CartDeleteResponseModel responseModel = null;

        ObjectMapper mapper = new ObjectMapper();

        try {
            requestModel = mapper.readValue(jsonText, CartDeleteRequestModel.class);

            String email = requestModel.getEmail();
            String movieId = requestModel.getMovieId();

            if (!Utils.validInputFormat(email, EMAIL_REGEX))
            {
                responseModel = new CartDeleteResponseModel(-11, "Email address has invalid format.");
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            else if (email.length() > 50)
            {
                responseModel = new CartDeleteResponseModel(-10, "Email address has invalid length.");
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }

            responseModel = ShoppingCartService.deleteService(requestModel);

            String text = mapper.writeValueAsString(responseModel);
            ServiceLogger.LOGGER.info("RESPONSE: " + text);

            return Response.status(Response.Status.OK).entity(responseModel).build();
        } catch (JsonParseException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Parse Exception.");
            e.printStackTrace();

            responseModel = new CartDeleteResponseModel(-3, "JSON Parse Exception.");
            return Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST).entity(responseModel).build();
        } catch (JsonMappingException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Mapping Exception.");
            e.printStackTrace();

            responseModel = new CartDeleteResponseModel(-2, "JSON Mapping Exception.");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        } catch (Exception e) {
            ServiceLogger.LOGGER.info("Oops! " + "Internal server error occurred");
            e.printStackTrace();
            responseModel = new CartDeleteResponseModel(-1, "Internal Server Error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
        }
    }


    @Path("retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieve(String jsonText) {
        ServiceLogger.LOGGER.info("Received cart retrieve request");
        ServiceLogger.LOGGER.info("Request:\n" + jsonText);

        CartRetrieveRequestModel requestModel = null;
        CartRetrieveResponseModel responseModel = null;

        ObjectMapper mapper = new ObjectMapper();

        try {
            requestModel = mapper.readValue(jsonText, CartRetrieveRequestModel.class);

            String email = requestModel.getEmail();

            if (!Utils.validInputFormat(email, EMAIL_REGEX))
            {
                responseModel = new CartRetrieveResponseModel(-11, "Email address has invalid format.");
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            else if (email.length() > 50)
            {
                responseModel = new CartRetrieveResponseModel(-10, "Email address has invalid length.");
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }

            responseModel = ShoppingCartService.retrieveService(requestModel);

            String text = mapper.writeValueAsString(responseModel);
            ServiceLogger.LOGGER.info("RESPONSE: " + text);

            return Response.status(Response.Status.OK).entity(responseModel).build();
        } catch (JsonParseException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Parse Exception.");
            e.printStackTrace();

            responseModel = new CartRetrieveResponseModel(-3, "JSON Parse Exception.");
            return Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST).entity(responseModel).build();
        } catch (JsonMappingException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Mapping Exception.");
            e.printStackTrace();

            responseModel = new CartRetrieveResponseModel(-2, "JSON Mapping Exception.");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        } catch (Exception e) {
            ServiceLogger.LOGGER.info("Oops! " + "Internal server error occurred");
            e.printStackTrace();
            responseModel = new CartRetrieveResponseModel(-1, "Internal Server Error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
        }
    }

    @Path("clear")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response clear(String jsonText) {
        ServiceLogger.LOGGER.info("Received cart clear request");
        ServiceLogger.LOGGER.info("Request:\n" + jsonText);

        CartClearRequestModel requestModel = null;
        CartClearResponseModel responseModel = null;

        ObjectMapper mapper = new ObjectMapper();

        try {
            requestModel = mapper.readValue(jsonText, CartClearRequestModel.class);

            String email = requestModel.getEmail();

            if (!Utils.validInputFormat(email, EMAIL_REGEX))
            {
                responseModel = new CartClearResponseModel(-11, "Email address has invalid format.");
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            else if (email.length() > 50)
            {
                responseModel = new CartClearResponseModel(-10, "Email address has invalid length.");
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }

            responseModel = ShoppingCartService.clearService(requestModel);

            String text = mapper.writeValueAsString(responseModel);
            ServiceLogger.LOGGER.info("RESPONSE: " + text);

            return Response.status(Response.Status.OK).entity(responseModel).build();
        } catch (JsonParseException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Parse Exception.");
            e.printStackTrace();

            responseModel = new CartClearResponseModel(-3, "JSON Parse Exception.");
            return Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST).entity(responseModel).build();
        } catch (JsonMappingException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Mapping Exception.");
            e.printStackTrace();

            responseModel = new CartClearResponseModel(-2, "JSON Mapping Exception.");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        } catch (Exception e) {
            ServiceLogger.LOGGER.info("Oops! " + "Internal server error occurred");
            e.printStackTrace();
            responseModel = new CartClearResponseModel(-1, "Internal Server Error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
        }
    }



}
