package ca.dal.cs.wanderer.repositories;

import ca.dal.cs.wanderer.models.Pin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PinRepository extends JpaRepository<Pin, Integer> {
    @Query(value = "SELECT pin_id, user_id, pin_latitude, pin_longitude, pin_location_name, ( 6371 * acos( cos( radians(:centerLat) ) * cos( radians( pin_latitude ) ) \n" +
            "* cos( radians( pin_longitude ) - radians(:centerLng) ) + sin( radians(:centerLat) ) * sin(radians(pin_latitude)) ) ) AS distance \n" +
            "FROM pin_information\n" +
            "HAVING distance < :radius \n" +
            "ORDER BY distance ", nativeQuery = true)
    public List<Pin> getPinsByRadius(@Param("radius") double radius, @Param("centerLat") double centerLat, @Param("centerLng") double centerLng);
}