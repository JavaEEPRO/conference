package si.inspirited.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import si.inspirited.persistence.model.Participant;
import si.inspirited.persistence.repo.ParticipantRepository;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class CustomRememberMeService extends PersistentTokenBasedRememberMeServices {

    @Autowired
    private ParticipantRepository participantRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
    private PersistentTokenRepository tokenRepository;
    private String key;

    public CustomRememberMeService(String key, UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository) {
        super(key, userDetailsService, tokenRepository);
        this.tokenRepository = tokenRepository;
        this.key = key;
    }

    @Override
    protected void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {
        String login = ((Participant) successfulAuthentication.getPrincipal()).getLogin();
        logger.debug("Creating new persistent login for participant " + login);
        PersistentRememberMeToken persistentToken = new PersistentRememberMeToken(login, generateSeriesData(), generateTokenData(), new Date());
        try {
            tokenRepository.createNewToken(persistentToken);
            addCookie(persistentToken, request, response);
        } catch (Exception e) {
            logger.error("Failed to save persistent token ", e);
        }
    }

    @Override
    protected Authentication createSuccessfulAuthentication(HttpServletRequest request, UserDetails participant) {
        Participant authenticatedParticipant = new Participant();
        if (participantRepository.findByLogin(participant.getUsername()) != null) {
            participantRepository.findByLogin(participant.getUsername());
        }
        RememberMeAuthenticationToken authenticationToken = new RememberMeAuthenticationToken(key, authenticatedParticipant, authoritiesMapper.mapAuthorities(participant.getAuthorities()));
        authenticationToken.setDetails(authenticationDetailsSource.buildDetails(request));
        return authenticationToken;
    }

    private void addCookie(PersistentRememberMeToken token, HttpServletRequest request, HttpServletResponse response) {
        setCookie(new String[] { token.getSeries(), token.getTokenValue() }, getTokenValiditySeconds(), request, response);
    }
}