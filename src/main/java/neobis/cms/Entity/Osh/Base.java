package neobis.cms.Entity.Osh;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Base {
    @Column(name = "date_created")
    private LocalDateTime dateCreated;
    @Column(name = "date_updated")
    private LocalDateTime dateUpdated;
    @Column(precision = 0, name = "deleted")
    private boolean isDeleted;

    @PrePersist
    public void onPrePersist() {
        this.dateCreated = LocalDateTime.now();
    }

    @PreUpdate
    public void onPreUpdate() {
        this.dateUpdated = LocalDateTime.now();
    }
}
