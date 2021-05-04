CREATE TABLE recurso ( 
    idrecurso          	BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    endereco           	VARCHAR(255),
    tipo               	INT(4),
    servidor_idservidor	BIGINT(20),
    
    CONSTRAINT fk_servidor FOREIGN KEY (servidor_idservidor) REFERENCES servidor(idservidor)
    
) ENGINE=InnoDB DEFAULT CHARSET=utf8;