package com.bootcampproject.bootcamp_project.service;

import com.bootcampproject.bootcamp_project.entity.Role;
import com.bootcampproject.bootcamp_project.enums.RoleEnum;
import com.bootcampproject.bootcamp_project.repository.RoleRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class RoleService {

    private final RoleRepository roleRepository;

    @Transactional
    public boolean isRoleExists(String authority) {
        return roleRepository.countByAuthority(authority) > 0;
    }

    //@Transactional
    public Role saveRole(String authority) {

        Role role = Role.builder()
                .authority(authority).build();
        return roleRepository.save(role);
    }

    public List<Role> createRoles(RoleEnum roleEnum) {
        Role role = roleRepository.findByAuthority(roleEnum.name());
        List<Role> roles = new ArrayList<Role>();
        roles.add(role);
        return roles;
    }

}
