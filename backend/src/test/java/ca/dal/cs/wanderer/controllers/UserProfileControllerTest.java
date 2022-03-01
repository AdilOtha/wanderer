package ca.dal.cs.wanderer.controllers;

import ca.dal.cs.wanderer.models.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.regex.Matcher;

@RunWith(SpringJUnit4ClassRunner.class)
class UserProfileControllerTest {

    private MockMvc mockMvc;
    @InjectMocks
    private UserProfileController profileController;

    @InjectMocks
    private User user;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(profileController).build();
    }

/*    @Test
    public void testProfileController() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/getDetails")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("");
    }*/
}