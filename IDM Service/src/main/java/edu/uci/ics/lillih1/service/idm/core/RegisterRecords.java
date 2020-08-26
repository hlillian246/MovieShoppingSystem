package edu.uci.ics.lillih1.service.idm.core;

import edu.uci.ics.lillih1.service.idm.IDMService;
import edu.uci.ics.lillih1.service.idm.logger.ServiceLogger;
import edu.uci.ics.lillih1.service.idm.models.RegisterRequestModel;
import edu.uci.ics.lillih1.service.idm.models.RegisterResponseModel;
import edu.uci.ics.lillih1.service.idm.models.SessionResponseModel;
import edu.uci.ics.lillih1.service.idm.security.Crypto;
import edu.uci.ics.lillih1.service.idm.security.Token;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;


public class RegisterRecords
{
    private static String format(String binS) {
        int length = 2 - binS.length();
        char[] padArray = new char[length];
        Arrays.fill(padArray, '0');
        String padString = new String(padArray);
        return padString + binS;
    }

    public static String getHashedPass(byte[] hashedPassword) {
        StringBuffer buf = new StringBuffer();
        for (byte b : hashedPassword) {
            buf.append(format(Integer.toHexString(Byte.toUnsignedInt(b))));
        }
        return buf.toString();
    }

    public static RegisterResponseModel registerIntoDB(RegisterRequestModel requestModel) throws Exception
    {
        RegisterResponseModel responseModel = null;
        try
        {
            byte salt[] = Crypto.genSalt();
            ServiceLogger.LOGGER.info("salt: " + salt);

            byte[] hashedPassword = Crypto.hashPassword(requestModel.getPassword(), salt, Crypto.ITERATIONS, Crypto.KEY_LENGTH);

            String password = getHashedPass(hashedPassword);
            String saltString = getHashedPass(salt);

            ServiceLogger.LOGGER.info("saltString: " + saltString);

            String query = "INSERT INTO users(email,plevel,salt,pword,status) " +
                    "VALUES(?,?,?,?,(select statusid from session_status where status = ?));";

            PreparedStatement ps = IDMService.getCon().prepareStatement(query);

            ps.setString(1,requestModel.getEmail());
            ps.setInt(2,5);
            ps.setString(3,saltString);
            ps.setString(4,password);
            ps.setString(5,"ACTIVE");

            ServiceLogger.LOGGER.info("Inserting user into DB");

            ps.execute();
            responseModel = new RegisterResponseModel(110, "User registered successfully");
            return responseModel;
        }
        catch (SQLException e)
        {
            ServiceLogger.LOGGER.warning("Query failed: Unable to execute query");
            e.printStackTrace();
            throw e;
        }
    }

    public static boolean emailExist(String email) throws Exception
    {
        String query = "SELECT * from users WHERE email = ?;";

        PreparedStatement ps = IDMService.getCon().prepareStatement(query);
                ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }
}
