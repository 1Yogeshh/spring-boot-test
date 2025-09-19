package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserDetailServiceImp;
import com.example.demo.utils.jwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Component
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailServiceImp userDetailServiceImp;

    @Autowired
    private jwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        final String username;
        String email = oAuth2User.getAttribute("email");
        if (email != null) {
            username = email;
        } else {
            username = oAuth2User.getAttribute("login"); 
        }

        // Agar user DB me nahi hai to save karo
        userRepository.findByUsername(username).orElseGet(() -> {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(passwordEncoder.encode("OAUTH_USER")); 
            newUser.setRoles(Arrays.asList("USER"));
            return userRepository.save(newUser);
        });

        // UserDetails load
        var userDetails = userDetailServiceImp.loadUserByUsername(username);

        // JWT generate
        String token = jwtUtils.generateToken(userDetails.getUsername());
        System.out.println("Generated Token for OAuth2 user: " + token);

        // Authentication set
        var newAuth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(newAuth);

        // Redirect with token
        getRedirectStrategy().sendRedirect(request, response, "/weather?token=" + token);
    }

}
