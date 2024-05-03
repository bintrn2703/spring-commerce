package vn.edu.tdtu.springcommerce.service.impl;

import vn.edu.tdtu.springcommerce.model.CustomUserDetail;
import vn.edu.tdtu.springcommerce.model.User;
import vn.edu.tdtu.springcommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    UserService userService;

//    @Override
//    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
//        User user = userService.getUserByName(name)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        return new CustomUserDetail(user);
//    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new CustomUserDetail(user);
    }
//    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
//        User user = userService.getUserByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        return new CustomUserDetail(user);
//    }
}