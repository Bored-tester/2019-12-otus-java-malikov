package ru.otus.database.core.model;

import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import ru.otus.database.core.enums.UserRole;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(generator = "sequence-generator-users")
    @GenericGenerator(
            name = "sequence-generator-users",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "user_sequence"),
                    @Parameter(name = "initial_value", value = "100"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "login", unique = true)
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private UserRole role;

    @OneToOne(targetEntity = AddressDataSet.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private AddressDataSet address;

    @OneToMany(targetEntity = PhoneDataSet.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "phone_id", referencedColumnName = "id")
    private List<PhoneDataSet> phones;

    public User() {
    }

    public User(String name, String login, String password, UserRole userRole, AddressDataSet address, List<PhoneDataSet> phones) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.role = userRole;
        this.address = address;
        this.phones = phones;
    }

    @Override
    public String toString() {
        return String.format("User:\nid %d\nname %s\naddress %s\nphones %s",
                id,
                name,
                address,
                phones.stream()
                        .map(Objects::toString)
                        .collect(Collectors.joining(", ")));
    }
}
