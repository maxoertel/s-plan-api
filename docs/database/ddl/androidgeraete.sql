ALTER TABLE androidgeraete
ADD COLUMN apikeyhash varchar,
ADD COLUMN apikeyerstellungdatumzeit timestamp,
ADD COLUMN einmalcodehash varchar,
ADD COLUMN einmalcodeerstellungdatumzeit timestamp,
ADD COLUMN einmalcodegueltigbisdatumzeit timestamp;