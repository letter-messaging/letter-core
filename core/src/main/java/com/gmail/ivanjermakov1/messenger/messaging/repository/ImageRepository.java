package com.gmail.ivanjermakov1.messenger.messaging.repository;

import com.gmail.ivanjermakov1.messenger.messaging.entity.Image;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface ImageRepository extends CrudRepository<Image, Long> {
	
	Optional<Image> findByPath(String path);
	
}
