package com.sysmei.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sysmei.model.DocumentUrl;

public interface DocumentsRepository extends JpaRepository<DocumentUrl, Long> {

	List<DocumentUrl> findAll();
		
}
