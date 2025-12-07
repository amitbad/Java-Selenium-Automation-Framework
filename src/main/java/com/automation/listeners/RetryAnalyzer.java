package com.automation.listeners;

import com.automation.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retry Analyzer - Implements TestNG IRetryAnalyzer for automatic test retry on failure.
 */
public class RetryAnalyzer implements IRetryAnalyzer {
    
    private static final Logger logger = LogManager.getLogger(RetryAnalyzer.class);
    private int retryCount = 0;
    private final int maxRetryCount;
    
    public RetryAnalyzer() {
        this.maxRetryCount = ConfigManager.getInstance().getRetryCount();
    }
    
    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            logger.info("Retrying test '{}' - Attempt {} of {}", 
                result.getName(), retryCount, maxRetryCount);
            return true;
        }
        return false;
    }
}
