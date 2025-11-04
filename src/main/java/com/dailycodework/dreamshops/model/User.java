package com.dailycodework.dreamshops.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.springframework.data.redis.core.RedisHash;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User { // Список пользователей системы


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @NaturalId
    private String email;
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;

    @ManyToMany(fetch = FetchType.EAGER, cascade =
            {CascadeType.DETACH, // при сохранении пользователя автоматически добавляются новые роли
            CascadeType.MERGE, // При обновлении пользователя hibernate обновит роли
            CascadeType.PERSIST, // При удалении User от EntityManager, роли тоже удаляются
            CascadeType.REFRESH // При обновлении User из БД, Role тоже обновиться
            })
    @JoinTable(name = "user_roles",  joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Collection<Role> roles = new HashSet<>(); // роли пользователя
}
