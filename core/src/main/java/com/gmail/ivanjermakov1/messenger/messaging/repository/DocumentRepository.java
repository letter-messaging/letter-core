package com.gmail.ivanjermakov1.messenger.messaging.repository;

import com.gmail.ivanjermakov1.messenger.messaging.entity.Document;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DocumentRepository extends CrudRepository<Document, Long> {
	
	Optional<Document> findByPath(String path);
	
}
