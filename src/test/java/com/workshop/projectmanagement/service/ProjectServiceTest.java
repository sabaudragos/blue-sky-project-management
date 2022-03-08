package com.workshop.projectmanagement.service;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.workshop.projectmanagement.dto.ProjectDto;
import com.workshop.projectmanagement.dto.UserDto;
import com.workshop.projectmanagement.entity.ProjectEntity;
import com.workshop.projectmanagement.repo.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    private final Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    @Mock
    private ProjectRepository projectRepositoryMock;

    @Mock
    private UserService userService;

    private ProjectService projectService;

    @BeforeEach
    public void beforeEach() {
        projectService = new ProjectService(
                projectRepositoryMock,
                userService);
    }

    @Test
    void createProject() {
        // given / arrange
        ProjectDto projectDto = generateProjectDto();
        ProjectEntity projectEntity = mapper.map(projectDto, ProjectEntity.class);
        Mockito.when(projectRepositoryMock.save(any()))
                .thenReturn(projectEntity);

        List<Integer> userIdList = projectDto.getUserList()
                .stream()
                .map(UserDto::getId)
                .collect(Collectors.toList());
        Mockito.when(userService.getByUserIdList(userIdList)).thenReturn(projectDto.getUserList());

        // when / act
        ProjectDto savedProject = projectService.createProject(projectDto);

        //then / assert
        assertEquals(projectDto.getName(), savedProject.getName());
        assertEquals(projectDto.getDescription(), savedProject.getDescription());
        assertEquals(projectDto.getName(), savedProject.getName());

        UserDto savedUser = savedProject.getUserList().get(0);
        assertEquals(projectDto.getUserList().get(0).getId(), savedUser.getId());
    }

    @Test
    void updateProject() {
        // given / arrange
        ProjectDto projectDto = generateProjectDto();
        ProjectEntity projectEntity = mapper.map(projectDto, ProjectEntity.class);
        Mockito.when(projectRepositoryMock.save(any()))
                .thenReturn(projectEntity);

        List<Integer> userIdList = projectDto.getUserList()
                .stream()
                .map(UserDto::getId)
                .collect(Collectors.toList());
        Mockito.when(userService.getByUserIdList(userIdList)).thenReturn(projectDto.getUserList());

        // when / act
        ProjectDto savedProject = projectService.updateProject(projectDto);

        //then / assert
        assertEquals(projectDto.getName(), savedProject.getName());
        assertEquals(projectDto.getDescription(), savedProject.getDescription());
        assertEquals(projectDto.getName(), savedProject.getName());

        UserDto savedUser = savedProject.getUserList().get(0);
        assertEquals(projectDto.getUserList().get(0).getId(), savedUser.getId());
    }


    @Test
    void patchProject() {
    }

    @Test
    void getProject() {
        // given / arrange
        ProjectDto projectDto = generateProjectDto();
        projectDto.setId(1);
        ProjectEntity projectEntity = mapper.map(projectDto, ProjectEntity.class);

        Mockito.when(projectRepositoryMock.getById(projectDto.getId()))
                .thenReturn(projectEntity);

        List<Integer> userIdList = projectDto.getUserList()
                .stream()
                .map(UserDto::getId)
                .collect(Collectors.toList());
        Mockito.when(userService.getByUserIdList(userIdList)).thenReturn(projectDto.getUserList());

        // when / act
        ProjectDto getProject = projectService.getProject(1);

        //then / assert
        assertEquals(projectDto.getName(), getProject.getName());
        assertEquals(projectDto.getDescription(), getProject.getDescription());
        assertEquals(projectDto.getName(), getProject.getName());

        UserDto getUser = getProject.getUserList().get(0);
        assertEquals(projectDto.getUserList().get(0).getId(), getUser.getId());
    }

    @Test
    void deleteProject() {
        // when
        projectService.deleteProject(1);
    }


    private ProjectDto generateProjectDto() {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName("some name");
        projectDto.setDescription("some description");

        UserDto userDto = new UserDto();
        userDto.setId(1);

        projectDto.setUserList(Collections.singletonList(userDto));
        return projectDto;
    }
}