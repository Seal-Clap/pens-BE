package com.example.pens.repository;

import com.example.pens.domain.GroupFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupFileRepository extends JpaRepository<GroupFile, Integer> {
}
