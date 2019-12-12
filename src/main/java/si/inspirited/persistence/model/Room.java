package si.inspirited.persistence.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Collection;
import java.util.ArrayList;

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

    private ArrayList<String> chat;

    @ManyToOne
    private Conference conference;

    public boolean isNew() {
        return this.id == null;
    }
}