/**
 * 
 */
package com.home.security.rbac.repository;

import com.home.security.rbac.domain.Resource;
import org.springframework.stereotype.Repository;

/**
 * @author zhailiang
 *
 */
@Repository
public interface ResourceRepository extends HomeRepository<Resource> {

	Resource findByName(String name);

}
