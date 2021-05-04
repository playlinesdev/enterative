use wsenterative
go
CREATE PROCEDURE insere_voucher(IN card_no CHAR(20))
BEGIN
  insert into voucher (upc,card_no,amount) values ('505164401188',card_no,'000000002500');
END 

go

CREATE PROCEDURE insere_ativacao(IN codexterno CHAR(20))
BEGIN
  insert into ativacao(codexterno,status,prioridade,data_incl,response_code,situacao,voucher_idvoucher,ttl_ativacao,ttl_desfazimento)
  values (codexterno,0,0,now(),null,null,(select max(idvoucher)from voucher),2,10);
END 

go

--drop procedure popula_solicitacao
CREATE PROCEDURE popula_solicitacao(IN qtd_registros INT)
BEGIN
    DECLARE i int DEFAULT 1;
    WHILE i <= qtd_registros DO
        call insere_voucher('1234567890123456789'); //cupom OK
        call insere_ativacao(i);
        SET i = i + 1;
    END WHILE;
END

go
 
call popula_solicitacao(500)



/*
select * from ativacao
    6039534201000000024
    1234567890123456789
*/
