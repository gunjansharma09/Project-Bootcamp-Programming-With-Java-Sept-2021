package com.bootcampproject.bootcamp_project.service;

import com.bootcampproject.bootcamp_project.entity.Role;
import com.bootcampproject.bootcamp_project.repository.RoleRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Data
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
}
