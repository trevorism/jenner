package com.trevorism.gcloud.webapi.controller

import com.trevorism.gcloud.webapi.model.JennerRequest
import org.junit.Test

class CallbackControllerTest {

    @Test
    void testCheckResults() {
        String id = "4802279928496128"
        CallbackController callbackController = new CallbackController()
        println callbackController.checkResults(new JennerRequest(testSuiteId: id, jobName: "unit-datastore-client", type: "unit"))
    }

    @Test
    void testCheckXmlContent() {
        CallbackController callbackController = new CallbackController()
        //def thing = callbackController.checkXmlContent(new JennerRequest(jobName: "unit-list"))

        assert callbackController
    }

}
