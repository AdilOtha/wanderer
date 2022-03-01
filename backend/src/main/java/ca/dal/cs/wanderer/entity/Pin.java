package ca.dal.cs.wanderer.entity;
import javax.persistence.*;

@Entity
@Table(name = "pininformation")

// Class
public class Pin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PinID", unique = true, nullable = false)
    private int pinId;
    @Column(name="LocationName")
    private String locationName;
    @Column(name="Latitude")
    private double latitude;
    @Column(name="Longitude")
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