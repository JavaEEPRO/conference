package si.inspirited.persistence.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Data
@Entity
@NoArgsConstructor
public class Conference {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    @Column(length = 150)
    private String name;

    private LocalDateTime dateTime;

    @OneToMany
    private Collection<Room> rooms;

    public boolean isNew() {
        return this.id == null;
    }
}