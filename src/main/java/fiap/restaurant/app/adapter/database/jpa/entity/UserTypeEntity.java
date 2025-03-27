package fiap.restaurant.app.adapter.database.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_types")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 50, unique = true)
    private String name;

    @OneToMany(mappedBy = "userType")
    private List<UserEntity> users = new ArrayList<>();
} 