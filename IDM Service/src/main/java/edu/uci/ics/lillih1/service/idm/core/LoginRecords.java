package edu.uci.ics.lillih1.service.idm.core;

import edu.uci.ics.lillih1.service.idm.IDMService;
import edu.uci.ics.lillih1.service.idm.logger.ServiceLogger;
import edu.uci.ics.lillih1.service.idm.models.LoginRequestModel;
import edu.uci.ics.lillih1.service.idm.models.LoginResponseModel;
import edu.uci.ics.lillih1.service.idm.models.RegisterRequestModel;
import edu.uci.ics.lillih1.service.idm.models.SessionRequestModel;
import edu.uci.ics.lillih1.service.idm.security.Crypto;
import edu.uci.ics.lillih1.service.idm.security.Session;
import edu.uci.ics.lillih1.service.idm.security.Token;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;


public class LoginRecords {

    public static LoginResponseModel login(LoginRequestModel requestModel) throws Exception
    {
        LoginResponseModel responseModel = null;
        try
        {
            String query = "SELECT pword, salt FROM users WHERE email = ?;";

            PreparedStatement ps = IDMService.getCon().prepareStatement(query);
            ps.setString(1,requestModel.getEmail());
            ResultSet rs = ps.executeQuery();
            rs.next();

            byte salt[] = Token.convert(rs.getString("salt"));
            byte[] hashedPassword = Crypto.hashPassword(requestModel.getPassword(), salt, Crypto.ITERATIONS, Crypto.KEY_LENGTH);
            String password = RegisterRecords.getHashedPass(hashedPassword);

            if (!rs.getString("pword").equals(password))
            {
                responseModel =  new LoginResponseModel(11,"Passwords do not match.");
            }
            else
            {
                String sessionId = SessionRecords.getActiveSession(requestModel.getEmail());
                if (sessionId != null)
                {
                    SessionRecords.setSessionStatus(sessionId, "REVOKED");
                }

                sessionId = SessionRecords.insertNewSession(requestModel.getEmail());

                ServiceLogger.LOGGER.info("Created a new session. SessionId: " + sessionId);

                responseModel = new LoginResponseModel(120, "User logged in successfully.", sessionId);
            }

            return responseModel;
        }
        catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Unable to login");
            e.printStackTrace();
            throw e;
        }
    }
}
