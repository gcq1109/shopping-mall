package com.oauth2.repo;

import com.oauth2.pojo.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author gcq1109
 * @date 2023/7/4 17:31
 * @email gcq1109@126.com
 */
@Repository
public interface UserRepository extends JpaRepository<UserInfo, Integer> {
    UserInfo queryByUserName(String userName);
}
