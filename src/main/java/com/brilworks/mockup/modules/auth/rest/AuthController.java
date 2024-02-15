package com.brilworks.mockup.modules.auth.rest;

import com.brilworks.mockup.dto.RestError;
import com.brilworks.mockup.modules.auth.dto.AuthenticationTransfer;
import com.brilworks.mockup.modules.auth.dto.LoginRequest;
import com.brilworks.mockup.modules.user.model.AuthUser;
import com.brilworks.mockup.security.TokenUtils;
import com.brilworks.mockup.modules.auth.service.AuthUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.sasl.AuthenticationException;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "API endpoints for user authentication.")
public class AuthController {

    private final AuthUserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenUtils tokenUtils;

    public AuthController(AuthUserService userService, AuthenticationManager authenticationManager, TokenUtils tokenUtils) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenUtils = tokenUtils;
    }

    @PostMapping("/login")
    @Operation(description = "login api for client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged in.",
                    content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthenticationTransfer.class)),
                    }),
            @ApiResponse(responseCode = "400", description = "Error in login.",
                    content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthenticationTransfer.class)),
                    }),
            @ApiResponse(responseCode = "404", description = "Not found.",
                    content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RestError.class)),
                    })
    })
    public AuthenticationTransfer login(@Parameter(description = "LoginRequest object with userName & password") @Valid @RequestBody LoginRequest loginRequest) throws AuthenticationException {
        AuthUser user = userService.authenticate(loginRequest);
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenUtils.generateToken(user);
        return new AuthenticationTransfer(token);
    }
}
