package com.oauth2.service;

import com.oauth2.pojo.UserInfo;
import com.oauth2.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author gcq1109
 * @date 2023/7/4 17:35
 * @email gcq1109@126.com
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo user = userRepository.queryByUserName(username);
        if (user != null) {
            return new User(user.getUserName(), user.getPasswd(),
                    AuthorityUtils.createAuthorityList(user.getPasswd()));
        } else {
            throw new UsernameNotFoundException("未查询到该用户信息");
        }
    }
}
