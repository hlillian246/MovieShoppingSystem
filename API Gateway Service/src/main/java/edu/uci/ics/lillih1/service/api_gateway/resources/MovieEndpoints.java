package edu.uci.ics.lillih1.service.api_gateway.resources;

import edu.uci.ics.lillih1.service.api_gateway.GatewayService;
import edu.uci.ics.lillih1.service.api_gateway.core.UserSessionValidation;
import edu.uci.ics.lillih1.service.api_gateway.exceptions.ModelValidationException;
import edu.uci.ics.lillih1.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.lillih1.service.api_gateway.models.idm.SessionResponseModel;
import edu.uci.ics.lillih1.service.api_gateway.models.movies.*;
import edu.uci.ics.lillih1.service.api_gateway.threadpool.ClientRequest;
import edu.uci.ics.lillih1.service.api_gateway.utilities.ModelValidator;
import edu.uci.ics.lillih1.service.api_gateway.utilities.TransactionIDGenerator;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.HashMap;
import java.util.Map;

@Path("movies")
public class MovieEndpoints {
    @Path("search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchMovieRequest(@Context HttpHeaders headers,
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
        ServiceLogger.LOGGER.info("Received request movie search.");

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        ServiceLogger.LOGGER.info("email: " + email);
        ServiceLogger.LOGGER.info("sessionID: " + sessionID);

        if (sessionID == null)
        {
            SessionResponseModel response = new SessionResponseModel(-17,
                    "SessionID not provided in request header.");
            return Response.status(Status.BAD_REQUEST).entity(response)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        ServiceLogger.LOGGER.info("sessionID: " + sessionID);

        try {
            SessionResponseModel response = UserSessionValidation.userSessionValidation(email, sessionID);
            if (response.getResultCode() != 130)
            {
                return Response.status(Status.OK).entity(response)
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Headers", "*")
                        .header("Access-Control-Expose-Headers", "*")
                        .header("Access-Control-Request-Headers", "*")
                        .build();
            }

            sessionID = response.getSessionID();
        }
        catch (Exception e)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPMovieSearch());

        String transactionID = TransactionIDGenerator.generateTransactionID();
        cr.setTransactionID(transactionID);
        cr.setEmail(email);
        cr.setSessionID(sessionID);


        ServiceLogger.LOGGER.info("sessionID: " + sessionID);

        Map<String,Object> queryParams = new HashMap<String,Object>();
        queryParams.put("title", title);
        queryParams.put("genre",genre);
        queryParams.put("year",year);
        queryParams.put("director",director);
        queryParams.put("hidden",hidden);
        queryParams.put("offset",offset);
        queryParams.put("limit",limit);
        queryParams.put("orderby",orderby);
        queryParams.put("direction",direction);

        cr.setQueryParams(queryParams);
        cr.setMethod(HttpMethod.GET);

        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Status.NO_CONTENT)
                .header("delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*")
                .header("Access-Control-Expose-Headers", "*")
                .header("Access-Control-Request-Headers", "*")
                .build();

    }

    @Path("get/{movieid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieRequest(@Context HttpHeaders headers,@PathParam("movieid") String movieid) {

        ServiceLogger.LOGGER.info("Received request movie get by id.");

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        if (sessionID == null)
        {
            SessionResponseModel response = new SessionResponseModel(-17,
                    "SessionID not provided in request header.");
            return Response.status(Status.BAD_REQUEST).entity(response)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        try {
            SessionResponseModel response = UserSessionValidation.userSessionValidation(email, sessionID);
            if (response.getResultCode() != 130)
            {
                return Response.status(Status.OK).entity(response)
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Headers", "*")
                        .header("Access-Control-Expose-Headers", "*")
                        .header("Access-Control-Request-Headers", "*")
                        .build();
            }
            sessionID = response.getSessionID();
        }
        catch (Exception e)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }


        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPMovieGet());

        String transactionID = TransactionIDGenerator.generateTransactionID();
        cr.setTransactionID(transactionID);
        cr.setEmail(email);
        cr.setSessionID(sessionID);

        Map<String, Object> pathParam = new HashMap<String,Object>();
        pathParam.put("movieid", movieid);

        cr.setPathParam(pathParam);
        cr.setMethod(HttpMethod.GET);

        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Status.NO_CONTENT)
                .header("delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*")
                .header("Access-Control-Expose-Headers", "*")
                .header("Access-Control-Request-Headers", "*")
                .build();
    }

    @Path("add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addMovieRequest(@Context HttpHeaders headers,String jsonText) {

        ServiceLogger.LOGGER.info("Received request movie add.");

        MovieAddRequestModel requestModel;
        try {
            requestModel = (MovieAddRequestModel) ModelValidator.verifyModel(jsonText, MovieAddRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, MovieAddResponseModel.class);
        }

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        if (sessionID == null)
        {
            SessionResponseModel response = new SessionResponseModel(-17,
                    "SessionID not provided in request header.");
            return Response.status(Status.BAD_REQUEST).entity(response)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        try {
            SessionResponseModel response = UserSessionValidation.userSessionValidation(email, sessionID);
            if (response.getResultCode() != 130)
            {
                return Response.status(Status.OK).entity(response)
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Headers", "*")
                        .header("Access-Control-Expose-Headers", "*")
                        .header("Access-Control-Request-Headers", "*")
                        .build();
            }
            sessionID = response.getSessionID();
        }
        catch (Exception e)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPMovieAdd());
        cr.setRequest(requestModel);

        String transactionID = TransactionIDGenerator.generateTransactionID();
        cr.setTransactionID(transactionID);
        cr.setEmail(email);
        cr.setSessionID(sessionID);

        cr.setMethod(HttpMethod.POST);
        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Status.NO_CONTENT)
                .header("delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*")
                .header("Access-Control-Expose-Headers", "*")
                .header("Access-Control-Request-Headers", "*")
                .build();
    }

    @Path("delete/{movieid}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMovieRequest(@Context HttpHeaders headers,@PathParam("movieid") String movieId) {

        ServiceLogger.LOGGER.info("Received request movie delete.");

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        if (sessionID == null)
        {
            SessionResponseModel response = new SessionResponseModel(-17,
                    "SessionID not provided in request header.");
            return Response.status(Status.BAD_REQUEST).entity(response)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        try {
            SessionResponseModel response = UserSessionValidation.userSessionValidation(email, sessionID);
            if (response.getResultCode() != 130)
            {
                return Response.status(Status.OK).entity(response)
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Headers", "*")
                        .header("Access-Control-Expose-Headers", "*")
                        .header("Access-Control-Request-Headers", "*")
                        .build();
            }
            sessionID = response.getSessionID();
        }
        catch (Exception e)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPMovieDelete());

        String transactionID = TransactionIDGenerator.generateTransactionID();
        cr.setTransactionID(transactionID);
        cr.setEmail(email);
        cr.setSessionID(sessionID);

        cr.setMethod(HttpMethod.DELETE);

        Map<String, Object> pathParam = new HashMap<String,Object>();
        pathParam.put("movieid", movieId);

        cr.setPathParam(pathParam);
        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Status.NO_CONTENT)
                .header("delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*")
                .header("Access-Control-Expose-Headers", "*")
                .header("Access-Control-Request-Headers", "*")
                .build();
    }

    @Path("genre")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGenresRequest(@Context HttpHeaders headers) {

        ServiceLogger.LOGGER.info("Received request to get genres.");

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        if (sessionID == null)
        {
            SessionResponseModel response = new SessionResponseModel(-17,
                    "SessionID not provided in request header.");
            return Response.status(Status.BAD_REQUEST).entity(response)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        try {
            SessionResponseModel response = UserSessionValidation.userSessionValidation(email, sessionID);
            if (response.getResultCode() != 130)
            {
                return Response.status(Status.OK).entity(response)
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Headers", "*")
                        .header("Access-Control-Expose-Headers", "*")
                        .header("Access-Control-Request-Headers", "*")
                        .build();
            }
            sessionID = response.getSessionID();
        }
        catch (Exception e)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPGenreGet());

        String transactionID = TransactionIDGenerator.generateTransactionID();
        cr.setTransactionID(transactionID);
        cr.setEmail(email);
        cr.setSessionID(sessionID);

        cr.setMethod(HttpMethod.GET);
        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Status.NO_CONTENT)
                .header("delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*")
                .header("Access-Control-Expose-Headers", "*")
                .header("Access-Control-Request-Headers", "*")
                .build();
    }

    @Path("genre/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addGenreRequest(@Context HttpHeaders headers,String jsonText) {

        ServiceLogger.LOGGER.info("Received request to add genre.");

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        if (sessionID == null)
        {
            SessionResponseModel response = new SessionResponseModel(-17,
                    "SessionID not provided in request header.");
            return Response.status(Status.BAD_REQUEST).entity(response)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        try {
            SessionResponseModel response = UserSessionValidation.userSessionValidation(email, sessionID);
            if (response.getResultCode() != 130)
            {
                return Response.status(Status.OK).entity(response)
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Headers", "*")
                        .header("Access-Control-Expose-Headers", "*")
                        .header("Access-Control-Request-Headers", "*")
                        .build();
            }
            sessionID = response.getSessionID();
        }
        catch (Exception e)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        GenreAddRequestModel requestModel;
        try {
            requestModel = (GenreAddRequestModel) ModelValidator.verifyModel(jsonText, GenreAddRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, GenreAddResponseModel.class);
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPGenreAdd());
        cr.setRequest(requestModel);

        String transactionID = TransactionIDGenerator.generateTransactionID();
        cr.setTransactionID(transactionID);
        cr.setEmail(email);
        cr.setSessionID(sessionID);

        cr.setMethod(HttpMethod.POST);

        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Status.NO_CONTENT)
                .header("delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*")
                .header("Access-Control-Expose-Headers", "*")
                .header("Access-Control-Request-Headers", "*")
                .build();
    }

    @Path("genre/{movieid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGenresForMovieRequest(@Context HttpHeaders headers,@PathParam("movieid") String movieId) {

        ServiceLogger.LOGGER.info("Received request to add genre to movie.");

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        if (sessionID == null)
        {
            SessionResponseModel response = new SessionResponseModel(-17,
                    "SessionID not provided in request header.");
            return Response.status(Status.BAD_REQUEST).entity(response)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        try {
            SessionResponseModel response = UserSessionValidation.userSessionValidation(email, sessionID);
            if (response.getResultCode() != 130)
            {
                return Response.status(Status.OK).entity(response)
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Headers", "*")
                        .header("Access-Control-Expose-Headers", "*")
                        .header("Access-Control-Request-Headers", "*")
                        .build();
            }
            sessionID = response.getSessionID();
        }
        catch (Exception e)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPGenreMovie());

        String transactionID = TransactionIDGenerator.generateTransactionID();
        cr.setTransactionID(transactionID);
        cr.setEmail(email);
        cr.setSessionID(sessionID);

        cr.setMethod(HttpMethod.GET);

        Map<String, Object> pathParam = new HashMap<String,Object>();
        pathParam.put("movieid", movieId);

        cr.setPathParam(pathParam);
        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Status.NO_CONTENT)
                .header("delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*")
                .header("Access-Control-Expose-Headers", "*")
                .header("Access-Control-Request-Headers", "*")
                .build();
    }

    @Path("star/search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response starSearchRequest(@Context HttpHeaders headers, @QueryParam("name") String name,
                                      @QueryParam("birthYear") Integer birthYear,
                                      @QueryParam("movieTitle") String movieTitle,
                                      @DefaultValue("10") @QueryParam("limit") int limit,
                                      @DefaultValue("0") @QueryParam("offset") int offset,
                                      @DefaultValue("NAME") @QueryParam("orderby") String orderby,
                                      @DefaultValue("ASC") @QueryParam("direction") String direction)
    {
        ServiceLogger.LOGGER.info("Received request star search.");

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        if (sessionID == null)
        {
            SessionResponseModel response = new SessionResponseModel(-17,
                    "SessionID not provided in request header.");
            return Response.status(Status.BAD_REQUEST).entity(response)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        try {
            SessionResponseModel response = UserSessionValidation.userSessionValidation(email, sessionID);
            if (response.getResultCode() != 130)
            {
                return Response.status(Status.OK).entity(response)
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Headers", "*")
                        .header("Access-Control-Expose-Headers", "*")
                        .header("Access-Control-Request-Headers", "*")
                        .build();
            }
            sessionID = response.getSessionID();
        }
        catch (Exception e)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPStarSearch());

        String transactionID = TransactionIDGenerator.generateTransactionID();
        cr.setTransactionID(transactionID);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setMethod(HttpMethod.GET);

        Map<String,Object> queryParams = new HashMap<String,Object>();
        queryParams.put("birthYear",birthYear);
        queryParams.put("movieTitle",movieTitle);
        queryParams.put("limit",limit);
        queryParams.put("offset",offset);
        queryParams.put("orderby",orderby);
        queryParams.put("direction",direction);

        cr.setQueryParams(queryParams);
        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Status.NO_CONTENT)
                .header("delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*")
                .header("Access-Control-Expose-Headers", "*")
                .header("Access-Control-Request-Headers", "*")
                .build();
    }

    @Path("star/{starid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStarRequest(@Context HttpHeaders headers, @PathParam("starid") String starid)
    {
        ServiceLogger.LOGGER.info("Received request to get star by id.");

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        if (sessionID == null)
        {
            SessionResponseModel response = new SessionResponseModel(-17,
                    "SessionID not provided in request header.");
            return Response.status(Status.BAD_REQUEST).entity(response)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        try {
            SessionResponseModel response = UserSessionValidation.userSessionValidation(email, sessionID);
            if (response.getResultCode() != 130)
            {
                return Response.status(Status.OK).entity(response)
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Headers", "*")
                        .header("Access-Control-Expose-Headers", "*")
                        .header("Access-Control-Request-Headers", "*")
                        .build();
            }
            sessionID = response.getSessionID();
        }
        catch (Exception e)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPStarGet());

        String transactionID = TransactionIDGenerator.generateTransactionID();
        cr.setTransactionID(transactionID);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setMethod(HttpMethod.GET);

        Map<String, Object> pathParam = new HashMap<String,Object>();
        pathParam.put("starid", starid);
        cr.setPathParam(pathParam);

        ServiceLogger.LOGGER.info("starid: " + starid);

        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Status.NO_CONTENT)
                .header("delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*")
                .header("Access-Control-Expose-Headers", "*")
                .header("Access-Control-Request-Headers", "*")
                .build();
    }

    @Path("star/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStarRequest(@Context HttpHeaders headers,String jsonText)
    {
        ServiceLogger.LOGGER.info("Received request to add star.");

        StarAddRequestModel requestModel;
        try {
            requestModel = (StarAddRequestModel) ModelValidator.verifyModel(jsonText, StarAddRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, StarAddResponseModel.class);
        }

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        if (sessionID == null)
        {
            SessionResponseModel response = new SessionResponseModel(-17,
                    "SessionID not provided in request header.");
            return Response.status(Status.BAD_REQUEST).entity(response)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        try {
            SessionResponseModel response = UserSessionValidation.userSessionValidation(email, sessionID);
            if (response.getResultCode() != 130)
            {
                return Response.status(Status.OK).entity(response)
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Headers", "*")
                        .header("Access-Control-Expose-Headers", "*")
                        .header("Access-Control-Request-Headers", "*")
                        .build();
            }
            sessionID = response.getSessionID();
        }
        catch (Exception e)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPStarAdd());
        cr.setRequest(requestModel);

        String transactionID = TransactionIDGenerator.generateTransactionID();
        cr.setTransactionID(transactionID);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setMethod(HttpMethod.POST);

        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Status.NO_CONTENT)
                .header("delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*")
                .header("Access-Control-Expose-Headers", "*")
                .header("Access-Control-Request-Headers", "*")
                .build();
    }

    @Path("star/starsin")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStarToMovieRequest(@Context HttpHeaders headers,String jsonText)
    {
        ServiceLogger.LOGGER.info("Received request to add star in movie.");

        StarInRequestModel requestModel;
        try {
            requestModel = (StarInRequestModel) ModelValidator.verifyModel(jsonText, StarInRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, StarInResponseModel.class);
        }

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        if (sessionID == null)
        {
            SessionResponseModel response = new SessionResponseModel(-17,
                    "SessionID not provided in request header.");
            return Response.status(Status.BAD_REQUEST).entity(response)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        try {
            SessionResponseModel response = UserSessionValidation.userSessionValidation(email, sessionID);
            if (response.getResultCode() != 130)
            {
                return Response.status(Status.OK).entity(response)
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Headers", "*")
                        .header("Access-Control-Expose-Headers", "*")
                        .header("Access-Control-Request-Headers", "*")
                        .build();
            }
            sessionID = response.getSessionID();
        }
        catch (Exception e)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPStarIn());
        cr.setRequest(requestModel);

        String transactionID = TransactionIDGenerator.generateTransactionID();
        cr.setTransactionID(transactionID);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setMethod(HttpMethod.POST);


        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Status.NO_CONTENT)
                .header("delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*")
                .header("Access-Control-Expose-Headers", "*")
                .header("Access-Control-Request-Headers", "*")
                .build();
    }

    @Path("rating")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRatingRequest(@Context HttpHeaders headers,String jsonText) {

        ServiceLogger.LOGGER.info("Received request to rate movie.");

        RatingRequestModel requestModel;
        try {
            requestModel = (RatingRequestModel) ModelValidator.verifyModel(jsonText, RatingRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, RatingResponseModel.class);
        }

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");

        if (sessionID == null)
        {
            SessionResponseModel response = new SessionResponseModel(-17,
                    "SessionID not provided in request header.");
            return Response.status(Status.BAD_REQUEST).entity(response)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        try {
            SessionResponseModel response = UserSessionValidation.userSessionValidation(email, sessionID);
            if (response.getResultCode() != 130)
            {
                return Response.status(Status.OK).entity(response)
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Headers", "*")
                        .header("Access-Control-Expose-Headers", "*")
                        .header("Access-Control-Request-Headers", "*")
                        .build();
            }
            sessionID = response.getSessionID();
        }
        catch (Exception e)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "*")
                    .header("Access-Control-Expose-Headers", "*")
                    .header("Access-Control-Request-Headers", "*")
                    .build();
        }

        ClientRequest cr = new ClientRequest();
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPRating());
        cr.setRequest(requestModel);

        String transactionID = TransactionIDGenerator.generateTransactionID();
        cr.setTransactionID(transactionID);
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setMethod(HttpMethod.POST);

        GatewayService.getThreadPool().getQueue().enqueue(cr);

        return Response.status(Status.NO_CONTENT)
                .header("delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("sessionID", sessionID)
                .header("transactionID", transactionID)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*")
                .header("Access-Control-Expose-Headers", "*")
                .header("Access-Control-Request-Headers", "*")
                .build();
    }
}
