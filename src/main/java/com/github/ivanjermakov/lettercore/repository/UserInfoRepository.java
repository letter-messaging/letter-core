package com.github.ivanjermakov.lettercore.repository;

import com.github.ivanjermakov.lettercore.entity.User;
import com.github.ivanjermakov.lettercore.entity.UserInfo;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {

	Optional<UserInfo> findByUser(User user);

}
