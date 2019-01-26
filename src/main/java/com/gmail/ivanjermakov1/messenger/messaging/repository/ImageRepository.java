package com.gmail.ivanjermakov1.messenger.messaging.repository;

import com.gmail.ivanjermakov1.messenger.messaging.entity.Image;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends CrudRepository<Image, Long> {
	
	Image findByPath(String path);
	
}
