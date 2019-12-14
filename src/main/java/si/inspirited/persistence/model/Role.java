package si.inspirited.persistence.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return id.equals(role.id) &&
                name.equals(role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
