package neobis.cms.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long ID;

    @Column(name = "date_created")
    private LocalDateTime dateCreated;
    @Column(name = "date_updated")
    private LocalDateTime dateUpdated;

    @PrePersist
    public void onPrePersist() {
        this.dateCreated = LocalDateTime.now();
    }

    @PreUpdate
    public void onPreUpdate() {
        this.dateUpdated = LocalDateTime.now();
    }
}
