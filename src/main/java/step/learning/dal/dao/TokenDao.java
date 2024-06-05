package step.learning.dal.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dal.dto.Token;
import step.learning.dal.dto.User;
import step.learning.services.db.DBService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

@Singleton
public class TokenDao {
    private final DBService dbService;

    private final int tokenTimeValid = 1000 * 60 * 60 * 24; // 1 day

    @Inject
    public TokenDao(DBService dbService) {
        this.dbService = dbService;
    }

    public String getTokenByUser ( User user ) {
        // шукаємо токен,
        String token = findTokenByUser( user );
        if( token == null ) {
            // якщо не має то генеруємо.
            token = generateToken( user );
        }
        else {
            // є - подовжуємо дію на 50% від стандартного часу
            updateTokenExpires( token, 50 );
        }

        return token;
    }

    private void updateTokenExpires( String token, int percent ) {
        String sql = "UPDATE Tokens SET token_expires = ? WHERE token_id = ?";
        try ( PreparedStatement prep = dbService.getConnection().prepareStatement( sql )) {
            prep.setTimestamp(1, new Timestamp( new java.util.Date().getTime() + Math.abs(tokenTimeValid / 100 * percent) ));
            prep.setString(2, token);
            prep.executeUpdate();
        } catch ( SQLException ex ) {
            System.err.print("Error UserDao:getUserByEmail: ");
            System.err.println(ex.getMessage());
        } catch ( Exception ex ) {
            System.err.print("Error UserDao:getUserByEmail: ");
            System.err.println(ex.getMessage());
        }
    }

    private String findTokenByUser( User user ) {
        String sql = "SELECT t.* FROM Users u JOIN Tokens t ON t.user_id = u.user_id WHERE u.user_id = ? LIMIT 1";
        try ( PreparedStatement prep = dbService.getConnection().prepareStatement( sql )) {
            prep.setString(1, user.getId().toString());
            ResultSet resultSet = prep.executeQuery();
            if( resultSet.next() ) {
                Token token = Token.fromResultSet( resultSet );
                if( token != null ) return token.getTokenId().toString();
            }
        } catch ( SQLException ex ) {
            System.err.print("Error UserDao:getUserByEmail: ");
            System.err.println(ex.getMessage());
        } catch ( Exception ex ) {
            System.err.print("Error UserDao:getUserByEmail: ");
            System.err.println(ex.getMessage());
        }
        return null;
    }

    private String generateToken( User user ) {
        String sql = "INSERT INTO Tokens( token_id, user_id, token_expires ) VALUE(?,?,?)";
        try ( PreparedStatement prep = dbService.getConnection().prepareStatement( sql )) {
            String token = UUID.randomUUID().toString();

            prep.setString(1, token);
            prep.setString(2, user.getId().toString());
            prep.setTimestamp(3, new Timestamp( new java.util.Date().getTime() + tokenTimeValid ));
            prep.executeUpdate();
            return token;
        } catch ( SQLException ex ) {
            System.err.print("Error UserDao:getUserByEmail: ");
            System.err.println(ex.getMessage());
        } catch ( Exception ex ) {
            System.err.print("Error UserDao:getUserByEmail: ");
            System.err.println(ex.getMessage());
        }
        return null;
    }
}
