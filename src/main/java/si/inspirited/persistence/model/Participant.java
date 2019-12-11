package si.inspirited.persistence.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collection;

@Data
@Entity
@NoArgsConstructor
public class Participant {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    private String login;

    @NotNull
    private String fullName;

    @NonNull
    private LocalDate birthDay;

    @ManyToMany
    private Collection<Room> conferenceRoom;
}
