package com.trevorism.gcloud.webapi.model

class TestSuiteDetails {

    String id
    String testSuiteId
    String testCodeUrl
    String testResultUrl
    boolean lastRunSuccess
    Date lastRunDate
    List<String> codeUnderTestUrls = []
    Map<String, TestMetadata> testMetadata = [:]


}
