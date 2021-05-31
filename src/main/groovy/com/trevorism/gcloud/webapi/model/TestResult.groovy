package com.trevorism.gcloud.webapi.model

import groovy.transform.ToString

@ToString
class TestResult {

    List<TestCase> testCases = []
    boolean result
    String timeRan
}
