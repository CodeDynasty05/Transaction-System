package com.matrix.Transaction.listener;

import com.matrix.Transaction.model.dto.TransactionDTO;
import com.matrix.Transaction.model.entity.TransactionStatus;
import com.matrix.Transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@EnableScheduling
@EnableAsync
@RequiredArgsConstructor
public class Scheduler {

    private final TransactionService transactionService;

    @Scheduled(cron = "0 0 1 * * ?", zone = "Asia/Baku")
    public void scheduleCron(){
        List<TransactionDTO> transactions = transactionService.getTransactions();
        for(TransactionDTO transactionDTO : transactions){
            if(transactionDTO.getTransactionStatus()==TransactionStatus.PENDING){
                transactionService.changePaymentStatus(transactionDTO.getId(),TransactionStatus.SUCCESS);
            }
        }
    }
}
