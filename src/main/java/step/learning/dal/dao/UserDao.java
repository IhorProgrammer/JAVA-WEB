package step.learning.dal.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dal.dto.User;
import step.learning.services.db.DBService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

@Singleton
public class UserDao {
    private final DBService dbService;

    @Inject
    public UserDao(DBService dbService) {
        this.dbService = dbService;
    }

    public User getUserByToken ( String token ) {
        String sql = "SELECT u.*, t.* FROM Tokens as t JOIN Users as u ON t.user_id = u.user_id WHERE t.token_id = ? AND t.token_expires > CURRENT_TIMESTAMP LIMIT 1";
        try ( PreparedStatement prep = dbService.getConnection().prepareStatement( sql )) {
            prep.setString(1, token);
            ResultSet resultSet = prep.executeQuery();
            if( resultSet.next() ) {
                return User.fromResultSet( resultSet );
            }
        } catch ( SQLException ex ) {
            System.err.print("Error UserDao:getUserByToken: ");
            System.err.println(ex.getMessage());
        } catch ( Exception ex ) {
            System.err.print("Error UserDao:getUserByToken: ");
            System.err.println(ex.getMessage());
        }
        return null;
    }

    public User getUserByEmail( String email ) {
        String sql = "SELECT u.* FROM Users as u WHERE u.user_email = ?";
        try ( PreparedStatement prep = dbService.getConnection().prepareStatement( sql )) {
            prep.setString(1, email);
            ResultSet resultSet = prep.executeQuery();
            if( resultSet.next() ) {
                return User.fromResultSet( resultSet );
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

    public boolean installTable() {

        String sql = "CREATE TABLE Users (" +
                "user_id        CHAR(36)     PRIMARY KEY DEFAULT( UUID() ), " +
                "user_name      VARCHAR(64)  NOT NULL," +
                "user_email     VARCHAR(128) NOT NULL," +
                "user_avatar    VARCHAR(64)      NULL," +
                "user_salt      CHAR(32)     NOT NULL," +
                "user_dk        CHAR(32)     NOT NULL," +
                "user_created   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "user_deleted   DATETIME         NULL" +
                ") ENGINE = INNODB, DEFAULT CHARSET= utf8mb4";

        try(Statement statement = dbService.getConnection().createStatement()) {
            statement.executeUpdate( sql );
            return true;
        } catch (SQLException ex) {
            System.err.println(" Error UserDao:installTable");
            System.err.println(ex.getMessage());
            System.err.println( sql );
            return false;
        }
    }

    public boolean registerUser ( User user ) {
        if( user == null ) return false ;

        if( user.getId() == null )
            user.setId( UUID.randomUUID() );
        String sql = "INSERT INTO Users"
                + "(user_id, user_name, user_email, user_avatar, user_salt, user_dk) "
                + "VALUES(?,?,?,?,?,?)";
        try( PreparedStatement prep = dbService.getConnection().prepareStatement(sql) ) {
            prep.setString( 1, user.getId().toString() );   // у JDBC відлік від 1
            prep.setString( 2, user.getName() );
            prep.setString( 3, user.getEmail() );
            prep.setString( 4, user.getAvatar() );
            prep.setString( 5, user.getSalt() );
            prep.setString( 6, user.getDerivedKey() );
            prep.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.err.println( ex.getMessage() );
            System.out.println( sql );
            return false ;
        }
    }
}
