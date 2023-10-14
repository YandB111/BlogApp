package com.yb.blogApp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yb.blogApp.entity.BlogEntity;

@Repository
public interface BlogServiceRepo extends JpaRepository<BlogEntity, Long>{


}
