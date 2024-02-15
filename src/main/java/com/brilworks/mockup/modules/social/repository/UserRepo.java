package com.brilworks.mockup.modules.social.repository;

import com.brilworks.mockup.modules.social.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepo extends JpaRepository<User, Long> {


    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM User e WHERE e.email = :email")
    boolean existByEmailId(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM User e WHERE e.socialId = :socialId")
    boolean existBySocialId(@Param("socialId") String socialId);

    User findBySocialId(String socialId);

    User findByEmail(String email);

}
