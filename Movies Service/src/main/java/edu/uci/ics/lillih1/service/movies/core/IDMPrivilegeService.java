package edu.uci.ics.lillih1.service.movies.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.lillih1.service.movies.MovieService;
import edu.uci.ics.lillih1.service.movies.logger.ServiceLogger;
import edu.uci.ics.lillih1.service.movies.models.VerifyPrivilegeRequestModel;
import edu.uci.ics.lillih1.service.movies.models.VerifyPrivilegeResponseModel;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class IDMPrivilegeService {
    public static VerifyPrivilegeResponseModel isUserAllowedToMakeRequest(String email, int plevel) {
        // Create a new Client
        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);

        // Get the URI for the IDM
        String IDM_URI = MovieService.getMovieConfigs().getIdmConfigs().getIdmUri();
        String IDM_ENDPOINT_PATH = MovieService.getMovieConfigs().getIdmConfigs().getPrivilegePath();

        // Create a WebTarget to send a request at
        WebTarget webTarget = client.target(IDM_URI).path(IDM_ENDPOINT_PATH);

        ServiceLogger.LOGGER.info("URI: " + webTarget.getUri());

        // Create an InvocationBuilder to create the HTTP request
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        // Set the payload
        VerifyPrivilegeRequestModel requestModel = new VerifyPrivilegeRequestModel(email, plevel);

        // Send the request and save it to a Response
        Response response = invocationBuilder.post(Entity.entity(requestModel,MediaType.APPLICATION_JSON));

        // Check that status code of the request
        if (response.getStatus() == 200) {
            ServiceLogger.LOGGER.info("Received status 200!");
            String jsonText = response.readEntity(String.class);
            ServiceLogger.LOGGER.info("jsonText: " + jsonText);

            ObjectMapper mapper = new ObjectMapper();
            try {
                VerifyPrivilegeResponseModel responseModel = mapper.readValue(jsonText, VerifyPrivilegeResponseModel.class);

                return responseModel;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        else
        {
            ServiceLogger.LOGGER.info("Received status " + response.getStatus());
            String jsonText = response.readEntity(String.class);
            ServiceLogger.LOGGER.warning("jsonText: " + jsonText);
        }

        return null;
    }
}
