package ca.dal.cs.wanderer.controllers;

import ca.dal.cs.wanderer.handler.OAuthSuccessHandler;
import ca.dal.cs.wanderer.models.User;
import ca.dal.cs.wanderer.services.CustomOidcUserService;
import ca.dal.cs.wanderer.services.UserProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserProfileController.class)
public class UserProfileControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private UserProfileService userProfileService;

    @MockBean
    private CustomOidcUserService oidcUserService;

    @MockBean
    private OAuthSuccessHandler successHandler;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        setAuthentication();
    }

    @Test
    public void testProfileController() throws Exception {
        User expectedUser = getUser();
        when(userProfileService.fetchByEmail(anyString())).thenReturn(expectedUser);

        mockMvc.perform(get("/api/v1/wanderer/user/getDetails")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("payload.emailId").value("test@gmail.com"))
                .andExpect(jsonPath("payload.lastName").value("Test last name"))
                .andExpect(jsonPath("payload.firstName").value("Test First name"));

        verify(userProfileService, times(1)).fetchByEmail(anyString());
    }

    private User getUser() {
        User user = new User();
        user.setFirstName("Test First name");
        user.setLastName("Test last name");
        user.setId(10);
        user.setEmailId("test@gmail.com");
        return user;
    }

    private void setAuthentication() {
        OidcUser oidcUser = new DefaultOidcUser(
                AuthorityUtils.createAuthorityList("SCOPE_message:read"),
                OidcIdToken.withTokenValue("id-token").claim("user_name", "foo_user")
                        .claim("email", "test@gmail.com").build(),
                "user_name");

        SecurityContextHolder.getContext().setAuthentication(new OAuth2AuthenticationToken(oidcUser, AuthorityUtils.createAuthorityList("SCOPE_message:read"), "clientId"));
    }
}