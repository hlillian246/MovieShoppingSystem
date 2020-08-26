package edu.uci.ics.lillih1.service.idm.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.lillih1.service.idm.core.LoginRecords;
import edu.uci.ics.lillih1.service.idm.core.RegisterRecords;
import edu.uci.ics.lillih1.service.idm.logger.ServiceLogger;
import edu.uci.ics.lillih1.service.idm.models.LoginRequestModel;
import edu.uci.ics.lillih1.service.idm.models.LoginResponseModel;
import edu.uci.ics.lillih1.service.idm.models.RegisterResponseModel;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("")
public class LoginPage {
    private final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]+$";

    @Path("login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response LoginRequest(String jsonText)
    {
        ObjectMapper mapper = new ObjectMapper();
        LoginRequestModel requestModel = null;
        LoginResponseModel responseModel = null;

        try
        {
            requestModel = mapper.readValue(jsonText, LoginRequestModel.class);

            if(requestModel.getPassword() == null || requestModel.getPassword().length == 0 ||
                    requestModel.getPassword().length < 7 || requestModel.getPassword().length > 16)
            {
                responseModel = new LoginResponseModel(-12,"Password has invalid length.");
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            else if (!RegisterPage.validInputFormat(requestModel.getEmail(), EMAIL_REGEX))
            {
                responseModel = new LoginResponseModel
                        (-11 ,"Email address has invalid format.");
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            else if (requestModel.getEmail() == null || requestModel.getEmail().isEmpty() || requestModel.getEmail().length() > 50)
            {
                responseModel = new LoginResponseModel
                        (-10 ,"Email address has invalid length.");
                return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
            }
            else if (!RegisterRecords.emailExist(requestModel.getEmail()))
            {
                responseModel = new LoginResponseModel(14, "User not found.");
                return Response.status(Response.Status.OK).entity(responseModel).build();
            }
            else
            {
                responseModel = LoginRecords.login(requestModel);
                return Response.status(Response.Status.OK).entity(responseModel).build();
            }
        }
        catch(JsonParseException e)
        {
            responseModel = new LoginResponseModel(-3,"JSON Parse Exception.");
            return Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST).entity(responseModel).build();
        }
        catch(JsonMappingException e)
        {
            responseModel = new LoginResponseModel(-2,"JSON Mapping Exception.");
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        }
        catch(Exception e)
        {
            responseModel = new LoginResponseModel(-1,"Internal Server Error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel).build();
        }
    }
}
