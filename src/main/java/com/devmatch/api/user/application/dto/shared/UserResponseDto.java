package com.devmatch.api.user.application.dto.shared;

import com.devmatch.api.tag.application.dto.TagResponseDto;
import lombok.Data;
import java.util.List;

@Data
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String country;
    private String province;
    private String city;
    private String githubUrl;
    private String linkedinUrl;
    private String portfolioUrl;
    private String avatarUrl;
    private String bio;
    private String role;
    private boolean active;
    private boolean deleted;
    private List<String> profileTypes;
    private List<TagResponseDto> tags;
}
