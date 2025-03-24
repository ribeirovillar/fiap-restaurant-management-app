package fiap.restaurant.app.adapter.database.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
