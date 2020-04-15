package com.github.ivanjermakov.lettercore.file.repository;

import com.github.ivanjermakov.lettercore.file.entity.Document;
import org.springframework.data.repository.CrudRepository;

public interface DocumentRepository extends CrudRepository<Document, Long> {

}
