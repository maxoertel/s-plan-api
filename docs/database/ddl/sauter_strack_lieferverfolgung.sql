CREATE TABLE sauter_strack_lieferverfolgung
(
    tokenhash                VARCHAR                 NOT NULL PRIMARY KEY,
    werksnummer              BIGINT                  NOT NULL,
    datum                    DATE                    NOT NULL,
    zeit                     TIME                    NOT NULL,
    lsnummer                 INTEGER                 NOT NULL,
    gueltigbisdatumzeit      TIMESTAMP               NOT NULL,
    fehlversuche             INTEGER DEFAULT 0       NOT NULL,
    erstellungdatumzeit      TIMESTAMP DEFAULT now() NOT NULL,
    erstellungsbediener      TEXT                    NOT NULL
);

alter table sauter_strack_lieferverfolgung
    owner to postgres;

create index ix_lieferverfolgung_lieferschein
    on sauter_strack_lieferverfolgung (werksnummer, datum, zeit, lsnummer);

create index ix_lieferverfolgung_gueltigbisdatumzeit
    on sauter_strack_lieferverfolgung (gueltigbisdatumzeit);
