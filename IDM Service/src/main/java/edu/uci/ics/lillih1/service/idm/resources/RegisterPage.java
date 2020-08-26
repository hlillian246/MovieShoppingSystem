package edu.uci.ics.lillih1.service.idm.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.lillih1.service.idm.core.RegisterRecords;
import edu.uci.ics.lillih1.service.idm.logger.ServiceLogger;
import edu.uci.ics.lillih1.service.idm.models.RegisterRequestModel;
import edu.uci.ics.lillih1.service.idm.models.RegisterResponseModel;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Path("")
public class RegisterPage {
    private final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]+$";
    private final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[ !\"#$%&'()*+,-./:;<=>?@\\[\\\\\\]^_`{|}~])(?=.*[0-9])(?=.*[a-z]).+$";

    @Path("register")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response RegisterRequest(String jsonText)
    {
        ServiceLogger.LOGGER.info("Received request to register.");
        ServiceLogger.LOGGER.info("Request:\n" + jsonText);
        RegisterRequestModel requestModel = null;
        RegisterResponseModel responseModel = null;
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            requestModel = mapper.readValue(jsonText, RegisterRequestModel.class);

            if(requestModel.getPassword() == null || requestModel.getPassword().length == 0)
            {
                responseModel = new RegisterResponseModel
                        (-12,"Password has invalid length (cannot be empty/null).");
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            else if (requestModel.getEmail() == null || requestModel.getEmail().isEmpty() || requestModel.getEmail().length() > 50)
            {
                responseModel = new RegisterResponseModel
                        (-10 ,"Email address has invalid length.");
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            else if (!validInputFormat(requestModel.getEmail(), EMAIL_REGEX))
            {
                responseModel = new RegisterResponseModel
                        (-11 ,"Email address has invalid format.");
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            else if(requestModel.getPassword().length < 7 || requestModel.getPassword().length > 16)
            {
                responseModel = new RegisterResponseModel(12,"Password does not meet length requirements.");
                String json = mapper.writeValueAsString(responseModel);
                ServiceLogger.LOGGER.info("RESPONSE: " + json);
                return Response.status(Response.Status.OK).entity(responseModel).build();
            }
            else if (!validInputFormat(new String(requestModel.getPassword()), PASSWORD_REGEX))
            {
                responseModel = new RegisterResponseModel
                        (13 ,"Password does not meet character requirements.");
                return Response.status(Response.Status.OK).entity(responseModel).build();
            }
            else if (RegisterRecords.emailExist(requestModel.getEmail()))
            {
                responseModel = new RegisterResponseModel(16, "Email already in use");
                return Response.status(Response.Status.OK).entity(responseModel).build();
            }
            else
            {
                ServiceLogger.LOGGER.info("Start calling ...");
                responseModel = RegisterRecords.registerIntoDB(requestModel);
                return Response.status(Response.Status.OK).entity(responseModel).build();
            }
        }
        catch(JsonParseException e1)
        {
            responseModel = new RegisterResponseModel(-3,"JSON Parse Exception.");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();

        }
        catch(JsonMappingException e)
        {
            responseModel = new RegisterResponseModel(-2,"JSON Mapping Exception.");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        }
        catch(Exception e)
        {
            responseModel = new RegisterResponseModel(-1,"Internal Server Error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
        }
    }

    public static boolean validInputFormat(String input, String regex)
    {
//        ServiceLogger.LOGGER.info("Input: " + input + ", REGEX: " + regex);

        Pattern n = Pattern.compile(regex);
        Matcher m = n.matcher(input);
        return m.find();
    }
}
