package com.trevorism.gcloud.webapi.service

import com.google.gson.Gson
import com.trevorism.data.PingingDatastoreRepository
import com.trevorism.data.Repository
import com.trevorism.gcloud.webapi.model.TestCase
import com.trevorism.gcloud.webapi.model.TestMetadata
import com.trevorism.gcloud.webapi.model.TestResult
import com.trevorism.gcloud.webapi.model.TestSuiteDetails
import com.trevorism.https.DefaultSecureHttpClient
import com.trevorism.https.SecureHttpClient
import org.cyberneko.html.parsers.SAXParser

import java.util.logging.Logger

class JunitXmlTestResultService implements TestResultService {

    public static final String BASE_URL = "https://testing.trevorism.com/api/suite"

    private SecureHttpClient secureHttpClient = new DefaultSecureHttpClient()
    private Repository<TestSuiteDetails> repository = new PingingDatastoreRepository<>(TestSuiteDetails)
    private static final Logger log = Logger.getLogger(JunitXmlTestResultService.class.name)

    @Override
    TestSuiteDetails getTestSuiteDetails(String testSuiteId) {
        try {
            String json = secureHttpClient.get("$BASE_URL/$testSuiteId/detail")
            Gson gson = new Gson()
            return gson.fromJson(json, TestSuiteDetails)
        } catch (Exception e) {
            log.warning("Unable to retrieve test suite with id: $id, reason: ${e.message}")
            return null
        }
    }

    @Override
    TestResult parseUnitTestResult(String jobName) {
        Set<Object> links = getAllTestCaseUrls(jobName)

        def xmlSlurper = new XmlSlurper()
        TestResult testResult = new TestResult()
        testResult.result = true

        links.each{
            def parsedXml = xmlSlurper.parse("https://trevorism-build.eastus.cloudapp.azure.com/job/${jobName}/ws/build/test-results/test/${it}/*view*/")
            def attributes = parsedXml.attributes()
            testResult.result = testResult.result && (attributes["failures"] == "0" && attributes["errors"] == "0")
            parsedXml.testcase.each {
                TestCase testCase = new TestCase(name: it['@name'], classname: it['@classname'], failed: !it.failure.isEmpty(), time: Double.valueOf(it['@time'].toString()))
                testResult.testCases << testCase
            }
        }

        return testResult
    }

    private Set<Object> getAllTestCaseUrls(String jobName) {
        XmlSlurper slurper = new XmlSlurper(new SAXParser())
        def path = slurper.parse("https://trevorism-build.eastus.cloudapp.azure.com/job/${jobName}/ws/build/test-results/test/")
        def candidates = path.depthFirst().findAll {
            it.text().startsWith("TEST-com.trevorism") && it.text().endsWith(".xml")
        }
        return candidates.collect { it.text() } as Set
    }

    @Override
    TestSuiteDetails updateTestSuiteDetails(String id, TestResult testResult) {
        TestSuiteDetails details = getTestSuiteDetails(id)
        details.lastRunSuccess = testResult.result
        details.lastRunDate = new Date()
        repository.update(details.id, details)
    }
}
