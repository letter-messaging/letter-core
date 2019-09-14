package com.gmail.ivanjermakov1.messenger.repository;

import com.gmail.ivanjermakov1.messenger.entity.User;
import com.gmail.ivanjermakov1.messenger.entity.UserInfo;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {

	Optional<UserInfo> findByUser(User user);

}
