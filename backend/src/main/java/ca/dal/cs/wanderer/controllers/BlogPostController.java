package ca.dal.cs.wanderer.controllers;

import ca.dal.cs.wanderer.exception.GenericResponse;
import ca.dal.cs.wanderer.exception.category.EmailNotFound;
import ca.dal.cs.wanderer.exception.category.PrincipalNotFound;
import ca.dal.cs.wanderer.exception.category.blogexception.*;
import ca.dal.cs.wanderer.exception.category.pinexception.CommentNotFound;
import ca.dal.cs.wanderer.models.Blog;
import ca.dal.cs.wanderer.models.BlogComment;
import ca.dal.cs.wanderer.models.User;
import ca.dal.cs.wanderer.services.BlogPostService;
import ca.dal.cs.wanderer.services.UserProfileService;
import ca.dal.cs.wanderer.util.ErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/blogs")
public class BlogPostController {

    @Autowired
    BlogPostService blogPostService;

    @Autowired
    UserProfileService userProfileService;

    // accept a blog post and blogImage as a multipart file
    @PostMapping("/addBlog")
    public ResponseEntity<GenericResponse<Blog>> createBlogs(@AuthenticationPrincipal OidcUser principal, @RequestPart("blog") Blog blog, @RequestPart(value = "blogImage", required = false) MultipartFile blogImage) throws Exception {

        User user = getUser(principal);

        if(user==null) {
            throw new PrincipalNotFound(ErrorMessages.PRINCIPAL_NOT_FOUND);
        }

        if(blog == null) {
            throw new BlogNotFound(ErrorMessages.BLOG_NOT_FOUND);
        }

        Blog newBlog = blogPostService.createBlog(user, blog, blogImage);
        return ResponseEntity.ok(new GenericResponse<>(true, "Blog created successfully", newBlog));
    }

    @GetMapping("/getAllBlogs")
    public ResponseEntity<GenericResponse<List<Blog>>> showAllBlogs(@AuthenticationPrincipal OidcUser principal) throws Exception {
        getUser(principal);

        List<Blog> blogs = blogPostService.showAllBlogs();
        return ResponseEntity.ok(new GenericResponse<>(true, "All Blogs are successfully retrieved", blogs));
    }

    @GetMapping("/getBlogById")
    public ResponseEntity<GenericResponse<Blog>> showSingleBlog(@AuthenticationPrincipal OidcUser principal, @RequestParam Integer blogId) {
        getUser(principal);

        if(blogId <= 0) {
            throw new InvalidBlogId(ErrorMessages.INVALID_BLOG_ID);
        }
        Blog blog = blogPostService.showSingleBlog(blogId);

        if(blog == null) {
            throw new BlogNotFound(ErrorMessages.INVALID_BLOG_ID);
        }
        return ResponseEntity.ok(new GenericResponse<>(true, "Blog is successfully retrieved", blog));
    }

    @DeleteMapping("/deleteBlogById")
    public ResponseEntity<GenericResponse<Boolean>> deleteBlogById(@AuthenticationPrincipal OidcUser principal, @RequestParam Integer blogId) throws Exception {
        User user = getUser(principal);

        if (blogId == null || blogId <= 0) {
            throw new InvalidBlogId(ErrorMessages.INVALID_BLOG_ID);
        }

        blogPostService.deleteBlogPost(blogId, user);

        return ResponseEntity.ok(new GenericResponse<>(true, "Blog deleted successfully", true));
    }

    @GetMapping("/getComments")
    public ResponseEntity<GenericResponse<List<BlogComment>>> getComments(@AuthenticationPrincipal OidcUser principal, @RequestParam Integer blogId) throws Exception {
        getUser(principal);

        if (blogId <= 0) {
            throw new InvalidBlogId(ErrorMessages.INVALID_BLOG_ID);
        }
        List<BlogComment> comments = blogPostService.getComments(blogId);

        return ResponseEntity.ok(new GenericResponse<>(true, "Comments retrieved successfully", comments));
    }

    @PostMapping("/addComments")
    public ResponseEntity<GenericResponse<BlogComment>> addComments(@AuthenticationPrincipal OidcUser principal, @RequestParam Integer blogId,
                                                                   @RequestBody String comment) throws Exception {

        User user = getUser(principal);

        if (blogId <= 0) {
            throw new InvalidBlogId(ErrorMessages.INVALID_BLOG_ID);
        }

        Blog blog = blogPostService.showSingleBlog(blogId);
        if (blog == null) {
            throw new BlogNotFound(ErrorMessages.BLOG_NOT_FOUND);
        }

        if(comment == null) {
            throw new CommentNotFound(ErrorMessages.COMMENT_NOT_FOUND);
        }

        BlogComment blogComment = blogPostService.addComment(user, blog, comment);
        return ResponseEntity.ok(new GenericResponse<>(true, "Comment added successfully", blogComment));
    }

    @GetMapping("/getBlogsByUser")
    public ResponseEntity<GenericResponse<List<Blog>>> getBlogByUser(@AuthenticationPrincipal OidcUser principal) throws Exception {

        User user = getUser(principal);

        List<Blog> blogs = blogPostService.getBlogsByUser(user);

        return ResponseEntity.ok(new GenericResponse<>(true, "Blog retrieved successfully", blogs));
    }

    private User getUser(OidcUser principal) {
        if (principal == null) {
            throw new PrincipalNotFound(ErrorMessages.PRINCIPAL_NOT_FOUND);
        }
        String email = principal.getEmail();
        if (email == null) {
            throw new EmailNotFound(ErrorMessages.EMAIL_NOT_FOUND);
        }
        return userProfileService.fetchByEmail(email);
    }
}
