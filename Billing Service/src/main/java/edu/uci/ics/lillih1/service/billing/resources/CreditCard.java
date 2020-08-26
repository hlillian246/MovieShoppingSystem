package edu.uci.ics.lillih1.service.billing.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.lillih1.service.billing.core.CreditCardService;
import edu.uci.ics.lillih1.service.billing.logger.ServiceLogger;
import edu.uci.ics.lillih1.service.billing.models.*;
import edu.uci.ics.lillih1.service.billing.utils.Utils;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.time.LocalDate;

@Path("creditcard")
public class CreditCard {

    private final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]+$";
    private final String CC_REGEX = "^[0-9]+$";


    @Path("insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insert(String jsonText) {
        ServiceLogger.LOGGER.info("Received credit card insert request");
        ServiceLogger.LOGGER.info("Request:\n" + jsonText);

        CreditCardInsertRequestModel requestModel = null;
        CreditCardInsertResponseModel responseModel = null;

        ObjectMapper mapper = new ObjectMapper();

        try {
            requestModel = mapper.readValue(jsonText, CreditCardInsertRequestModel.class);

            String id = requestModel.getId();
            LocalDate today = LocalDate.now();

            if (!Utils.validCCFormat(id))
            {
                responseModel = new CreditCardInsertResponseModel(321, "Credit card ID has invalid length.");
                return Response.status(Status.OK).entity(responseModel).build();
            }
            else if (!Utils.validInputFormat(id, CC_REGEX))
            {
                responseModel = new CreditCardInsertResponseModel(322, "Credit card ID has invalid value.");
                return Response.status(Status.OK).entity(responseModel).build();
            }
            else if (requestModel.getExpiration().isBefore(today))
            {
                responseModel = new CreditCardInsertResponseModel(323, "expiration has invalid value.");
                return Response.status(Status.OK).entity(responseModel).build();
            }

            responseModel = CreditCardService.insertService(requestModel);

            String text = mapper.writeValueAsString(responseModel);
            ServiceLogger.LOGGER.info("RESPONSE: " + text);

            return Response.status(Status.OK).entity(responseModel).build();
        } catch (JsonParseException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Parse Exception.");
            e.printStackTrace();
            responseModel = new CreditCardInsertResponseModel(-3, "JSON Parse Exception.");
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        } catch (JsonMappingException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Mapping Exception.");
            e.printStackTrace();
            responseModel = new CreditCardInsertResponseModel(-2, "JSON Mapping Exception.");
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        } catch (Exception e) {
            ServiceLogger.LOGGER.info("Oops! " + "Internal server error occurred");
            e.printStackTrace();
            responseModel = new CreditCardInsertResponseModel(-1, "Internal Server Error");
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
        }
    }


    @Path("update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(String jsonText) {
        ServiceLogger.LOGGER.info("Received credit card update request");
        ServiceLogger.LOGGER.info("Request:\n" + jsonText);

        CreditCardUpdateRequestModel requestModel = null;
        CreditCardUpdateResponseModel responseModel = null;

        ObjectMapper mapper = new ObjectMapper();

        try {
            requestModel = mapper.readValue(jsonText, CreditCardUpdateRequestModel.class);

            String id = requestModel.getId();
            LocalDate today = LocalDate.now();

            if (!Utils.validCCFormat(id))
            {
                responseModel = new CreditCardUpdateResponseModel(321, "Credit card ID has invalid length.");
                return Response.status(Status.OK).entity(responseModel).build();
            }
            else if (!Utils.validInputFormat(id, CC_REGEX))
            {
                responseModel = new CreditCardUpdateResponseModel(322, "Credit card ID has invalid value.");
                return Response.status(Status.OK).entity(responseModel).build();
            }
            else if (requestModel.getExpiration().isBefore(today))
            {
                responseModel = new CreditCardUpdateResponseModel(323, "expiration has invalid value.");
                return Response.status(Status.OK).entity(responseModel).build();
            }

            responseModel = CreditCardService.updateService(requestModel);

            String text = mapper.writeValueAsString(responseModel);
            ServiceLogger.LOGGER.info("RESPONSE: " + text);

            return Response.status(Status.OK).entity(responseModel).build();
        } catch (JsonParseException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Parse Exception.");
            e.printStackTrace();

            responseModel = new CreditCardUpdateResponseModel(-3, "JSON Parse Exception.");
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        } catch (JsonMappingException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Mapping Exception.");
            e.printStackTrace();

            responseModel = new CreditCardUpdateResponseModel(-2, "JSON Mapping Exception.");
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        } catch (Exception e) {
            ServiceLogger.LOGGER.info("Oops! " + "Internal server error occurred");
            e.printStackTrace();
            responseModel = new CreditCardUpdateResponseModel(-1, "Internal Server Error");
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
        }
    }


    @Path("delete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(String jsonText) {
        ServiceLogger.LOGGER.info("Received credit card delete request");
        ServiceLogger.LOGGER.info("Request:\n" + jsonText);

        CreditCardDeleteRequestModel requestModel = null;
        CreditCardDeleteResponseModel responseModel = null;

        ObjectMapper mapper = new ObjectMapper();

        try {
            requestModel = mapper.readValue(jsonText, CreditCardDeleteRequestModel.class);

            String id = requestModel.getId();

            if (!Utils.validCCFormat(id))
            {
                responseModel = new CreditCardDeleteResponseModel(321, "Credit card ID has invalid length.");
                return Response.status(Status.OK).entity(responseModel).build();
            }
            else if (!Utils.validInputFormat(id, CC_REGEX))
            {
                responseModel = new CreditCardDeleteResponseModel(322, "Credit card ID has invalid value.");
                return Response.status(Status.OK).entity(responseModel).build();
            }

            responseModel = CreditCardService.deleteService(requestModel);

            String text = mapper.writeValueAsString(responseModel);
            ServiceLogger.LOGGER.info("RESPONSE: " + text);

            return Response.status(Status.OK).entity(responseModel).build();

        } catch (JsonParseException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Parse Exception.");
            e.printStackTrace();

            responseModel = new CreditCardDeleteResponseModel(-3, "JSON Parse Exception.");
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        } catch (JsonMappingException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Mapping Exception.");
            e.printStackTrace();

            responseModel = new CreditCardDeleteResponseModel(-2, "JSON Mapping Exception.");
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        } catch (Exception e) {
            ServiceLogger.LOGGER.info("Oops! " + "Internal server error occurred");
            e.printStackTrace();
            responseModel = new CreditCardDeleteResponseModel(-1, "Internal Server Error");
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
        }
    }

    @Path("retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieve(String jsonText) {
        ServiceLogger.LOGGER.info("Received credit card retrieve request");
        ServiceLogger.LOGGER.info("Request:\n" + jsonText);

        CreditCardRetrieveRequestModel requestModel = null;
        CreditCardRetrieveResponseModel responseModel = null;

        ObjectMapper mapper = new ObjectMapper();

        try {
            requestModel = mapper.readValue(jsonText, CreditCardRetrieveRequestModel.class);

            String id = requestModel.getId();

            if (!Utils.validCCFormat(id))
            {
                responseModel = new CreditCardRetrieveResponseModel(321, "Credit card ID has invalid length.");
                return Response.status(Status.OK).entity(responseModel).build();
            }
            else if (!Utils.validInputFormat(id, CC_REGEX))
            {
                responseModel = new CreditCardRetrieveResponseModel(322, "Credit card ID has invalid value.");
                return Response.status(Status.OK).entity(responseModel).build();
            }

            responseModel = CreditCardService.retrieveService(requestModel);

            String text = mapper.writeValueAsString(responseModel);
            ServiceLogger.LOGGER.info("RESPONSE: " + text);

            return Response.status(Status.OK).entity(responseModel).build();
        } catch (JsonParseException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Parse Exception.");
            e.printStackTrace();

            responseModel = new CreditCardRetrieveResponseModel(-3, "JSON Parse Exception.");
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        } catch (JsonMappingException e) {
            ServiceLogger.LOGGER.info("RESPONSE: " + "JSON Mapping Exception.");
            e.printStackTrace();

            responseModel = new CreditCardRetrieveResponseModel(-2, "JSON Mapping Exception.");
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        } catch (Exception e) {
            ServiceLogger.LOGGER.info("Oops! " + "Internal server error occurred");
            e.printStackTrace();
            responseModel = new CreditCardRetrieveResponseModel(-1, "Internal Server Error");
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
        }
    }
}
