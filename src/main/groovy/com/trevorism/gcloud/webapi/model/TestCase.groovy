package com.trevorism.gcloud.webapi.model

import groovy.transform.ToString

@ToString
class TestCase {

    String name
    String classname
    double time
    boolean failed

}
