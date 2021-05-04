ALTER TABLE ativacao ADD pdv VARCHAR(10);
ALTER TABLE ativacao ADD terminal VARCHAR(6);
ALTER TABLE ativacao ADD distribuidor_iddistribuidor BIGINT(20);
ALTER TABLE ativacao ADD usuario_web_idusuarioWeb BIGINT(20);

ALTER TABLE ativacao ADD CONSTRAINT ativacao.fk_distribuidor FOREIGN KEY (distribuidor_iddistribuidor) REFERENCES distribuidor(iddistribuidor);
ALTER TABLE ativacao ADD CONSTRAINT ativacao.fk_usuarioweb FOREIGN KEY (usuario_web_idusuarioWeb) REFERENCES usuarioweb(idusuarioweb);