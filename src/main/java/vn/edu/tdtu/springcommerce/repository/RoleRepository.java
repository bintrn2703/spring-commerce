package vn.edu.tdtu.springcommerce.repository;

import vn.edu.tdtu.springcommerce.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

}
