package online.caltuli.model;

import online.caltuli.model.exceptions.userconnection.UserConnectionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;

public class UserConnection {
    private int id;
    private String ipAddress;
    private Timestamp timestamp;
    private Integer userId;
    private Boolean isAllowed;

    private static final Logger logger = LogManager.getLogger(User.class);

    public UserConnection() {

    }

    public UserConnection(String ipAddress, Integer userId) throws UserConnectionException {
        this.setId(-1);
        this.setIpAddress(ipAddress);
        this.setUserId(userId);
        this.setTimestamp(new Timestamp(System.currentTimeMillis()));
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getIpAddress() {return ipAddress;}
    public void setIpAddress(String ipAddress) {this.ipAddress = ipAddress;}
    public Timestamp getTimestamp() {return timestamp;}
    public void setTimestamp(Timestamp timestamp) {this.timestamp = timestamp;}
    public int getUserId() {return userId;}
    public void setUserId(int userId) {this.userId = userId;}
    public Boolean getIsAllowed() {return isAllowed;}
    public void setIsAllowed(Boolean isAllowed) {this.isAllowed = isAllowed;}
}
