package si.inspirited.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import si.inspirited.persistence.model.Participant;
import si.inspirited.persistence.model.Role;
import si.inspirited.persistence.repo.ParticipantRepository;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private HttpServletRequest request;

    public CustomUserDetailsService() {
        super();
    }

    @Override
    public UserDetails loadUserByUsername(final String login) throws UsernameNotFoundException {
        final String ip = getClientIP();
        if (loginAttemptService.isBlocked(ip)) {
            throw new RuntimeException("blocked");
        }

        try {
            final Participant participant = new Participant();
            if (participantRepository.findByLogin(login).isPresent()) {
                participantRepository.findByLogin(login).get();
            }
            if (participant.isNew()) {
                throw new UsernameNotFoundException("No user found with username: " + login);
            }

            return new User(participant.getLogin(), participant.getPassword(), participant.isEnabled(), true, true, true, getAuthorities(participant.getRoles()));
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final Collection<? extends GrantedAuthority> getAuthorities(final Collection<Role> roles) {
        return getGrantedAuthorities(new ArrayList<>(roles));
    }

    private final List<GrantedAuthority> getGrantedAuthorities(final List<Role> privileges) {
        final List<GrantedAuthority> authorities = new ArrayList<>();
        for (final Role privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege.getAuthority()));
        }
        return authorities;
    }

    private final String getClientIP() {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}