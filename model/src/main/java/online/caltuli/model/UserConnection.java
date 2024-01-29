package online.caltuli.model;

import java.sql.Timestamp;

public class UserConnection {
    private int id;
    private String ipAddress;
    private Timestamp timestamp;
    private Integer userId;

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getIpAddress() {return ipAddress;}
    public void setIpAddress(String ipAddress) {this.ipAddress = ipAddress;}
    public Timestamp getTimestamp() {return timestamp;}
    public void setTimestamp(Timestamp timestamp) {this.timestamp = timestamp;}
    public int getUserId() {return userId;}
    public void setUserId(int userId) {this.userId = userId;}
}
