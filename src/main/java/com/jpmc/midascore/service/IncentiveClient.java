
package com.jpmc.midascore.service;

import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class IncentiveClient {
    private final RestTemplate restTemplate;
    private final String baseUrl;

    public IncentiveClient(RestTemplate restTemplate,
                           @Value("${incentive.api.url:http://localhost:8080/incentive}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public Incentive fetchIncentive(Transaction tx) {
        // Spring will serialize the Transaction automatically
        Incentive res = restTemplate.postForObject(baseUrl, tx, Incentive.class);
        if (res == null) {
            Incentive zero = new Incentive(0f);
            return zero;
        }
        return res;
    }
}
