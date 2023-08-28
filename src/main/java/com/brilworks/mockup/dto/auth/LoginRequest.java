package com.brilworks.mockup.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Schema(description = "Represents a login request containing client credentials.")
public class LoginRequest {
    @JsonProperty("user_name")
    @NotNull
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "User Name", example = "abc@xyx.com")
    private String userName;

    @JsonProperty("password")
    @NotNull
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Password", example = "Qwerty@123")
    private String password;
}
