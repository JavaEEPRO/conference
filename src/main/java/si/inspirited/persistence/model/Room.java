package si.inspirited.persistence.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Room {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    String name;

    String location;

    private int maxPlaces;

    @ManyToMany
    private Collection<Participant> participants;

    private List<String> chat;
}