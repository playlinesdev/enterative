use enterative
go
/*
--------------------------------------------------------------------------------
-- Inicializa Banco de Dados
--------------------------------------------------------------------------------
drop database wsenterative
go
create database wsenterative

/*
--------------------------------------------------------------------------------
-- Cadastra Servidor da BHN
--------------------------------------------------------------------------------
use wsenterative
go

INSERT INTO servidor (situacao,descricao) VALUES(1,'HOMOLOGACAO');
go
INSERT INTO recurso(servidor_idservidor,tipo,endereco) VALUES((SELECT idservidor from servidor where descricao = 'HOMOLOGACAO'),0,'https://blast.preprod.blackhawk-net.com:8443/transactionManagement/v2/transaction');
go
INSERT INTO recurso(servidor_idservidor,tipo,endereco) VALUES((SELECT idservidor from servidor where descricao = 'HOMOLOGACAO'),4,'https://blast.preprod.blackhawk-net.com:8443/transactionManagement/v2/transaction/reverse');
go
INSERT INTO recurso(servidor_idservidor,tipo,endereco) VALUES((SELECT idservidor from servidor where descricao = 'HOMOLOGACAO'),6,'https://blast.preprod.blackhawk-net.com:8443/transactionManagement/v2/transaction/network');

--CADASTRA OS PARAMETROS PARA HOMOLOGACAO


*/


/*
--------------------------------------------------------------------------------
-- Ativações Pendentes
--------------------------------------------------------------------------------
use wsenterative
go

select
    CASE WHEN T.status = 0 THEN "ATIVACAO"
        WHEN T.status = 1 THEN "DESFAZIMENTO"
        WHEN T.status = 2 THEN "PROCESSADA"
        WHEN T.status = 3 THEN "NAO PROCESSADA"
        ELSE "ERRO"
    END AS _TIPO,
    T.situacao,
    T.ttl_ativacao,
    T.ttl_desfazimento, 
    T.*
from ativacao T
where NOT (T.status = 2 AND situacao IS NULL )

*/

--------------------------------------------------------------------------------
-- Acompanhamento de transações
--------------------------------------------------------------------------------
SELECT
    A.codexterno,
    T.idtransacao,
    CASE WHEN T.acao = 0 THEN "ENVIO"
         WHEN T.acao = 1 THEN "RETORNO"
         ELSE "ERRO"
    END AS _ACAO,
    CASE WHEN T.status = 0 THEN "ATIVACAO"
         WHEN T.status = 1 THEN "DESFAZIMENTO"
         WHEN T.status = 2 THEN "PROCESSADA"
         ELSE "ERRO"
    END AS _TIPO,
    CASE WHEN T.response_code = "00" THEN "APROVADO"
         WHEN T.response_code = "74" THEN "SOFT DECLINE"
         WHEN T.response_code = "15" THEN "SOFT DECLINE"
         WHEN T.response_code IS NULL THEN "TIMEOUT"
         ELSE "HARD DECLINE"
    END AS _STATUS,
    T.response_code,
	CASE WHEN T.response_code = "00" THEN "Approved – balance available"
		 WHEN T.response_code = "01" THEN "Approved – balance unavailable"
		 WHEN T.response_code = "03" THEN "Approved – balance unavailable on external account number"
		 WHEN T.response_code = "74" THEN "Unable to route / System Error"
		 WHEN T.response_code = "15" THEN "Time Out occurred- Auth Server not available /responding"
		 WHEN T.response_code = "02" THEN "Refer to card issuer"
		 WHEN T.response_code = "04" THEN "Already Redeemed"
		 WHEN T.response_code = "05" THEN "Error account problem"
		 WHEN T.response_code = "06" THEN "Invalid expiration date"
		 WHEN T.response_code = "07" THEN "Unable to process"
		 WHEN T.response_code = "08" THEN "Card not found"
		 WHEN T.response_code = "12" THEN "Invalid transaction"
		 WHEN T.response_code = "13" THEN "Invalid amount"
		 WHEN T.response_code = "14" THEN "Invalid Product"
		 WHEN T.response_code = "16" THEN "Invalid status change"
		 WHEN T.response_code = "17" THEN "Invalid merchant"
		 WHEN T.response_code = "18" THEN "Invalid Phone Number"
		 WHEN T.response_code = "20" THEN "Invalid Pin"
		 WHEN T.response_code = "21" THEN "Card already active"
		 WHEN T.response_code = "22" THEN "Card Already Associated"
		 WHEN T.response_code = "30" THEN "Bad track2 – format error "
		 WHEN T.response_code = "33" THEN "Expired card"
		 WHEN T.response_code = "34" THEN "Already reversed"
		 WHEN T.response_code = "35" THEN "Already voided"
		 WHEN T.response_code = "36" THEN "Restricted card"
		 WHEN T.response_code = "37" THEN "Restricted External Account "
		 WHEN T.response_code = "38" THEN "Restricted Merchant"
		 WHEN T.response_code = "41" THEN "Lost card"
		 WHEN T.response_code = "42" THEN "Lost External Account "
		 WHEN T.response_code = "43" THEN "Stolen card"
		 WHEN T.response_code = "44" THEN "Stolen External Account "
		 WHEN T.response_code = "51" THEN "Insufficient funds"
		 WHEN T.response_code = "54" THEN "Expired External Account "
		 WHEN T.response_code = "55" THEN "Max recharge reached"
		 WHEN T.response_code = "56" THEN "Advance less amount / enter lesser amount"
		 WHEN T.response_code = "58" THEN "Request not permitted by merchant location"
		 WHEN T.response_code = "59" THEN "Request not permitted by processor "
		 WHEN T.response_code = "61" THEN "Exceeds withdrawal amt / over limit"
		 WHEN T.response_code = "62" THEN "Exceeds financial limit "
		 WHEN T.response_code = "65" THEN "Exceeds withdrawal frequency limit"
		 WHEN T.response_code = "66" THEN "Exceeds transaction count limit "
		 WHEN T.response_code = "69" THEN "Format error –bad data"
		 WHEN T.response_code = "71" THEN "Invalid External Account number"
		 WHEN T.response_code = "94" THEN "Duplicate transaction"
		 WHEN T.response_code = "95" THEN "Cannot Reverse the Original Transaction"
		 WHEN T.response_code = "99" THEN "General decline"
	END AS _STATUS_DETALHADO,
    R.idrecurso,
    CASE WHEN tipo = 0 THEN "FINANCIAL"
         WHEN tipo = 1 THEN "PRE_AUTHORIZATION"
         WHEN tipo = 2 THEN "SAF"
         WHEN tipo = 3 THEN "PRE_AUTOHRIZATION_SAF"
         WHEN tipo = 4 THEN "REVERSAL"
         WHEN tipo = 5 THEN "PRE_AUTOHRIZATION_REVERSAL"
         WHEN tipo = 6 THEN "NETWORK"
         ELSE "ERRO"
   END AS _TIPO_RECURSO,
   R.endereco,
   S.idservidor,
   S.descricao    
FROM transacao T
LEFT JOIN ativacao A ON A.idativacao = T.ativacao_idativacao
LEFT JOIN recurso R ON R.idrecurso = T.recurso_idrecurso
LEFT JOIN servidor S ON S.idservidor = R.servidor_idservidor
--WHERE T.acao = 0 -- APENAS ENVIOS
ORDER BY CAST( A.codexterno AS SIGNED) , T.idtransacao

--select * from voucher


