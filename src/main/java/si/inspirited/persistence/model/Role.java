package si.inspirited.persistence.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
public class Role implements GrantedAuthority {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "roles")
    private Collection<Participant> participants;

    @Override
    public String getAuthority() {
        return name;
    }

    public Role() {
        super();
    }

    public Role(final String name) {
        super();
        this.name = name;
    }

    public boolean isNew() {
        return this.id == null;
    }
}
