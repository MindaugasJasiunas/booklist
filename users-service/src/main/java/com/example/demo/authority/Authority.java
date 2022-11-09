package com.example.demo.authority;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Authority {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String id;
    private String authorityName;
}
