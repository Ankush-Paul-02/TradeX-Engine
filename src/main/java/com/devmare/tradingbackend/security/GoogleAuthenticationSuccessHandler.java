package com.devmare.tradingbackend.security;

import com.devmare.tradingbackend.data.entity.User;
import com.devmare.tradingbackend.data.enums.Provider;
import com.devmare.tradingbackend.data.enums.UserRole;
import com.devmare.tradingbackend.data.exception.UserInfoException;
import com.devmare.tradingbackend.data.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class GoogleAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        log.info("GoogleAuthenticationSuccessHandler.onAuthenticationSuccess");

        // Identify the provider
        OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken) authentication;
        String authorizedClientRegistrationId = authenticationToken.getAuthorizedClientRegistrationId();
        log.info("Authorized client registration id: {}", authorizedClientRegistrationId);

        DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
        String email = principal.getAttribute("email");
        log.info("Email: {}", email);
        String name = principal.getAttribute("name");
        log.info("Name: {}", name);

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (authorizedClientRegistrationId.equalsIgnoreCase("google")) {
            log.info("Google user authenticated");
            if (userOptional.isPresent()) {
                new DefaultRedirectStrategy().sendRedirect(request, response, "/api/v1/user/profile");
            } else {
                User newUser = User.builder()
                        .fullname(name)
                        .email(email)
                        .role(UserRole.ROLE_CUSTOMER)
                        .provider(Provider.GOOGLE)
                        .build();
                userRepository.save(newUser);
                new DefaultRedirectStrategy().sendRedirect(request, response, "/api/v1/user/profile");
            }
        } else {
            throw new UserInfoException("Unknown provider!");
        }
    }
}
