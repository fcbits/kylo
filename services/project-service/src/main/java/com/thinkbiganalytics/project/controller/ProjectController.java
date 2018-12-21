package com.thinkbiganalytics.project.controller;

/*-
 * #%L
 * project-service
 * %%
 * Copyright (C) 2017 - 2018 ThinkBig Analytics, a Teradata Company
 * %%
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
 * #L%
 */

import com.thinkbiganalytics.metadata.api.project.Project;
import com.thinkbiganalytics.project.model.ProjectDTO;
import com.thinkbiganalytics.project.service.ProjectService;
import com.thinkbiganalytics.rest.model.RestResponseStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(tags = "Manage Projects", produces = "application/json")
@Component
@Path("/v1/projects")
@SwaggerDefinition(tags = @Tag(name = "Manage Project", description = "projects"))
public class ProjectController {

    @Inject
    ProjectService projectService;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProjectDTO> listProjects() {
        return projectService.getProjects();
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public ProjectDTO create(Project project) {
        return projectService.createProject(project);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ProjectDTO findProject(@PathParam("id") String id) {
        return projectService.findProjectById(id);
    }

    @DELETE
    @Path("/{id}/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteProject(@PathParam("id") String id) {
        boolean deleted = projectService.deleteProject(id);
        return Response.ok(deleted ? new RestResponseStatus.ResponseStatusBuilder().buildSuccess() : new RestResponseStatus.ResponseStatusBuilder().buildError()).build();
    }

    @PUT
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public ProjectDTO update(ProjectDTO project) {
        return projectService.update(project);
    }
}
