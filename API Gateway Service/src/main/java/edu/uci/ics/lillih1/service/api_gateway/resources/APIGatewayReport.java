package edu.uci.ics.lillih1.service.api_gateway.resources;

import edu.uci.ics.lillih1.service.api_gateway.GatewayService;
import edu.uci.ics.lillih1.service.api_gateway.core.ClientReport;
import edu.uci.ics.lillih1.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.lillih1.service.api_gateway.models.ReportResponseModel;
import edu.uci.ics.lillih1.service.api_gateway.threadpool.ClientRequest;

import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("")
public class APIGatewayReport {
    @Path("report")
    @OPTIONS
    @Produces(MediaType.APPLICATION_JSON)

    public Response report1(@Context HttpHeaders headers)
    {
        return Response.status(Response.Status.OK)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*")
                .header("Access-Control-Expose-Headers", "transactionID, delay")
                .header("Access-Control-Request-Headers", "*")
                .build();
    }

    @Path("report")
    @GET
    @Produces(MediaType.APPLICATION_JSON)

    public Response report(@Context HttpHeaders headers)
    {
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");
        String response = "";

        ServiceLogger.LOGGER.info("Received report request for transactionId " + transactionID);

        ReportResponseModel responseModel = null;

        try
        {
            response = ClientReport.fetchClientReport(email,sessionID,transactionID);
            if(response == null || response.isEmpty())
            {
                ServiceLogger.LOGGER.info("No response found for transactionId " + transactionID);

                return Response.status(Response.Status.NO_CONTENT)
                        .header("delay", GatewayService.getGatewayConfigs().getRequestDelay())
                        .header("message","No response given.")
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Headers", "*")
                        .header("Access-Control-Expose-Headers", "*")
                        .header("Access-Control-Request-Headers", "*")
                        .build();
            }

//            ServiceLogger.LOGGER.info("Sending response for transactionId " + transactionID);

            ServiceLogger.LOGGER.info("RESPONSE: " + response);


            return Response.status(Response.Status.OK).entity(response)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }
        catch (Exception e )
        {
            e.printStackTrace();
            responseModel = new ReportResponseModel(-1,"Internal server error.");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseModel)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }
    }

}
