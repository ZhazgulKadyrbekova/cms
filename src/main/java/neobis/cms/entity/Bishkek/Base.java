package neobis.cms.Entity.Bishkek;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Data
@MappedSuperclass

public class Base {
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;
    @Column(precision = 0, name = "deleted", nullable = false)
    private boolean isDeleted;

    @PrePersist
    public void persistCreate() {
        this.dateCreated = LocalDateTime.now();
    }

    @PreUpdate
    public void persistUpdate() {
        this.dateUpdated = LocalDateTime.now();
    }
}
