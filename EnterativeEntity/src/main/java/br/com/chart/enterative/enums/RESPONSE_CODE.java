package br.com.chart.enterative.enums;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;

/**
 * Enum com as respostas do <b>[E]</b>nterative e da <b>[B]</b>lackHawk
 *
 * @author William Leite
 */
public enum RESPONSE_CODE {
    /**
     * 0 [B00] Aprovado - saldo disponível (Approved – balance available)
     */
    B00("00", "Aprovado - saldo disponível", 0),
    /**
     * 1 [B01] Aprovado - saldo indisponível (Approved – balance unavailable)
     */
    B01("01", "Aprovado - saldo indisponível", 1),
    /**
     * 2 [B02] Consulte o emissor do cartão (Refer to card issuer)
     */
    B02("02", "Consulte o emissor do cartão", 2),
    /**
     * 3 [B03] Aprovado - saldo indisponível na conta externa (Approved – balance unavailable on external account number)
     */
    B03("03", "Aprovado - saldo indisponível na conta externa", 3),
    /**
     * 4 [B04] Cartão redimido previamente (Already Redeemed)
     */
    B04("04", "Cartão redimido previamente", 4),
    /**
     * 5 [B05] Erro - problema na conta (Error account problem)
     */
    B05("05", "Erro - problema na conta", 5),
    /**
     * 6 [B06] Data de validade inválido (Invalid expiration date)
     */
    B06("06", "Data de validade inválido", 6),
    /**
     * 7 [B07] Incapaz de processar (Unable to process)
     */
    B07("07", "Incapaz de processar", 7),
    /**
     * 8 [B08] Cartão não encontrado (Card not found)
     */
    B08("08", "Cartão não encontrado", 8),
    /**
     * 9
     * [B12] Transação inválida (Invalid transaction)
     */
    B12("12", "Transação inválida", 9),
    /**
     * 10 [B13] Valor inválido (Invalid amount)
     */
    B13("13", "Valor inválido", 10),
    /**
     * 11 [B14] Produto inválido (Invalid Product)
     */
    B14("14", "Produto inválido", 11),
    /**
     * 12 [B15] Tempo esgotado - Servidor de autenticação não disponível/respondendo (Time Out occurred- Auth Server not available /responding)
     */
    B15("15", "Tempo esgotado - Servidor de autenticação não disponível/respondendo", 12),
    /**
     * 13 [B16] Mudança de estado inválida (Invalid status change)
     */
    B16("16", "Mudança de estado inválida", 13),
    /**
     * 14 [B17] Distribuidor inválido (Invalid merchant)
     */
    B17("17", "Distribuidor inválido", 14),
    /**
     * 15 [B18] Número de telefone inválido (Invalid Phone Number)
     */
    B18("18", "Número de telefone inválido", 15),
    /**
     * 16 [B20] Pin inválido (Invalid Pin)
     */
    B20("20", "Pin inválido", 16),
    /**
     * 17 [B21] Cartão ativado previamente (Card already active)
     */
    B21("21", "Cartão ativado previamente", 17),
    /**
     * 18 [B22] Cartão associado previamente (Card Already Associated)
     */
    B22("22", "Cartão associado previamente", 18),
    /**
     * 19 [B30] Erro de formatação (Bad track2 – format error)
     */
    B30("30", "Erro de formatação", 19),
    /**
     * 20 [B33] Cartão expirado (Expired card)
     */
    B33("33", "Cartão expirado", 20),
    /**
     * 21 [B34] Cartão revertido previamente (Already reversed)
     */
    B34("34", "Cartão revertido previamente", 21),
    /**
     * 22 [B35] Cartão anulado previamente (Already voided)
     */
    B35("35", "Cartão anulado previamente", 22),
    /**
     * 23 [B36] Cartão restrito (Restricted card)
     */
    B36("36", "Cartão restrito", 23),
    /**
     * 24 [B37] Conta externa restrita (Restricted External Account)
     */
    B37("37", "Conta externa restrita", 24),
    /**
     * 25 [B38] Distribuidor restrito (Restricted Merchant)
     */
    B38("38", "Distribuidor restrito", 25),
    /**
     * 26 [B41] Cartão consta como perdido (Lost card)
     */
    B41("41", "Cartão consta como perdido", 26),
    /**
     * 27 [B42] Conta externa consta como perdida (Lost External Account)
     */
    B42("42", "Conta externa consta como perdida", 27),
    /**
     * 28 [B43] Cartão consta como roubado (Stolen card)
     */
    B43("43", "Cartão consta como roubado", 28),
    /**
     * 29 [B44] Conta externa consta como roubada (Stolen External Account)
     */
    B44("44", "Conta externa consta como roubada", 29),
    /**
     * 30 [B51] Fundos insuficientes (Insufficient funds)
     */
    B51("51", "Fundos insuficientes", 30),
    /**
     * 31 [B54] Conta externa expirada (Expired External Account)
     */
    B54("54", "Conta externa expirada", 31),
    /**
     * 32 [B55] Recarga máxima atingida (Max recharge reached)
     */
    B55("55", "Recarga máxima atingida", 32),
    /**
     * 33 [B56] Adiante valor menor / Entre com um valor menor (Advance less amount / enter lesser amount)
     */
    B56("56", "Adiante valor menor / Entre com um valor menor", 33),
    /**
     * 34 [B58] Requisição não permitida pela localização do distribuidor (Request not permitted by merchant location)
     */
    B58("58", "Requisição não permitida pela localização do distribuidor", 34),
    /**
     * 35 [B59] Requisição não permitida pelo processador (Request not permitted by processor)
     */
    B59("59", "Requisição não permitida pelo processador", 35),
    /**
     * 36 [B61] Valor de retirada excedido / Acima do limite (Exceeds withdrawal amt / over limit)
     */
    B61("61", "Valor de retirada excedido / Acima do limite", 36),
    /**
     * 37 [B62] Limite financeiro excedido (Exceeds financial limit)
     */
    B62("62", "Limite financeiro excedido", 37),
    /**
     * 38 [B65] Limite de frequência de retirada excedido (Exceeds withdrawal frequency limit)
     */
    B65("65", "Limite de frequência de retirada excedido", 38),
    /**
     * 39 [B66] Limite de número de transações excedido (Exceeds transaction count limit)
     */
    B66("66", "Limite de número de transações excedido", 39),
    /**
     * 40 [B69] Erro de formatação - dados inválidos (Format error –bad data)
     */
    B69("69", "Erro de formatação - dados inválidos", 40),
    /**
     * 41 [B71] Número de conta externa inválida (Invalid External Account number)
     */
    B71("71", "Número de conta externa inválida", 41),
    /**
     * 42 [B74] Erro de sistema (Unable to route / System Error)
     */
    B74("74", "Erro de sistema", 42),
    /**
     * 43 [B94] Transação duplicada (Duplicate transaction)
     */
    B94("94", "Transação duplicada", 43),
    /**
     * 44 [B95] Transação original não pode ser revertida (Cannot Reverse the Original Transaction)
     */
    B95("95", "Transação original não pode ser revertida", 44),
    /**
     * 45 [B99] Recusa geral (General decline)
     */
    B99("99", "Recusa geral", 45),
    /**
     * 46 [E00] OK
     */
    E00("E00", "OK", 46),
    /**
     * 47 [E01] Usuário não validado
     */
    E01("E01", "Usuário não validado", 47),
    /**
     * 48 [E02] Usuário inativo
     */
    E02("E02", "Usuário inativo", 48),
    /**
     * 49 [E03] JSON em formato desconhecido
     */
    E03("E03", "JSON em formato desconhecido", 49),
    /**
     * 50 [E04] Distribuidor diverge do esperado
     */
    E04("E04", "Distribuidor diverge do esperado", 50),
    /**
     * 51 [E05] Código externo não preenchido
     */
    E05("E05", "Código externo não preenchido", 51),
    /**
     * 52 [E06] Código de barras inválido
     */
    E06("E06", "Código de barras inválido", 52),
    /**
     * 53 [E07] Produto não encontrado
     */
    E07("E07", "Produto não encontrado", 53),
    /**
     * 54 [E08] Valor de face inválido
     */
    E08("E08", "Valor de face inválido", 54),
    /**
     * 55 [E09] Timeout
     */
    E09("E09", "Timeout", 55),
    /**
     * 56 [E10] Ativação não encontrada
     */
    E10("E10", "Ativação não encontrada", 56),
    /**
     * 57 [E11] Loja diverge do esperado
     */
    E11("E11", "Loja diverge do esperado", 57),
    /**
     * 58 [E12] Distribuidor não possui MID
     */
    E12("E12", "Distribuidor não possui MID", 58),
    /**
     * 59 [E13] Distribuidor não possui Localização
     */
    E13("E13", "Distribuidor não possui Localização", 59),
    /**
     * 60 [E14] Distribuidor não possui Categoria
     */
    E14("E14", "Distribuidor não possui Categoria", 60),
    /**
     * 61 [E15] Categoria do distribuidor não possui Código
     */
    E15("E15", "Categoria do distribuidor não possui Código", 61),
    /**
     * 62 [E16] Código Externo já existente no sistema
     */
    E16("E16", "Código Externo já existente no sistema", 62),
    /**
     * 63 [E99] Erro Geral
     */
    E99("E99", "Erro Geral", 63),
    /**
     * 64 [E17] Violação de Restrição
     */
    E17("E17", "Violação de Restrição", 64),
    /**
     * 65 [E18] Entidade não Encontrada
     */
    E18("E18", "Entidade não Encontrada", 65),
    /**
     * 66 [E19] Senha e/ou confirmação inválida
     */
    E19("E19", "responsecode.e19", 66),
    E20("E20", "responsecode.e20", 67),
    E21("E21", "responsecode.e21", 68),
    E22("E22", "responsecode.e22", 69),
    E23("E23", "responsecode.e23", 70),
    E24("E24", "responsecode.e24", 71),
    E25("E25", "responsecode.e25", 72),
    E26("E26", "responsecode.e26", 73),
    E27("E27", "responsecode.e27", 74),
    E28("E28", "responsecode.e28", 75),
    E29("E29", "responsecode.e29", 76),
    E30("E30", "responsecode.e30", 77),
    E31("E31", "responsecode.e31", 78),
    // Invalid activation process
    E32("E32", "responsecode.e32", 79),
    /**
     * E-pay [0] Sucesso
     */
    P0("P0", "Sucesso", 80),
    /**
     * E-pay [2010] Timeout
     */
    P2010("P2010", "Timeout", 81),
    E33("E33", "responsecode.e33", 82),
    E34("E34", "responsecode.e34", 83);

    @Getter private final String code;
    @Getter private final String description;
    @Getter private final Integer sequence;

    RESPONSE_CODE(String code, String description, Integer sequence) {
        this.code = code;
        this.description = description;
        this.sequence = sequence;
    }

    public static RESPONSE_CODE get(String code) {
        RESPONSE_CODE[] values = RESPONSE_CODE.values();
        Optional<RESPONSE_CODE> result = Arrays.asList(values).stream().filter(r -> r.getCode().equals(code)).findFirst();
        if (result.isPresent()) {
            return result.get();
        } else {
            return null;
        }
    }

    public static List<RESPONSE_CODE> ordered() {
        return Arrays.stream(RESPONSE_CODE.values()).sorted(Comparator.comparing(RESPONSE_CODE::getSequence)).collect(Collectors.toList());
    }
}
