package edu.uci.ics.lillih1.service.idm.core;

import edu.uci.ics.lillih1.service.idm.IDMService;
import edu.uci.ics.lillih1.service.idm.logger.ServiceLogger;
import edu.uci.ics.lillih1.service.idm.models.PrivilegeRequestModel;
import edu.uci.ics.lillih1.service.idm.models.PrivilegeResponseModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PrivilegeRecords {

    public static PrivilegeResponseModel verifyPrivilege(PrivilegeRequestModel requestModel) throws Exception
    {
        PrivilegeResponseModel responseModel = null;
        try
        {
//            String sessionId = SessionRecords.getActiveSession(requestModel.getEmail());
//            if (sessionId != null)
//            {
//                SessionRecords.updateSession(requestModel.getEmail(), sessionId);
//            }
//
            String query = "SELECT email,plevel  FROM users WHERE email LIKE ?;";
            PreparedStatement ps = IDMService.getCon().prepareStatement(query);
            ps.setString(1,requestModel.getEmail());
//            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
//            ServiceLogger.LOGGER.info("Query succeeded.");

            rs.next();

            if (rs.getInt("plevel") <= requestModel.getPlevel())
            {
                responseModel = new PrivilegeResponseModel(140,"User has sufficient privilege level.");
                return responseModel;
            }
            else
            {
                responseModel = new PrivilegeResponseModel(141,"User has insufficient privilege level.");
                return  responseModel;
            }
        }
        catch(SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: Unable to retrieve string records.");
            e.printStackTrace();
            throw e;
        }

    }
}
