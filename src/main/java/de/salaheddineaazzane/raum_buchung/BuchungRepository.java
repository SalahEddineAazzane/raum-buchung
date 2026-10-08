package de.salaheddineaazzane.raum_buchung;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BuchungRepository extends JpaRepository<Buchung, Long> {
    List<Buchung> findByRaumIdOrderByStartZeit(Long raumId);
}