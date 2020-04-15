package com.github.ivanjermakov.lettercore.user.repository;

import com.github.ivanjermakov.lettercore.user.entity.User;
import com.github.ivanjermakov.lettercore.user.entity.UserInfo;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {

	Optional<UserInfo> findByUser(User user);

}
