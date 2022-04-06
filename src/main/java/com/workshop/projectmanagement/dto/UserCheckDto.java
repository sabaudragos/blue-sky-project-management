package com.workshop.projectmanagement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCheckDto {
    private Boolean userExists;
    private UserDto user;
}
