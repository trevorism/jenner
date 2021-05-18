package com.trevorism.gcloud.webapi.controller

import com.trevorism.gcloud.webapi.model.JennerRequest
import com.trevorism.schedule.DefaultScheduleService
import com.trevorism.schedule.ScheduleService
import com.trevorism.secure.Roles
import com.trevorism.secure.Secure
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation

import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import java.util.logging.Logger

@Api("Callback Operations")
@Path("/callback")
class CallbackController {

    private ScheduleService scheduleService = new DefaultScheduleService()
    private static final Logger log = Logger.getLogger(CallbackController.class.name)

    @ApiOperation(value = "Check the results **Secure")
    @POST
    @Secure(value = Roles.USER, allowInternal = true)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    boolean checkResults(JennerRequest jennerRequest) {
        log.info("In callback: ${jennerRequest}")
        def result = scheduleService.delete(jennerRequest.jobName)
        log.info("Successfully deleted schedule?: ${result}")
        checkXmlContent(jennerRequest)
        updateTestSuiteMetadata(jennerRequest.testSuiteId)
    }

    boolean checkXmlContent(JennerRequest jennerRequest) {
        false
    }

    boolean updateTestSuiteMetadata(String testSuiteId) {
        false
    }
}
