package ca.dal.cs.wanderer.models;
import javax.persistence.*;

@Entity
@Table(name = "pin_information")

// Class
public class Pin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pin_id", unique = true, nullable = false)
    private Integer pinId;
    @Column(name="user_id", nullable = true)
    private Integer userId;
    @Column(name="pin_location_name")
    private String locationName;
    @Column(name="pin_latitude")
    private Double latitude;
    @Column(name="pin_longitude")
    private Double longitude;

    public Pin() {
    }

    public Pin(Integer userId, String locationName, Double latitude, Double longitude) {
        this.userId = userId;
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setPinId(int pinId) {
        this.pinId = pinId;
    }

    public Integer getPinId() {
        return pinId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}