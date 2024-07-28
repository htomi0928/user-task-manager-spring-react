package com.bdo.digital.user_task_service.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Represents a user entity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_user")
public class User {

    /**
     * Unique identifier for the user, auto generated.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    private String name;
    private String email;

    @Embedded
    private Address address;

    /**
     * Soft delete flag
     */
    private boolean deleted = false;

    private String password;

    /**
     * List of tasks. This relationship is managed by the 'user' field in the Task entity.
     * CascadeType.ALL ensures that operations such as persist, merge, and remove are cascaded to the tasks.
     * orphanRemoval = true ensures that tasks removed from the list are also deleted from the database.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Task> tasks;
}
