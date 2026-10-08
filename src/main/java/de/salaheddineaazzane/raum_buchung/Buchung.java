package de.salaheddineaazzane.raum_buchung;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "buchungen")
public class Buchung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "raum_id", nullable = false)
    private Long raumId;

    @Column(name = "gebucht_von", nullable = false)
    private String gebuchtVon;

    @Column(name = "start_zeit", nullable = false)
    private OffsetDateTime startZeit;

    @Column(name = "end_zeit", nullable = false)
    private OffsetDateTime endZeit;

    protected Buchung() {}

    public Buchung(Long raumId, String gebuchtVon, OffsetDateTime startZeit, OffsetDateTime endZeit) {
        this.raumId = raumId;
        this.gebuchtVon = gebuchtVon;
        this.startZeit = startZeit;
        this.endZeit = endZeit;
    }

    public Long getId() { return id; }
    public Long getRaumId() { return raumId; }
    public String getGebuchtVon() { return gebuchtVon; }
    public OffsetDateTime getStartZeit() { return startZeit; }
    public OffsetDateTime getEndZeit() { return endZeit; }
}