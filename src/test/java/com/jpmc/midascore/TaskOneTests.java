package com.jpmc.midascore;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TaskOneTests {
    private static final Logger logger = LoggerFactory.getLogger(TaskOneTests.class);

    @Test
    void task_one_verifier() throws InterruptedException {
        Thread.sleep(2000);

        logger.info("----------------------------------------------------------");
        logger.info("Starting Task One Verification");
        logger.info("----------------------------------------------------------");

        logger.info("Congrats! It looks like your application booted without issue");
        logger.info("Submit the following output to complete the task (include begin and end output denotations)");

        StringBuilder output = new StringBuilder();
        output.append("\n---begin output ---\n");
        for (int i = 0; i < 10; i++) {
            output.append((int) Math.floor(Math.pow(i, i))).append(" ");
        }
        output.append("\n---end output ---");

        logger.info(output.toString());
    }
}
