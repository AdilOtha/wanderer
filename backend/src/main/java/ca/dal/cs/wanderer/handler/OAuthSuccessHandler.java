package ca.dal.cs.wanderer.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

    RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        DefaultOidcUser principal = (DefaultOidcUser) authentication.getPrincipal();
        redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "http://localhost:4200/login?token=" + principal.getIdToken().getTokenValue());
    }
}
