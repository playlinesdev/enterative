CREATE TABLE temp (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(30)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO temp (nome) (SELECT nome FROM roleweb);

ALTER TABLE usuarioweb_roles DROP FOREIGN KEY usuarioweb_roles_ibfk_2;

DROP TABLE roleweb;

RENAME TABLE temp TO roleweb;

ALTER TABLE usuarioweb_roles ADD COLUMN roles_id BIGINT(20);

UPDATE usuarioweb_roles ur SET ur.roles_id = (SELECT r.id FROM roleweb r WHERE r.nome = ur.roles_nome);

ALTER TABLE usuarioweb_roles DROP COLUMN roles_nome;

ALTER TABLE usuarioweb_roles ADD CONSTRAINT usuarioweb_roles_ibfk_2 FOREIGN KEY (roles_id) REFERENCES roleweb(id);