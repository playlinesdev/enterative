/*
drop database wsenterative
go
create database wsenterative
*/

use wsenterative
go

INSERT INTO servidor (situacao,descricao) VALUES(1,'CATATAU');
go
INSERT INTO recurso(servidor_idservidor,tipo,endereco) VALUES((SELECT idservidor from servidor where descricao = 'CATATAU'),0,'http://192.168.10.102:8080/transactionManagement/v2/transaction');
go
INSERT INTO recurso(servidor_idservidor,tipo,endereco) VALUES((SELECT idservidor from servidor where descricao = 'CATATAU'),4,'http://192.168.10.102:8080/transactionManagement/v2/transaction/reverse');
go
INSERT INTO recurso(servidor_idservidor,tipo,endereco) VALUES((SELECT idservidor from servidor where descricao = 'CATATAU'),6,'http://192.168.10.102:8080/transactionManagement/v2/transaction/network');

go
INSERT INTO servidor (situacao,descricao) VALUES(1,'BLACKHALK_HOMOLOGACAO');
go
INSERT INTO recurso(servidor_idservidor,tipo,endereco) VALUES((SELECT idservidor from servidor where descricao = 'BLACKHALK_HOMOLOGACAO'),0,'https://blast.preprod.blackhawk-net.com:8443/transactionManagement/v2/transaction');
go
INSERT INTO recurso(servidor_idservidor,tipo,endereco) VALUES((SELECT idservidor from servidor where descricao = 'BLACKHALK_HOMOLOGACAO'),4,'https://blast.preprod.blackhawk-net.com:8443/transactionManagement/v2/transaction/reverse');
go
INSERT INTO recurso(servidor_idservidor,tipo,endereco) VALUES((SELECT idservidor from servidor where descricao = 'BLACKHALK_HOMOLOGACAO'),6,'https://blast.preprod.blackhawk-net.com:8443/transactionManagement/v2/transaction/network');

go

INSERT INTO servidor (situacao,descricao) VALUES(1,'CRUX_BHN_8010');
go
INSERT INTO recurso(servidor_idservidor,tipo,endereco) VALUES((SELECT idservidor from servidor where descricao = 'CRUX_BHN_8010'),0,'http://192.168.10.4:8010/transactionManagement/v2/transaction');
go
INSERT INTO recurso(servidor_idservidor,tipo,endereco) VALUES((SELECT idservidor from servidor where descricao = 'CRUX_BHN_8010'),4,'http://192.168.10.4:8010/transactionManagement/v2/transaction/reverse');
go
INSERT INTO recurso(servidor_idservidor,tipo,endereco) VALUES((SELECT idservidor from servidor where descricao = 'CRUX_BHN_8010'),6,'http://192.168.10.4:8010/transactionManagement/v2/transaction/network');

go
INSERT INTO servidor (situacao,descricao) VALUES(1,'CRUX_BHN_8011');
go
INSERT INTO recurso(servidor_idservidor,tipo,endereco) VALUES((SELECT idservidor from servidor where descricao = 'CRUX_BHN_8011'),0,'http://192.168.10.4:8011/transactionManagement/v2/transaction');
go
INSERT INTO recurso(servidor_idservidor,tipo,endereco) VALUES((SELECT idservidor from servidor where descricao = 'CRUX_BHN_8011'),4,'http://192.168.10.4:8011/transactionManagement/v2/transaction/reverse');
go
INSERT INTO recurso(servidor_idservidor,tipo,endereco) VALUES((SELECT idservidor from servidor where descricao = 'CRUX_BHN_8011'),6,'http://192.168.10.4:8011/transactionManagement/v2/transaction/network');

go
INSERT INTO servidor (situacao,descricao) VALUES(1,'CRUX_BHN_8012');
go
INSERT INTO recurso(servidor_idservidor,tipo,endereco) VALUES((SELECT idservidor from servidor where descricao = 'CRUX_BHN_8012'),0,'http://192.168.10.4:8012/transactionManagement/v2/transaction');
go
INSERT INTO recurso(servidor_idservidor,tipo,endereco) VALUES((SELECT idservidor from servidor where descricao = 'CRUX_BHN_8012'),4,'http://192.168.10.4:8012/transactionManagement/v2/transaction/reverse');
go
INSERT INTO recurso(servidor_idservidor,tipo,endereco) VALUES((SELECT idservidor from servidor where descricao = 'CRUX_BHN_8012'),6,'http://192.168.10.4:8012/transactionManagement/v2/transaction/network');

go
select  idservidor ,
        descricao ,
        CASE WHEN situacao = 0 THEN "INATIVO"
             WHEN situacao = 1 THEN "ATIVO"
             ELSE "ERRO"
        END AS _SITUACAO
from servidor
go
select idrecurso,
       servidor_idservidor,
       CASE WHEN tipo = 0 THEN "FINANCIAL"
             WHEN tipo = 1 THEN "PRE_AUTHORIZATION"
			 WHEN tipo = 2 THEN "SAF"
			 WHEN tipo = 3 THEN "PRE_AUTOHRIZATION_SAF"
			 WHEN tipo = 4 THEN "REVERSAL"
			 WHEN tipo = 5 THEN "PRE_AUTOHRIZATION_REVERSAL"
			 WHEN tipo = 6 THEN "NETWORK"
             ELSE "ERRO"
       END AS _TIPO_RECURSO,
       endereco
 from recurso

-- update servidor set situacao = 1 where idservidor = 1