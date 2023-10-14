package com.yb.blogApp.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.yb.blogApp.dto.BlogEntityDTO;
import com.yb.blogApp.entity.BlogEntity;
import com.yb.blogApp.response.GenericResponse;
import com.yb.blogApp.service.impl.BlogServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "BLOG_ADD_USER")
public class BlogPostController extends ApiController {
	protected Logger logger = LoggerFactory.getLogger(BlogPostController.class);

	@Autowired
	private BlogServiceImpl blogService;

	// Save Method

@ApiOperation(value = "Create a Blog", notes = "Creates a new blog post.")
@ApiResponses(value = {
    @ApiResponse(code = 200, message = "Blog created successfully", response = String.class),
    @ApiResponse(code = 409, message = "Conflict: Error creating blog")
})
@PostMapping(value = BlOG_ADD_USER, produces = "application/json")
public ResponseEntity<GenericResponse> createBlog(@RequestBody BlogEntityDTO blogDTO) {
    try {
        BlogEntity blog = new BlogEntity();
        blog.setTopic(blogDTO.getTopic());
        blog.setImages(blogDTO.getImages());
        blog.setBlogContent(blogDTO.getBlogContent());
        blog.setCreatedAt(blogDTO.getCreatedAt());

        blogService.createBlog(blog);

        // Assuming you have a GenericResponse constructor that takes the necessary parameters
        GenericResponse response = new GenericResponse(blogDTO, "Blog created successfully", HttpStatus.OK);

        return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
        // Handle the exception and create a GenericResponse for the error
        GenericResponse errorResponse = new GenericResponse(null, "Error creating blog.", HttpStatus.CONFLICT);

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}


	// findbyId of Blog
	@CrossOrigin
	@GetMapping(value = BlOG_FIND_BY_ID,produces = "application/json")
	public ResponseEntity<BlogEntity> getBlogById(@PathVariable Long id) {
		BlogEntity blog = blogService.getBlogById(id);
		if (blog != null) {
			return ResponseEntity.ok(blog);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// update Blog
	@CrossOrigin
	@PutMapping(value = BlOG_UPDATE_BY_ID, produces = "application/json")
	public ResponseEntity<GenericResponse> updateBlog(@PathVariable Long id, @RequestBody BlogEntityDTO updatedBlogDTO) {
	    boolean updated = blogService.updateBlog(id, updatedBlogDTO);
	    if (updated) {
	        // Assuming you have a GenericResponse constructor that takes the necessary parameters
	        GenericResponse response = new GenericResponse("Blog updated successfully", null, HttpStatus.OK);

	        return new ResponseEntity<>(response, HttpStatus.OK);
	    } else {
	        // Create a GenericResponse for the not found case
	        GenericResponse<String> notFoundResponse = new GenericResponse<>(null, "Blog not found", HttpStatus.NOT_FOUND);

	        return new ResponseEntity<>(notFoundResponse, HttpStatus.NOT_FOUND);
	    }
	}


	// delete Blog
	@CrossOrigin
	@DeleteMapping(value = DELETE_BY_ID, produces = "application/json")
	public ResponseEntity<GenericResponse> deleteBlog(@PathVariable Long id) {
	    boolean deleted = blogService.deleteBlog(id);
	    if (deleted) {
	        // Assuming you have a GenericResponse constructor that takes the necessary parameters
	        GenericResponse response = new GenericResponse("Blog deleted successfully", null, HttpStatus.OK);

	        return new ResponseEntity<>(response, HttpStatus.OK);
	    } else {
	        // Create a GenericResponse for the not found case
	        GenericResponse<String> notFoundResponse = new GenericResponse<>(null, "Blog not found", HttpStatus.NOT_FOUND);

	        return new ResponseEntity<>(notFoundResponse, HttpStatus.NOT_FOUND);
	    }
	}


	// get all Blogs by User
	@CrossOrigin
	@GetMapping(value=BLOG_GET_ALL,produces="application/json")
	public ResponseEntity<List<BlogEntity>> getAllBlogs() {
		List<BlogEntity> blogs = blogService.getAllBlogs();
		return ResponseEntity.ok(blogs);
	}

}
