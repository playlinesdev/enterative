CREATE TABLE server ( 
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    description VARCHAR(255),
    status INT(4),
    echo_product_id BIGINT(20),

    CONSTRAINT fk_server_echo_product_id FOREIGN KEY (echo_product_id) REFERENCES product(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE resource ( 
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    endereco VARCHAR(255),
    tipo INT(4),
    server_id BIGINT(20),
    
    CONSTRAINT fk_resource_server_id FOREIGN KEY (server_id) REFERENCES server(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO server (description, status) (SELECT descricao, situacao FROM servidor);

INSERT INTO resource (endereco, tipo, server_id) (SELECT endereco, tipo, servidor_idservidor FROM recurso);

ALTER TABLE transacao DROP FOREIGN KEY fk_transacao_recurso_id;

DROP TABLE IF EXISTS recurso;
DROP TABLE IF EXISTS servidor;

ALTER TABLE transacao ADD CONSTRAINT fk_transacao_recurso_id FOREIGN KEY (recurso_id) REFERENCES resource(id);