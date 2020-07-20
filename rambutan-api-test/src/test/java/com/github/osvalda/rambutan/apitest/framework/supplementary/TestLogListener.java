package com.github.osvalda.rambutan.apitest.framework.supplementary;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

@Slf4j
public class TestLogListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult iTestResult) {
        MDC.put("testCaseId", iTestResult.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        log.info("SUCCESSFUL");
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        log.info("FAILED");
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        log.info("SKIPPED");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
    }

    @Override
    public void onStart(ITestContext iTestContext) {
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
    }

}
