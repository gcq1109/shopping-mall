package com.userservice.repo;

import com.userservice.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author gcq1109
 * @date 2023/7/5 21:24
 * @email gcq1109@126.com
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUserName(String userName);

    User findByUserPhone(String userPhone);

}
