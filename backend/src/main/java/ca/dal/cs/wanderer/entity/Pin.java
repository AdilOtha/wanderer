package ca.dal.cs.wanderer.entity;
import javax.persistence.*;

@Entity
@Table(name = "pininformation")

// Class
public class Pin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PinID", unique = true, nullable = false)
    private Integer pinId;
    @Column(name="UserID", nullable = true)
    private Integer userId;
    @Column(name="LocationName")
    private String locationName;
    @Column(name="Latitude")
    private Double latitude;
    @Column(name="Longitude")
    private Double longitude;

    public Pin() {
    }

    public Pin(Integer userId, String locationName, Double latitude, Double longitude) {
        this.userId = userId;
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
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