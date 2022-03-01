package ca.dal.cs.wanderer.repositories;

import ca.dal.cs.wanderer.entity.Pin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PinRepository extends JpaRepository<Pin, Integer> {
    @Query(value = "SELECT pin_id, latitude, longitude, location_name, ( 6371 * acos( cos( radians(:centerLat) ) * cos( radians( latitude ) ) \n" +
            "* cos( radians( longitude ) - radians(:centerLng) ) + sin( radians(:centerLat) ) * sin(radians(latitude)) ) ) AS distance \n" +
            "FROM pins_information\n" +
            "HAVING distance < :radius \n" +
            "ORDER BY distance ", nativeQuery = true)
    public List<Pin> getPinsByRadius(@Param("radius") double radius, @Param("centerLat") double centerLat, @Param("centerLng") double centerLng);
}