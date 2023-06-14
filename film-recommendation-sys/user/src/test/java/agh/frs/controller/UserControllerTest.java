//package agh.frs.controller;
//
//import agh.frs.model.User;
//import agh.frs.service.UserService;
//import org.junit.Ignore;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//
//
//import static org.hibernate.id.IdentifierGeneratorHelper.get;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
//@Ignore
//@ExtendWith(SpringExtension.class)
//public class UserControllerTest {
//    @MockBean
//    UserService userService;
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @Ignore
//    @Test
//    public void testGetById() throws Exception {
//        User user = new User("Daniel","123");
//
//        when(userService.findById(1)).thenReturn(user);
//
//
//        verify(userService).findById(1);
//
//    }
//}
