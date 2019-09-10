/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.helidon.examples.lra;

import java.net.URI;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.lra.annotation.*;
import org.eclipse.microprofile.lra.annotation.ws.rs.LRA;

import static org.eclipse.microprofile.lra.annotation.ws.rs.LRA.LRA_HTTP_CONTEXT_HEADER;
import static org.eclipse.microprofile.lra.annotation.ws.rs.LRA.LRA_HTTP_PARENT_CONTEXT_HEADER;

@Path("/order")
@ApplicationScoped
public class OrderResource {


    private ParticipantStatus participantStatus;
    private String orderStatus = "none";

    @Inject
    public OrderResource() {
        super();
    }

    @Path("/placeOrder")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @LRA(value = LRA.Type.REQUIRES_NEW)
    public Response placeOrder(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) String lraId)  {
        System.out.println("OrderResource.placeOrder in LRA due to LRA.Type.REQUIRES_NEW lraId:" + lraId);
        participantStatus = ParticipantStatus.Active;
        orderStatus = "pending";
        Response response = ClientBuilder.newBuilder().build().target("http://localhost:8091/inventory/reserveInventoryForOrder")
                .request().header(LRA_HTTP_CONTEXT_HEADER, lraId).get();
        String entity = response.readEntity(String.class);
        System.out.println("OrderResource.placeOrder response from inventory:" + entity);
        if (entity.equals("inventorysuccess")) {
            orderStatus = "completed";
            return Response.ok()
                    .entity("orderStatus:" + orderStatus)
                    .build();
        } else {
            orderStatus = "failed";
            return Response.serverError()
                    .entity("orderStatus:" + orderStatus)
                    .build();
        }
    }

    @Path("/cancelOrder")
    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Compensate
    public Response cancelOrder(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) String lraId) throws NotFoundException {
        participantStatus = ParticipantStatus.Compensating;
        System.out.println("OrderResource.cancelOrder");
        orderStatus = "cancelled";
        participantStatus = ParticipantStatus.Compensated;
        return Response.ok().entity(participantStatus.name()).build();
    }

    @PUT
    @Path("/completeOrder")
    @Produces(MediaType.APPLICATION_JSON)
    @Complete
    public Response completeOrder(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) String lraId) throws NotFoundException {
        participantStatus = ParticipantStatus.Completing;
        System.out.println("OrderResource.completeOrder");
        participantStatus = ParticipantStatus.Completed;
        return Response.ok().entity(participantStatus.name()).build();
    }

    @GET
    @Path("/status")
    @Status
    public Response status(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId,
                           @HeaderParam(LRA_HTTP_PARENT_CONTEXT_HEADER) URI parentlraId) {
        System.out.println("OrderResource.status participantStatus:" + participantStatus +
                " lraId:" + lraId + " parentlraId:" + parentlraId);
        return Response.ok().entity(participantStatus).build();
    }

}
