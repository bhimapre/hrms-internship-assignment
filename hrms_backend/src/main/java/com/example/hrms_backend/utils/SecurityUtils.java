package com.example.hrms_backend.utils;


import com.example.hrms_backend.services.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class SecurityUtils {

    // Get User Id from JWT Tokn
    public static UUID getCurrentUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)){
            throw new IllegalStateException("User is not authenticated");
        }

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        return user.getUserId();
    }

    public static String getRole(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)){
            throw new IllegalStateException("User is not authenticated");
        }

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        return user.getRole();
    }
}
