/**
 * 
 */
package com.home.code;

import org.springframework.web.context.request.ServletWebRequest;

import com.home.security.core.validate.code.ValidateCodeGenerator;
import com.home.security.core.validate.code.image.ImageCode;

/**
 * @author zhailiang
 *
 */
//@Component("imageCodeGenerator")
public class DemoImageCodeGenerator implements ValidateCodeGenerator {

	/* (non-Javadoc)
	 * @see ValidateCodeGenerator#generate(org.springframework.web.context.request.ServletWebRequest)
	 */
	@Override
	public ImageCode generate(ServletWebRequest request) {
		System.out.println("更高级的图形验证码生成代码");
		return null;
	}

}
