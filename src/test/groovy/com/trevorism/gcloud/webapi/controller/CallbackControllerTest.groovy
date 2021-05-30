package com.trevorism.gcloud.webapi.controller

import com.trevorism.gcloud.webapi.model.JennerRequest
import org.junit.Test

class CallbackControllerTest {
    void testCheckResults() {
    }

    @Test
    void testCheckXmlContent() {
        CallbackController callbackController = new CallbackController()
        def thing =  callbackController.checkXmlContent(new JennerRequest(jobName: "unit-list"))

        println thing
    }

    @Test
    void testUpdateTestSuiteMetadata() {
        def myXml = """<?xml version="1.0" encoding="UTF-8"?>
<testsuite name="com.trevorism.gcloud.webapi.controller.RootControllerTest" tests="2" skipped="0" failures="0" errors="0" timestamp="2021-05-18T19:34:40" hostname="build" time="0.934">
  hello
  <properties/>
  <testcase name="testRootControllerPing" classname="com.trevorism.gcloud.webapi.controller.RootControllerTest" time="0.811"/>
  <testcase name="testRootControllerEndpoints" classname="com.trevorism.gcloud.webapi.controller.RootControllerTest" time="0.121"/>
  <system-out><![CDATA[]]></system-out>
  <system-err><![CDATA[]]></system-err>
</testsuite>
"""
        def xml = new XmlSlurper().parseText(myXml)

        println xml.attributes().each{ k,v -> println "$k,$v"}

        println xml.text()
        println xml.children().each {
            println it['@name']
            println it['@classname']
            println it['@time']
        }

        println xml.testsuite.@tests.text()


        println xml.testsuite.testcase.text()
//        println xml.testsuite.'**'.text()
       // println xml.testsuite.'**'.attributes()


    }
}
