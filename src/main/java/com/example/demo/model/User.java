package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "db_user")
public class User {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private long id;

    @Column(name = "name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username")
    private String userName;

    @Column(name = "lang")
    private String lang;

    @Column(name = "gender")
    private String gender;

    @Column(name = "date")
    private String date;

    @Column(name = "region")
    private String region;

    @Column(name = "job")
    private String job;

    @Column(name = "seen_year")
    private String seenYear;

    @Column(name = "user_added", columnDefinition = "real default 0")
    private int userAdded;

    @Column(name = "user_is_channel")
    private boolean userIsChannel;

    @Column(name = "participate_program")
    private String participateProgram;

    @Column(name = "plus_definition")
    private String plusDefinition;
}
