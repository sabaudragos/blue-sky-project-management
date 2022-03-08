package com.workshop.projectmanagement.service;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.workshop.projectmanagement.dto.ProjectDto;
import com.workshop.projectmanagement.dto.ProjectPatchNameDto;
import com.workshop.projectmanagement.dto.UserDto;
import com.workshop.projectmanagement.entity.ProjectEntity;
import com.workshop.projectmanagement.repo.ProjectRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    private final ProjectRepository projectRepository;

    private final UserService userService;

    public ProjectService(ProjectRepository projectRepository,
                          UserService userService) {
        this.projectRepository = projectRepository;
        this.userService = userService;
    }

    public ProjectDto createProject(ProjectDto projectDto) {
        ProjectEntity savedProject = projectRepository.save(mapper.map(projectDto, ProjectEntity.class));
        ProjectDto savedProjectDto = mapper.map(savedProject, ProjectDto.class);
        savedProjectDto.setUserList(fetchUserList(savedProjectDto.getUserList()));

        return savedProjectDto;
    }

    public ProjectDto updateProject(ProjectDto projectDto) {
        ProjectEntity savedProject = projectRepository.save(mapper.map(projectDto, ProjectEntity.class));
        ProjectDto savedProjectDto = mapper.map(savedProject, ProjectDto.class);
        savedProjectDto.setUserList(fetchUserList(savedProjectDto.getUserList()));

        return savedProjectDto;
    }

    public ProjectDto patchProject(ProjectPatchNameDto projectPatchNameDto) {
        ProjectEntity projectEntity = projectRepository.getById(projectPatchNameDto.getId());
        projectEntity.setName(projectPatchNameDto.getName());
        ProjectEntity patchedProject = projectRepository.save(projectEntity);

        return mapper.map(patchedProject, ProjectDto.class);

    }

    public ProjectDto getProject(Integer id) {
        ProjectEntity projectEntity = projectRepository.getById(id);
        ProjectDto projectDto = mapper.map(projectEntity, ProjectDto.class);
        projectDto.setUserList(fetchUserList(projectDto.getUserList()));

        return projectDto;
    }

    private List<UserDto> fetchUserList(List<UserDto> userList) {
        List<UserDto> fetchedUserList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(userList)) {
            List<Integer> userIdList = userList
                    .stream()
                    .map(UserDto::getId)
                    .collect(Collectors.toList());

            fetchedUserList = userService.getByUserIdList(userIdList);
        }

        return fetchedUserList;
    }

    public void deleteProject(Integer id) {
        projectRepository.deleteById(id);
    }
}
