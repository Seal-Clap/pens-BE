package com.example.pens.controller;

<<<<<<< HEAD:src/main/java/com/example/pens/controller/LoginController.java
import com.example.pens.domain.request.UserDTO;
=======
import com.example.pens.domain.auth.Token;
import com.example.pens.domain.request.UserRequest;
>>>>>>> user-api:src/main/java/com/example/pens/controller/UsersController.java
import com.example.pens.service.UserService;
import com.example.pens.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UsersController {
    private final UserService userService;

    @PostMapping("/login")
<<<<<<< HEAD:src/main/java/com/example/pens/controller/LoginController.java
    public ResponseEntity showValidation(@RequestBody UserDTO request) {
        return userService.validationLogin(request);
=======
    public ResponseEntity login(@Valid @RequestBody UserRequest userRequest) {
        return userService.login(userRequest);
>>>>>>> user-api:src/main/java/com/example/pens/controller/UsersController.java
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserRequest request) {
        return userService.register(request);
    }

    @PostMapping("/identity")
    public ResponseEntity identify() { return userService.identify(); }
}
