package com.workshop.projectmanagement.service;

import com.workshop.projectmanagement.dto.UserCheckDto;
import com.workshop.projectmanagement.dto.UserDto;
import com.workshop.projectmanagement.entity.UserEntity;
import com.workshop.projectmanagement.repo.UserRepository;
import com.workshop.projectmanagement.util.MapperUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

@Service
public class UserService {
    private final String usermanagementServiceBaseUrl;

    private UserRepository localUserRepository;

    private MapperUtil mapperUtil;

    private final RestTemplate restTemplate;

    public UserService(@Value("${usermanagement.user.baseUrl}") String usermanagementServiceBaseUrl,
                       UserRepository localUserRepository,
                       MapperUtil mapperUtil,
                       RestTemplate restTemplate) {
        this.usermanagementServiceBaseUrl = usermanagementServiceBaseUrl;
        this.localUserRepository = localUserRepository;
        this.mapperUtil = mapperUtil;
        this.restTemplate = restTemplate;
    }

    public UserDto getByUserId(Integer id) {
        return restTemplate
                .getForObject(usermanagementServiceBaseUrl + "/" + id,
                        UserDto.class);
    }

    public List<UserDto> getByUserIdList(List<Integer> idList) {
        String userList =
                idList.stream()
                        .map(Object::toString)
                        .collect(joining(","));

        UserDto[] userDtoArray = restTemplate.getForObject(usermanagementServiceBaseUrl + "/all" + "/" + userList,
                UserDto[].class);

        return Arrays.asList(userDtoArray);
    }

    public List<UserDto> ifUsersExistAddToLocalDatabase(List<Integer> userIdList) {
        List<UserCheckDto> userCheckDtoList = checkIfUsersExist(userIdList);

        localUserRepository.saveAll(userIdList
                .stream()
                .map(UserEntity::new)
                .collect(Collectors.toList()));

        List<UserDto> userDtoList = userCheckDtoList
                .stream()
                .map(UserCheckDto::getUser)
                .collect(Collectors.toList());
        return mapperUtil.mapList(userDtoList, UserDto.class);
    }

    private List<UserCheckDto> checkIfUsersExist(List<Integer> userIdList) {
        String userIds = userIdList.stream()
                .map(Object::toString)
                .collect(joining(","));

        ResponseEntity<List<UserCheckDto>> userCheckDtoArray = restTemplate.exchange(usermanagementServiceBaseUrl + "/check" + "/" + userIds,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<UserCheckDto>>() {
                });

        List<UserCheckDto> responseBody = userCheckDtoArray.getBody();
        if (responseBody != null) {
            List<Integer> usersThatDoNotExist = responseBody
                    .stream()
                    .filter(userCheckDto -> !userCheckDto.getUserExists())
                    .map(userCheckDto -> userCheckDto.getUser().getId())
                    .collect(Collectors.toList());

            if (!usersThatDoNotExist.isEmpty()) {
                throw new IllegalArgumentException("The following users do not exist in the system. Ids: " +
                        usersThatDoNotExist
                                .stream()
                                .map(Object::toString)
                                .collect(joining(", ")));
            }
        }

        return responseBody;
    }
}
