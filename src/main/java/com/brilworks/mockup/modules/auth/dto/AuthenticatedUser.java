package com.brilworks.mockup.modules.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.data.annotation.Immutable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Immutable
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Represents an authenticated user with UserDetails information.")
public class AuthenticatedUser implements UserDetails, Serializable {
    @Serial
    @JsonIgnore
    private static final long serialVersionUID = 7483831456232557575L;

    @JsonIgnore
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private final String userName;

    @JsonIgnore
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private final String password;

    @JsonIgnore
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private final List<String> roles;

    public AuthenticatedUser(String userName, String password, List<String> roles) {
        this.userName = userName;
        this.password = password;
        this.roles = roles;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getGrantedAuthorities(roles);
    }

    @JsonIgnore
    private List<GrantedAuthority> getGrantedAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        for (String scope : roles)
            authorities.add(new SimpleGrantedAuthority("ROLE_" + scope));

        return authorities;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return this.userName;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return isEnabled();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return isEnabled();
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return isEnabled();
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
