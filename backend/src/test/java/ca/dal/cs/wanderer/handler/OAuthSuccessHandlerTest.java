//package ca.dal.cs.wanderer.handler;
//
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.mock.web.MockHttpServletRequest;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.security.web.RedirectStrategy;
//
//import java.io.IOException;
//
//import static org.mockito.ArgumentMatchers.anyObject;
//import static org.mockito.Mockito.spy;
//
//class OAuthSuccessHandlerTest {
//
//    OAuthSuccessHandler oAuthSuccessHandler = new OAuthSuccessHandler();
//
//    @Test
//    void oauthSuccessTest() throws IOException {
//
//        RedirectStrategy redirectStrategy = spy(oAuthSuccessHandler.redirectStrategy);
//
//        Mockito.doNothing().when(redirectStrategy).sendRedirect(anyObject(), anyObject(), anyObject());
//
//        oAuthSuccessHandler.onAuthenticationSuccess(new MockHttpServletRequest(), new MockHttpServletResponse(), null);
//    }
//}