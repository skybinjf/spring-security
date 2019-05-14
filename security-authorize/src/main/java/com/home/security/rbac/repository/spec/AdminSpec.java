/**
 * 
 */
package com.home.security.rbac.repository.spec;

import com.home.security.rbac.domain.Admin;
import com.home.security.rbac.dto.AdminCondition;
import com.home.security.rbac.repository.support.HomeSpecification;
import com.home.security.rbac.repository.support.QueryWraper;

/**
 * @author zhailiang
 *
 */
public class AdminSpec extends HomeSpecification<Admin, AdminCondition> {

	public AdminSpec(AdminCondition condition) {
		super(condition);
	}

	@Override
	protected void addCondition(QueryWraper<Admin> queryWraper) {
		addLikeCondition(queryWraper, "username");
	}

}
