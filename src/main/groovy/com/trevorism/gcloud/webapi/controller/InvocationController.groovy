package com.trevorism.gcloud.webapi.controller

import com.fasterxml.jackson.annotation.ObjectIdGenerators
import com.google.gson.Gson
import com.trevorism.gcloud.webapi.model.JennerRequest
import com.trevorism.https.DefaultSecureHttpClient
import com.trevorism.https.SecureHttpClient
import com.trevorism.schedule.DefaultScheduleService
import com.trevorism.schedule.ScheduleService
import com.trevorism.schedule.factory.DefaultScheduledTaskFactory
import com.trevorism.schedule.factory.EndpointSpec
import com.trevorism.schedule.factory.ScheduledTaskFactory
import com.trevorism.schedule.model.HttpMethod
import com.trevorism.schedule.model.ScheduledTask
import com.trevorism.secure.Roles
import com.trevorism.secure.Secure
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation

import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.UriInfo
import java.time.Instant
import java.util.logging.Logger

@Api("Invocation Operations")
@Path("/invocation")
class InvocationController {

    private static final Logger log = Logger.getLogger(InvocationController.class.name)
    private SecureHttpClient client = new DefaultSecureHttpClient()
    private ScheduleService scheduleService = new DefaultScheduleService()

    @ApiOperation(value = "Run a job **Secure")
    @POST
    @Secure(Roles.SYSTEM)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    boolean runJob(@Context UriInfo uriInfo, JennerRequest jennerRequest) {
        client.post(createCinvokeUrl(jennerRequest), "{}")

        String callbackUrl = "${uriInfo.baseUri}callback/"
        ScheduledTaskFactory factory = new DefaultScheduledTaskFactory()
        Gson gson = new Gson()
        EndpointSpec endpointSpec = new EndpointSpec(callbackUrl, HttpMethod.POST, gson.toJson(jennerRequest))
        ScheduledTask scheduledTask = factory.createImmediateTask("${jennerRequest.jobName}-${UUID.randomUUID().toString()}", Instant.now().plus(60 * 10).toDate(), endpointSpec)

        log.info("Scheduled Task: ${scheduledTask}")

        scheduleService.create(scheduledTask)

        return true

    }

    private static String createCinvokeUrl(JennerRequest request) {
        return "https://cinvoke.datastore.trevorism.com/job/${request.jobName}/build"
    }
}
