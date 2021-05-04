ALTER TABLE ativacao ADD COLUMN callbackurl VARCHAR(255);
ALTER TABLE ativacao ADD COLUMN callback_status INT(4);

CREATE TABLE callback_queue (
    ativacao_idativacao BIGINT(20) PRIMARY KEY,
    
    CONSTRAINT fk_callback_queue_ativacao FOREIGN KEY (ativacao_idativacao) REFERENCES ativacao(idativacao)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;