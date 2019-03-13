package com.nofaterock.restdocs.user.service;

import com.nofaterock.restdocs.user.domain.User;

import java.util.List;

/**
 * @author 한승룡
 * @since 2019-02-14
 */
public interface UserService {

	User add(String name, String nickname);

	User get(Integer id);

	List<User> getAll();

}
