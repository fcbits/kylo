package com.thinkbiganalytics.project.service;

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

import com.thinkbiganalytics.metadata.api.MetadataAccess;
import com.thinkbiganalytics.metadata.api.project.Project;
import com.thinkbiganalytics.metadata.api.project.ProjectProvider;
import com.thinkbiganalytics.metadata.api.project.security.ProjectAccessControl;
import com.thinkbiganalytics.project.model.ProjectDTO;
import com.thinkbiganalytics.security.AccessController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class ProjectService {

    private static final Logger log = LoggerFactory.getLogger(ProjectService.class);

    @Inject
    private ProjectProvider projectProvider;

    @Inject
    private MetadataAccess metadataAccess;

    @Inject
    private AccessController accessController;

    public List<ProjectDTO> getProjects() {
        return metadataAccess.read(() -> {
            this.accessController.checkPermission(AccessController.SERVICES, ProjectAccessControl.ACCESS_PROJECT);

            List<ProjectDTO> projects = new ArrayList<>();
            for(Project domain: projectProvider.getProjects())
                projects.add(new ProjectDTO(domain));

            return projects;
        });
    }

    //TODO: validate and throw error when project exists
    public ProjectDTO createProject(Project project) {
        Project created = projectProvider.create(project);
        return new ProjectDTO(created);
    }

    public ProjectDTO findProjectById(String id) {
        if(StringUtils.isBlank(id))
            return null;
        return metadataAccess.read(() -> {
            Project found = projectProvider.findById(projectProvider.resolveId(id));
            return new ProjectDTO(found);
        });
    }

    //TODO: handle errors, check project exists
    public boolean deleteProject(String id){
        return metadataAccess.commit(() -> {
            projectProvider.deleteById(projectProvider.resolveId(id));
            return true;
        });
    }

    //TODO: transformation
    public ProjectDTO update(ProjectDTO dto) {
        return metadataAccess.commit(() -> {
            Project found = projectProvider.findById(projectProvider.resolveId(dto.getId()));
            Project updated = projectProvider.update(ProjectDTO.toDomain(dto, found));
            return new ProjectDTO(updated);
        });
    }
}
