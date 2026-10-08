package de.salaheddineaazzane.raum_buchung;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.time.OffsetDateTime;
import java.util.List;

@RestController
public class BuchungController {

    public record BuchungAnfrage(
            @NotNull Long raumId,
            @NotBlank String gebuchtVon,
            @NotNull OffsetDateTime startZeit,
            @NotNull OffsetDateTime endZeit) {}

    private final RaumRepository raeume;
    private final BuchungRepository buchungen;

    public BuchungController(RaumRepository raeume, BuchungRepository buchungen) {
        this.raeume = raeume;
        this.buchungen = buchungen;
    }

    @GetMapping("/raeume")
    public List<Raum> alleRaeume() {
        return raeume.findAll();
    }

    @GetMapping("/buchungen")
    public List<Buchung> buchungenFuerRaum(@RequestParam Long raumId) {
        return buchungen.findByRaumIdOrderByStartZeit(raumId);
    }

    @PostMapping("/buchungen")
    public ResponseEntity<Buchung> buchen(@Valid @RequestBody BuchungAnfrage a) {
        if (!a.endZeit().isAfter(a.startZeit())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "endZeit muss nach startZeit liegen");
        }
        if (!raeume.existsById(a.raumId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Raum nicht gefunden");
        }
        try {
            Buchung b = buchungen.saveAndFlush(new Buchung(a.raumId(), a.gebuchtVon(), a.startZeit(), a.endZeit()));
            return ResponseEntity.status(HttpStatus.CREATED).body(b);
        } catch (DataIntegrityViolationException ex) {
            String ursache = String.valueOf(ex.getMostSpecificCause().getMessage());
            if (ursache.contains("keine_ueberschneidung")) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Raum ist in diesem Zeitraum bereits gebucht");
            }
            throw ex;
        }
    }

    @DeleteMapping("/buchungen/{id}")
    public ResponseEntity<Void> stornieren(@PathVariable Long id) {
        if (!buchungen.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        buchungen.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}