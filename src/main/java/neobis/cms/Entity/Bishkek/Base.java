package neobis.cms.Entity.Bishkek;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class Base {
    @Column(name = "date_created")
    private LocalDateTime dateCreated;
    @Column(name = "date_updated")
    private LocalDateTime dateUpdated;
    @Column(name = "is_deleted", precision = 0, nullable = false)
    private boolean deleted;

    @PrePersist
    public void onPrePersist() {
        this.dateCreated = LocalDateTime.now();
    }

    @PreUpdate
    public void onPreUpdate() {
        this.dateUpdated = LocalDateTime.now();
    }
}
