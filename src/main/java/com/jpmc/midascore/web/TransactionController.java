
package com.jpmc.midascore.web;

import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping("/transactions")
    public ResponseEntity<Void> submit(@RequestBody Transaction tx) {
        service.process(tx);
        return ResponseEntity.accepted().build();
    }
}
