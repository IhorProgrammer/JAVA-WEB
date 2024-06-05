package step.learning.dal.dto;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public class Token {
    public UUID getTokenId() {
        return tokenId;
    }

    public void setTokenId(UUID tokenId) {
        this.tokenId = tokenId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Date getTokenCreated() {
        return tokenCreated;
    }

    public void setTokenCreated(Date tokenCreated) {
        this.tokenCreated = tokenCreated;
    }

    public Date getTokenExpires() {
        return tokenExpires;
    }

    public void setTokenExpires(Date tokenExpires) {
        this.tokenExpires = tokenExpires;
    }

    private UUID tokenId;
    private UUID userId;
    private Date tokenCreated;
    private Date tokenExpires;

    public static Token fromResultSet(ResultSet resultSet) {
        Token token = new Token();
        try {
            token.setTokenId( UUID.fromString( resultSet.getString("token_id" ) ) );
            token.setUserId( UUID.fromString( resultSet.getString("user_id" ) ) );

            Timestamp timestamp;
            timestamp = resultSet.getTimestamp ("token_created" ) ;
            if( timestamp != null ) {
                token.setTokenCreated( new Date( timestamp.getTime()));
            }

            timestamp = resultSet.getTimestamp("token_expires" );
            if( timestamp != null ) {
                token.setTokenExpires( new Date( timestamp.getTime() ) );
            }
            return token;
        } catch(Exception ex) {
            System.err.print( "Error User.fromResultSet: " );
            System.err.println( ex.getMessage() );
        }
        return null;
    }

}
