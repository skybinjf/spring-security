/**
 * 
 */
package com.home.security.rbac.repository;

import com.home.security.rbac.domain.Admin;
import org.springframework.stereotype.Repository;

/**
 * @author zhailiang
 *
 */
@Repository
public interface AdminRepository extends HomeRepository<Admin> {

	Admin findByUsername(String username);

}
