package ca.dal.cs.wanderer.repositories;

import ca.dal.cs.wanderer.models.PinImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PinImageRepo extends JpaRepository<PinImage, Integer> {

    @Query(value = "delete from image where pin_id:id", nativeQuery = true)
    public void delete(@Param("id") int id);
}
