package vn.edu.tdtu.springcommerce.service.impl;

import vn.edu.tdtu.springcommerce.model.Role;
import vn.edu.tdtu.springcommerce.repository.RoleRepository;
import vn.edu.tdtu.springcommerce.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepository roleRepository;

    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }

    public Optional<Role> findRoleById(int id){
        return roleRepository.findById(id);
    }

}
