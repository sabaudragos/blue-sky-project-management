package com.workshop.projectmanagement.controller;

import com.workshop.projectmanagement.dto.ProjectDto;
import com.workshop.projectmanagement.dto.ProjectPatchNameDto;
import com.workshop.projectmanagement.dto.UserDto;
import com.workshop.projectmanagement.service.ProjectService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/project")
public class ProjectController {
    private ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ProjectDto createProject(@RequestBody ProjectDto projectDto) {
        return projectService.createProject(projectDto);
    }

    @PutMapping
    public ProjectDto updateProject(@RequestBody ProjectDto projectDto) {
        return projectService.updateProject(projectDto);
    }

    @GetMapping ("/{id}")
    public ProjectDto getProject(@PathVariable Integer id) {
        return projectService.getProject(id);
    }

    @DeleteMapping ("/{id}")
    public void deleteProject(@PathVariable Integer id) {
        projectService.deleteProject(id);
    }

    @PatchMapping
    public ProjectDto patchProjectName(@RequestBody ProjectPatchNameDto projectPatchNameDto) {
        return projectService.patchProject(projectPatchNameDto);
    }
}
