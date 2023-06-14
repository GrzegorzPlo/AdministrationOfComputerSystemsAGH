package agh.frs.controller;


import agh.frs.model.User;
import agh.frs.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @CrossOrigin
    @GetMapping("HelloWorld")
    public String helloWorld(){
        return "Hi from Spring Boot";
    }

    @CrossOrigin
    @PostMapping("/api/user/createUser")
    public void createUser(@RequestBody String userName,@RequestBody String userPassword) {
        userService.createUser(userName, userPassword);
        System.out.println("User " + userName + "created :)");
    }
    @CrossOrigin
    @DeleteMapping("/api/user/deleteUser/{id}")
    public void deleteUser(@PathVariable long id) throws Exception {
        userService.deleteUser(id);
        System.out.println("User with id:" + id + " deleted :)");
    }


    @CrossOrigin
    @GetMapping("api/user/findUser/{id}")
    public User getUserById(@PathVariable long id) throws Exception {
        return userService.findById(id);
    }
    @CrossOrigin
    @GetMapping("/api/user/getUserList")
    public List<User> getUsers() {
        return userService.findAll();
    }
    @CrossOrigin
    @GetMapping("/api/user/findByName/{userName}")
    public Optional<User> findByName(@PathVariable String userName){return userService.findByName(userName);}


}
