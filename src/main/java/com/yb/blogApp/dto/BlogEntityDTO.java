package com.yb.blogApp.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class BlogEntityDTO {
	private String topic;
	private List<String> images;
	private String blogContent;
	private Date createdAt;

}
