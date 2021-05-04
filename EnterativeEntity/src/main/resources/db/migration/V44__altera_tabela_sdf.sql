ALTER TABLE sdf_file ADD COLUMN status INT(4);

ALTER TABLE sdf_detail ADD COLUMN status INT(4);
ALTER TABLE sdf_detail ADD COLUMN transacao_id BIGINT(20);
ALTER TABLE sdf_detail ADD CONSTRAINT fk_sdf_detail_transacao_id FOREIGN KEY (transacao_id) REFERENCES transacao(id);