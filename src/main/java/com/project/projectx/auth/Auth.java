package com.project.projectx.auth;

import com.project.projectx.auth.model.UserDetail;
import com.project.projectx.auth.model.request.LoginRequest;
import com.project.projectx.auth.model.request.RegisterRequest;
import com.project.projectx.auth.model.response.LoginResponse;
import com.project.projectx.auth.model.response.RegisterResponse;
import com.project.projectx.auth.service.JwtService;
import com.project.projectx.auth.service.UserService;
import com.project.projectx.core.model.ApiResponse;
import com.project.projectx.exceptionHandler.exception.AuthenticatedFailedException;
import com.project.projectx.exceptionHandler.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class Auth {
    private final UserService userService;
    private final JwtService jwtService;



    @PostMapping("/register")
    public ApiResponse<RegisterResponse> register(@RequestBody RegisterRequest registerRequest){
        return ApiResponse.success("User Register Successfully", userService.save(registerRequest));
    }


    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        if (!userService.existsByUsername(loginRequest.username()))
            throw new UserNotFoundException("User not found");
        UserDetail userDetail = (UserDetail) userService.loadUserByUsername(loginRequest.username());
        String token = null;
        if (userDetail == null) {
            throw new UserNotFoundException("User not found");
        }
        if (userService.checkUser(userDetail)){
            if (userService.checkPassword(loginRequest.password(), userDetail.getPassword())) {
                token = jwtService.generateToken(userDetail);
            }else {
                throw new AuthenticatedFailedException("Invalid username or password");
            }
        }



        return ApiResponse.success("Login is Successfully",new LoginResponse(null,token,userDetail.getName(),userDetail.getSurname()));
    }
}
