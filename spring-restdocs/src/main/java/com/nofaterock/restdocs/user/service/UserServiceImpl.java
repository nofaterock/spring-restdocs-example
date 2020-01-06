package com.nofaterock.restdocs.user.service;

import com.nofaterock.restdocs.user.domain.User;
import com.nofaterock.restdocs.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 한승룡
 * @since 2019-02-14
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Resource
	private UserRepository userRepository;

	@Override
	public User add(String name, String nickname) {
		return userRepository.save(new User(name, nickname));
	}

	@Override
	public User get(Integer id) {
		return userRepository.getOne(id);
	}

	@Override
	public List<User> getAll() {
		return userRepository.findAll();
	}

}
