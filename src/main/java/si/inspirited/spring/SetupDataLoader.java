package si.inspirited.spring;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import si.inspirited.persistence.model.Conference;
import si.inspirited.persistence.model.Participant;
import si.inspirited.persistence.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import si.inspirited.persistence.model.Room;
import si.inspirited.persistence.repo.ConferenceRepository;
import si.inspirited.persistence.repo.ParticipantRepository;
import si.inspirited.persistence.repo.RoleRepository;
import si.inspirited.persistence.repo.RoomRepository;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        //create initial roles
        final Role adminRole = createRoleIfNotFound("ROLE_ADMIN");
        final Role userRole = createRoleIfNotFound("ROLE_USER");

        // create initial participants
        Participant testParticipant = createParticipantIfNotFound("test",
                                                               "test",
                                                                "One Test Participant",
                                                                         LocalDate.now(),
                                                                         new ArrayList<>(),
                                                                         new ArrayList<>(Arrays.asList(adminRole, userRole)));

        //create initial conf. rooms
        Room devConsultingRoom1 = createRoomIfNotFound("dev consulting room 1",
                                                     "sec. floor",
                                                    20,
                                                              Collections.singletonList(testParticipant)
        );

        //create initial conferences
        createConferenceIfNotFound("dev conf. for discussing on how to make conf. system better 1",
                                         LocalDateTime.now(),
                                         Collections.singletonList(devConsultingRoom1)
                );
        alreadySetup = true;
    }

    @Transactional
    Role createRoleIfNotFound(final String name) {
        Role role = new Role();
        if (roleRepository.findByName(name).isPresent()) {
            roleRepository.findByName(name).get();
        }
        if (role.isNew()) {
            role = new Role(name);
        }
        role = roleRepository.save(role);
        return role;
    }

    @Transactional
    Participant createParticipantIfNotFound(final String login, final String password, final String fullName, final LocalDate birthDay, Collection<Room> conferenceRooms, final Collection<Role> roles) {
        Participant participant = new Participant();
        if (participantRepository.findByLogin(login).isPresent()) {
            participant = participantRepository.findByLogin(login).get();
        }
        if (participant.isNew()) {
            participant.setLogin(login);
            participant.setPassword(passwordEncoder.encode(password));
            participant.setFullName(fullName);
            participant.setBirthDay(birthDay);
            participant.setConferenceRooms(conferenceRooms);
            participant.setRoles(roles);
            participant.setEnabled(true);
            participant = participantRepository.save(participant);
        }
        return participant;
    }


    @Transactional
    Conference createConferenceIfNotFound(final String name, final LocalDateTime dateTime, final Collection<Room> rooms) {
        Conference conference = new Conference();
        if (conferenceRepository.findByName(name).isPresent()) {
            conferenceRepository.findByName(name).get();
        }
        if (conference.isNew()) {
            conference.setName(name);
            conference.setDateTime(dateTime);
            conference.setRooms(rooms);
        }
        return conference;
    }

    @Transactional
    Room createRoomIfNotFound(final String name, final String location, final int maxPlaces, final Collection<Participant> participants) {
        Room room = new Room();
        if (roomRepository.findByName(name).isPresent()) {
            roomRepository.findByName(name).get();
        }
        if (room.isNew()) {
            room.setName(name);
            room.setLocation(location);
            room.setMaxPlaces(maxPlaces);
            room.setParticipants(participants);
            room.setChat(new ArrayList<>());
        }
        return room;
    }
}