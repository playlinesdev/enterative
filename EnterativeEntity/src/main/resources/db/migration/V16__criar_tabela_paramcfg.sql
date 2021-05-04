CREATE TABLE paramcfg ( 
	ticodigo	varchar(255) NOT NULL,
    grcodigo	varchar(255) NOT NULL,
    dado    	varchar(255) NULL,
    PRIMARY KEY(ticodigo,grcodigo)
    
) ENGINE=InnoDB DEFAULT CHARSET=utf8;