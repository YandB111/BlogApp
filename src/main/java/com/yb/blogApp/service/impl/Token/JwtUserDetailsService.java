//package com.yb.blogApp.service.impl.Token;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import com.yb.blogApp.entity.UserSignUp;
//import com.yb.blogApp.service.UserSignUpService;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class JwtUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    private UserSignUpService userSignUpService; // Your user service to fetch user data
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        // Load the user from your database using the provided username (usually email)
//        UserSignUp user = userSignUpService.findByEmail(username);
//
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found with username: " + username);
//        }
//
//        // Create a list of authorities/roles for the user
//        List<GrantedAuthority> authorities = new ArrayList<>();
//
//        // Add user role
//        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
//
//        // Check if the user has admin role (you can customize this logic)
//        if (user.isAdmin()) {
//            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//        }
//
//        // Create a UserDetails object with user information and authorities
//        return new User(user.getEmail(), user.getPassword(), authorities);
//    }
//}
