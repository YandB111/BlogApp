package com.yb.blogApp.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yb.blogApp.dto.BlogEntityDTO;
import com.yb.blogApp.entity.BlogEntity;
import com.yb.blogApp.repo.BlogServiceRepo;
import com.yb.blogApp.service.BlogService;

@Service
public class BlogServiceImpl implements BlogService {
	@Autowired
	private BlogServiceRepo blogRepository;

	@Override
	public void createBlog(BlogEntity blog) {
		blogRepository.save(blog);

	}

	@Override
	public BlogEntity getBlogById(Long id) {
		return blogRepository.findById(id).orElse(null);
	}

	@Override
	public boolean updateBlog(Long id, BlogEntityDTO updatedBlogDTO) {
	    Optional<BlogEntity> optionalBlog = blogRepository.findById(id);
	    if (optionalBlog.isPresent()) {
	        BlogEntity blog = optionalBlog.get();
	        // Update the blog properties with the values from updatedBlogDTO
	        blog.setTopic(updatedBlogDTO.getTopic());
	        blog.setImages(updatedBlogDTO.getImages());
	        blog.setBlogContent(updatedBlogDTO.getBlogContent());
	        blog.setCreatedAt(updatedBlogDTO.getCreatedAt());

	        blogRepository.save(blog);
	        return true;
	    } else {
	        return false;
	    }
	}
	
	@Override
	public boolean deleteBlog(Long id) {
	    Optional<BlogEntity> optionalBlog = blogRepository.findById(id);
	    if (optionalBlog.isPresent()) {
	        blogRepository.deleteById(id);
	        return true;
	    } else {
	        return false;
	    }
	}
	
	@Override
	public List<BlogEntity> getAllBlogs() {
	    return blogRepository.findAll();
	}

	
	


}

