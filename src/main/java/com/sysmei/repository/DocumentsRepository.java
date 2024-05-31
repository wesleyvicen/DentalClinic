package com.sysmei.repository;


import com.sysmei.model.DocumentUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentsRepository extends JpaRepository<DocumentUrl, Long> {

  List<DocumentUrl> findAll();

}
