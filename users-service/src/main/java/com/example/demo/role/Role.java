package com.example.demo.role;

import com.example.demo.authority.Authority;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Role{
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String id;
    private String roleName;
    private List<Authority> authorities;
}