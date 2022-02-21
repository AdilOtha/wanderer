package ca.dal.cs.wanderer.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
public class HelloController {
    @ApiOperation(value = "Get a book by its id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Found the book"),
            @ApiResponse(code = 400, message = "Invalid id supplied"),
            @ApiResponse(code = 404, message = "Book not found")})


    @GetMapping("hello")
    public String getHelloWord() {
        return "Hello World";
    }
}
