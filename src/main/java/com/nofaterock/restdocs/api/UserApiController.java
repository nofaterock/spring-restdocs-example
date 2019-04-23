package com.nofaterock.restdocs.api;

import com.nofaterock.restdocs.user.domain.User;
import com.nofaterock.restdocs.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 한승룡
 * @since 2019-03-12
 */
@RestController
@RequestMapping("/api")
public class UserApiController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public User usersAdd(@RequestBody User user) {
		return userService.add(user.getName(), user.getNickname());
	}

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public List<User> users() {
		return userService.getAll();
	}

	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
	public User usersById(@PathVariable Integer id) {
		return userService.get(id);
	}

}
