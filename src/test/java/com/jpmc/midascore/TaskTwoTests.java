package com.jpmc.midascore;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class TaskTwoTests {
    static final Logger logger = LoggerFactory.getLogger(TaskTwoTests.class);

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private FileLoader fileLoader;

    @Test
    void task_two_verifier() throws InterruptedException {
        String[] transactionLines = fileLoader.loadStrings("test_data/poiuytrewq.uiop");
        for (String transactionLine : transactionLines) {
            kafkaProducer.send(transactionLine);
        }

        Thread.sleep(2000); // Let Kafka catch up

        logger.info("----------------------------------------------------------");
        logger.info("🧠 use your debugger to watch for incoming transactions");
        logger.info("🛑 kill this test once you find the answer");
        logger.info("----------------------------------------------------------");

        int maxLoops = 6; // ~2 minutes of wait time
        while (maxLoops-- > 0) {
            Thread.sleep(20000);
            logger.info("...still watching...");
        }

        logger.info("✅ Done waiting — if you ain't see the transactions, check the config");
    }
}
