package com.kupstudio.bbarge.security.admin;

import com.kupstudio.bbarge.dto.admin.AdminAuthDto;
import com.kupstudio.bbarge.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminDetailService implements UserDetailsService {

    private final AdminService adminService;


    @Override
    public UserDetails loadUserByUsername(String adminId) throws UsernameNotFoundException {

        AdminAuthDto adminDto = adminService.getAdminInfoByAdminId(adminId);

        if (adminDto == null) {

            throw new UsernameNotFoundException("ADMIN NOT FOUND : " + adminId);
        }


        return new AdminPrincipal(adminDto);

    }
}
