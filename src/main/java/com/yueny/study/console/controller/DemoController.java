package com.yueny.study.console.controller;

import com.yueny.study.console.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author yueny09 <deep_blue_yang@163.com>
 *
 * @DATE 2016年2月16日 下午8:23:11
 *
 */
@RestController
// 跨域处理，加下类上则对所有方法生效
@CrossOrigin(origins = "*", maxAge = 3600)
public class DemoController {
	@Autowired
	private IUserService userService;

	/**
	 *
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public List<String> bar(HttpServletRequest request) {
		String token = request.getHeader("token");

		return userService.getList();
	}

}
