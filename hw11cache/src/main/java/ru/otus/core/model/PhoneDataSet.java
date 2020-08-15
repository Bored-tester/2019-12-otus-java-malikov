package ru.otus.core.model;

import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "phones")
public class PhoneDataSet {

    @Id
    @GeneratedValue(generator = "sequence-generator-phones")
    @GenericGenerator(
            name = "sequence-generator-phones",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "phone_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "10"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Column(name = "id")
    private long id;

    @Column(name = "number")
    private String number;

    public PhoneDataSet(String number) {
        this.number = number;
    }

    public PhoneDataSet() {
    }

    @Override
    public String toString() {
        return number;
    }
}
