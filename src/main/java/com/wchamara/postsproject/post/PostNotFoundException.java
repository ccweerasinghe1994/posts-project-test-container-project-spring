package com.wchamara.postsproject.post;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Post not found")
public class PostNotFoundException extends RuntimeException {

}
