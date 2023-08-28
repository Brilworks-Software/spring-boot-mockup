package com.brilworks.mockup.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Transfer object for authentication containing an authentication token.")
public class AuthenticationTransfer {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Authentication token", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZFRQQTV2YlFva2JnbDBoY0dqSSIsImNyZWF0ZWQiOjE2OTI5NDg4NjU4MTcsImV4cCI6MzI3MDg3MjA2NX0.Th607DHgmj5MfBlBJqzbc8x41w3prmkD4IBdYVtSxORKIeZzditdv3GPIJknlk6ZyMmZDjo_GXFFDUEnI3EwYA")
    private String token;
}
