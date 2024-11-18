package com.kupstudio.bbarge.security.admin;

import com.kupstudio.bbarge.dto.admin.AdminAuthDto;
import com.kupstudio.bbarge.enumClass.admin.AdminRoleEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class AdminPrincipal implements UserDetails {

    private AdminAuthDto adminDto;


    public AdminPrincipal(AdminAuthDto adminDto) {
        this.adminDto = adminDto;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(adminDto.getRole().name()));

        return authorityList;
    }

    @Override
    public String getPassword() {
        return adminDto.getPassword();
    }

    @Override
    public String getUsername() {
        return adminDto.getAdminId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !adminDto.isDelete();
    }

    public int getAdminNo() {
        return adminDto.getAdminNo();
    }

    public AdminRoleEnum getAdminRole() {
        return adminDto.getRole();
    }

}
