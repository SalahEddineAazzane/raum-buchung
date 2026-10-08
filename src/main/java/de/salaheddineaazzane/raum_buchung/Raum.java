package de.salaheddineaazzane.raum_buchung;

import jakarta.persistence.*;

@Entity
@Table(name = "raeume")
public class Raum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int kapazitaet;

    protected Raum() {}

    public Long getId() { return id; }
    public String getName() { return name; }
    public int getKapazitaet() { return kapazitaet; }
}