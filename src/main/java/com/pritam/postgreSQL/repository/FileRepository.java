package com.pritam.postgreSQL.repository;

import com.pritam.postgreSQL.model.FileObj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileObj, Long> {
}
