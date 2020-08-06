package com.cramstack.cookies.Controller;

import com.cramstack.cookies.Entity.request.UserRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
public class CookieController {

    @GetMapping("/cookieByValue")
    public String getCookieByName(@CookieValue(value = "session_id", defaultValue = "shohag") String cookieValue){
        return "Cookie Value is: " + cookieValue;
    }

    @GetMapping("/cookieByRequest")
    public String getCookieByRequest(HttpServletRequest request){
        Cookie name = WebUtils.getCookie(request, "session_id");
        if (name != null) {
            return "Cookie value is: " + name.getValue();
        } else {
            return "Not found!";
        }
    }

    @GetMapping("/cookies")
    public String getAllCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            return Arrays.stream(cookies)
                    .map(c -> c.getName() + "=" + c.getValue()).collect(Collectors.joining(", "));
        }
        return "No cookies";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequest user, HttpServletResponse response){
        String userName = user.getName();
        Double randId = Math.random();

        Cookie cookie = new Cookie("session_id", userName + randId.toString());

        // setting properties
        cookie.setMaxAge(7 * 24 * 3600); // expiry = 7days
        //cookie.setSecure(true); // set cookies in encrypted format
        cookie.setHttpOnly(true); // prevent cross-site scripting (XSS) attacks and are not accessible via JavaScript's Document.cookie API
        cookie.setPath("/"); // https://stackoverflow.com/questions/576535/cookie-path-and-its-accessibility-to-subfolder-pages

        response.addCookie(cookie);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/cookie")
    public ResponseEntity<?> deleteCookie(HttpServletResponse response){
        Cookie cookie = new Cookie("session_id", null);

        // setting properties
        cookie.setMaxAge(0);
        //cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);
        return ResponseEntity.noContent().build();
    }
}
