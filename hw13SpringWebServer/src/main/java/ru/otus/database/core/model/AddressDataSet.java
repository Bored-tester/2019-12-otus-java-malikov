package ru.otus.database.core.model;

import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;


@Getter
@Entity
@Table(name = "userAddresses")
public class AddressDataSet {
    @Id
    @GeneratedValue(generator = "sequence-generator-addresses")
    @GenericGenerator(
            name = "sequence-generator-addresses",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "address_sequence"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    @Column(name = "id")
    private long id;

    @Column(name = "street")
    private String street;

    public AddressDataSet(String street) {
        this.street = street;
    }

    public AddressDataSet() {
    }

    @Override
    public String toString() {
        return street;
    }
}
