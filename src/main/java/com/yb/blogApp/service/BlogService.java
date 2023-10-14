package com.yb.blogApp.service;

import java.util.List;

import com.yb.blogApp.dto.BlogEntityDTO;
import com.yb.blogApp.entity.BlogEntity;

public interface BlogService {

	void createBlog(BlogEntity blog);

	BlogEntity getBlogById(Long id);

	boolean updateBlog(Long id, BlogEntityDTO updatedBlogDTO);

	boolean deleteBlog(Long id);

	List<BlogEntity> getAllBlogs();


}
