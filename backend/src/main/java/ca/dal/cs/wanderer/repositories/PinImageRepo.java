package ca.dal.cs.wanderer.repositories;

import ca.dal.cs.wanderer.models.PinImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PinImageRepo extends JpaRepository<Integer, PinImages> {
}
