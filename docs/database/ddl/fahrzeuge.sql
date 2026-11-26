ALTER TABLE fahrzeug
ADD COLUMN fahrzeugortungdatumzeit timestamp,
ADD COLUMN fahrzeugoffenemissionsupdateanzahl integer DEFAULT 0,
ADD COLUMN fahrzeugoffenemissionsanzahl integer DEFAULT 0,
ADD COLUMN fahrzeugaktuellemissionsnr varchar,
ADD COLUMN fahrzeugbatteriestand double precision,
ADD COLUMN fahrzeuggpsaktiv boolean DEFAULT false,
ADD COLUMN fahrzeugnetzwerktyp varchar,
ADD COLUMN fahrzeuggeschwindigkeitinmpros double precision,
ADD COLUMN fahrzeuggeschwindigkeitgenauigkeit double precision,
ADD COLUMN fahrzeugfahrtrichtungingrad double precision;

ALTER TABLE fahrzeug
ADD COLUMN strackeinstellungprofilid INTEGER NOT NULL DEFAULT 0;