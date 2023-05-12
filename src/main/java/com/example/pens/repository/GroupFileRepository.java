package com.example.pens.repository;

import com.example.pens.domain.Group;
import com.example.pens.domain.GroupFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupFileRepository extends JpaRepository<GroupFile, Integer> {
    List<GroupFile> findGroupFileByGroup(Group group);
}
