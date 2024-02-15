package com.brilworks.mockup.modules.social.repository;

import com.brilworks.mockup.modules.social.enums.ProviderEnum;
import com.brilworks.mockup.modules.social.model.UserProvider;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserProviderRepository extends JpaRepository<UserProvider, Long> {
    boolean existsByUserIdAndProvider(Long userId, ProviderEnum provider);
}
