package br.com.chart.enterative.entity.base;

import br.com.chart.enterative.entity.User;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author William Leite
 */
@MappedSuperclass
@SuppressWarnings("serial")
@ToString
public abstract class UserAwareEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter protected Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    @Getter @Setter protected Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "altered_at")
    @Getter @Setter protected Date alteredAt;

    @Column(name = "name")
    @Getter @Setter protected String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @Getter @Setter protected User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "altered_by")
    @Getter @Setter protected User alteredBy;

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.id);
        hash = 71 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserAwareEntity other = (UserAwareEntity) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return String.format("[UserAwareEntity: %s - %s", this.id, this.name);
    }
}
