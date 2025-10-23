package hu.unideb.cartshare.entity;

import hu.unideb.cartshare.entity.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "lists")
public class List extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "list")
    private java.util.Set<ListItem> items;

    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User user;
}
