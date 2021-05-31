package com.trevorism.gcloud.webapi.service

import com.trevorism.gcloud.webapi.model.TestResult
import com.trevorism.gcloud.webapi.model.TestSuiteDetails

interface TestResultService {

    TestSuiteDetails getTestSuiteDetails(String id)
    TestResult parseUnitTestResult(String jobName)
    TestSuiteDetails updateTestSuiteDetails(String id, TestResult testResult)

}