package com.brilworks.mockup.modules.social.repository;


import com.brilworks.mockup.modules.social.model.UserSocialHandleLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSocialLoginHandlesRepository extends JpaRepository<UserSocialHandleLogin,Long> {
}
