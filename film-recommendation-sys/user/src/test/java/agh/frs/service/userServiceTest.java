//package agh.frs.service;
//
//
//import agh.frs.model.User;
//import agh.frs.repositiory.UserRepository;
//import org.junit.Before;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import java.util.ArrayList;
//
//
//@ExtendWith(MockitoExtension.class)
//public class userServiceTest {
//
//        @Mock
//        UserRepository userRepository;
//        @Mock
//        UserService userService;
//
//    @Before
//    public void setUp() {
//        User testUser = new User("Daniel","123");
//    }
//
//
//    @Test
//    public void getUserByName() throws Exception {
//
//        User user = new User("Daniel","123");
//        userService.createUser("Daniel","123");
//
//
//        Assertions.assertNotNull(userService.findByName("Daniel"));
//    }
//
//
//    @Test
//    public void getUserById() throws Exception {
//
//        User user = new User("Daniel","123");
//        user.setId(1L);
//        userService.createUser(user);
//
//        User foundUser = userService.findById(1);
//
//
//
//        Assertions.assertNotNull(user);
//    }
//
//    @Test
//    public void getAllUsers() throws Exception {
//
//        User user1 = new User("Daniel","123");
//        User user2 = new User("Michal","123");
//        User user3 = new User("Grzegorz","123");
//
//        userService.createUser("Daniel","123");
//        userService.createUser("Michal","123");
//        userService.createUser("Grzegorz","123");
//
//
//        ArrayList<User> userList = new ArrayList<User>(userService.findAll());
//
//        Assertions.assertNotNull(userService.findAll());
//    }
//
//
//
//}
