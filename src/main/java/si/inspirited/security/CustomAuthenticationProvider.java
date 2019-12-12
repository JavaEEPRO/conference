package si.inspirited.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import si.inspirited.persistence.model.Participant;
import si.inspirited.persistence.repo.ParticipantRepository;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    private ParticipantRepository participantRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Participant participant = new Participant();
        if (participantRepository.findByLogin(authentication.getName()).isPresent()) {
            participant = participantRepository.findByLogin(authentication.getName()).get();
        }
        if ((participant.isNew())) {
            throw new BadCredentialsException("Invalid login or password");
        }
        final Authentication result = super.authenticate(authentication);
        return new UsernamePasswordAuthenticationToken(participant, result.getCredentials(), result.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
