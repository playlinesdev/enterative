package br.com.chart.enterative.cron.shoptransaction;

import br.com.chart.enterative.entity.AccountTransaction;
import br.com.chart.enterative.entity.AccountTransactionDeadFile;
import br.com.chart.enterative.helper.EnterativeUtils;
import br.com.chart.enterative.service.base.UserAwareComponent;
import br.com.chart.enterative.service.crud.AccountTransactionCRUDService;
import br.com.chart.enterative.service.crud.AccountTransactionDeadFileCRUDService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author William Leite
 */
@Component
public class AccountTransactionDeadFileSweeperController extends UserAwareComponent {

    @Autowired
    private AccountTransactionCRUDService transactionService;

    @Autowired
    private AccountTransactionDeadFileCRUDService deadFileService;

    @Autowired
    private EnterativeUtils utils;

    @Scheduled(cron = "0 0 0 * * *") // Every day at 12:00 AM, forever
    //    @Scheduled(fixedDelay = 20000)
    public void sweepTransactions() {
        this.log("--- Iniciando varredura de transações");

        LocalDate date = this.utils.toLocalDate(new Date());
        date = date.minusWeeks(1);
        Date endDate = this.utils.fromLocalDate(date);
        List<AccountTransaction> transactions = this.transactionService.findByTransactionDateLessThanOrderByTransactionDateAsc(endDate);

        if (Objects.nonNull(transactions) && !transactions.isEmpty()) {
            this.log(String.format("--- %s transações encontradas", transactions.size()));

            final List<AccountTransactionDeadFile> deadFile = new ArrayList<>();
            final List<Long> transactionsToRemove = new ArrayList<>();

            transactions.stream()
                    .map(this.deadFileService::convert)
                    .forEach(t -> {
                        deadFile.add(t);
                        transactionsToRemove.add(t.getId());
                    });

            try {
                this.deadFileService.saveAndFlush(deadFile);
                this.log("--- Arquivo morto criado com sucesso");
                try {
                    this.transactionService.deleteByID(transactionsToRemove);
                    this.log("--- Transações limpas com sucesso");
                } catch (Exception e) {
                    this.log(String.format("--- Um erro ocorreu ao tentar remover as transações", e.getMessage()));
                    e.printStackTrace();
                }
            } catch (Exception e) {
                this.log(String.format("--- Um erro ocorreu ao tentar salvar o arquivo morto: %s", e.getMessage()));
                e.printStackTrace();
            }
        } else {
            this.log("--- Nenhuma transação encontrada");
        }

        this.log("--- Atualizando última posição");
        try {
            this.transactionService.updateLastPosition();
        } catch (Exception e) {
            this.log(String.format("--- Um erro ocorreu ao tentar atualizar a última posiçao", e.getMessage()));
            e.printStackTrace();
        }

        this.log("--- Varredura finalizada");
    }
}
