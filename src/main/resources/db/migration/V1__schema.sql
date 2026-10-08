CREATE EXTENSION IF NOT EXISTS btree_gist;

CREATE TABLE raeume (
                        id BIGSERIAL PRIMARY KEY,
                        name TEXT NOT NULL UNIQUE,
                        kapazitaet INT NOT NULL CHECK (kapazitaet > 0)
);

CREATE TABLE buchungen (
                           id BIGSERIAL PRIMARY KEY,
                           raum_id BIGINT NOT NULL REFERENCES raeume(id),
                           gebucht_von TEXT NOT NULL,
                           start_zeit TIMESTAMPTZ NOT NULL,
                           end_zeit TIMESTAMPTZ NOT NULL,
                           CONSTRAINT zeit_gueltig CHECK (end_zeit > start_zeit),
                           CONSTRAINT keine_ueberschneidung EXCLUDE USING gist (
        raum_id WITH =,
        tstzrange(start_zeit, end_zeit) WITH &&
    )
);

CREATE INDEX idx_buchungen_raum_start ON buchungen (raum_id, start_zeit);