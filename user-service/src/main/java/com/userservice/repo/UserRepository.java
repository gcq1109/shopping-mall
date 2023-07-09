package com.userservice.repo;

import com.userservice.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gcq1109
 * @date 2023/7/5 21:24
 * @email gcq1109@126.com
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUserName(String userName);

    User findByUserPhone(String userPhone);

    @Modifying
    @Transactional
    @Query(value = "update user set user_phone = ?1 where id = ?2", nativeQuery = true)
    void updateUserPhoneById(String phoneNumber, Integer id);


}
