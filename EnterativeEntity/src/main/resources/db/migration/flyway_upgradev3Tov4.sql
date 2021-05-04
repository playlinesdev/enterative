DROP INDEX schema_version_vr_idx ON schema_version;
DROP INDEX schema_version_ir_idx ON schema_version;
ALTER TABLE schema_version DROP COLUMN version_rank;
ALTER TABLE schema_version DROP PRIMARY KEY;
ALTER TABLE schema_version MODIFY version VARCHAR(50);
ALTER TABLE schema_version ADD CONSTRAINT schema_version_pk PRIMARY KEY (installed_rank);
UPDATE schema_version SET type='BASELINE' WHERE type='INIT';