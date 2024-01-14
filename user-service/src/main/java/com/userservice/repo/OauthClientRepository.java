package com.userservice.repo;

import com.userservice.pojo.Oauth2Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gcq1109
 * @email gcq1109@126.com
 */
@Repository
public interface OauthClientRepository extends JpaRepository<Oauth2Client, Integer> {

    Oauth2Client findByClientId(String clientId);

    @Modifying
    @Transactional
    @Query(value = "update oauth_client_details set client_secret = ?1 where client_id = ?2", nativeQuery = true)
    void updateSecretByClientId(String secret, String clientId);

}
