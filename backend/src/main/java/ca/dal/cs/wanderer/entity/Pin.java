package ca.dal.cs.wanderer.entity;
import javax.persistence.*;

@Entity
@Table(name = "pins_information")

// Class
public class Pin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pin_id", unique = true, nullable = false)
    private int pinId;
    @Column(name="location_name")
    private String locationName;
    @Column(name="latitude")
    private double latitude;
    @Column(name="longitude")
    private double longitude;

    public Pin() {
    }

    public Pin(String locationName, double latitude, double longitude) {
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getPinId() {
        return pinId;
    }

    public String getLocationName() {
        return locationName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}