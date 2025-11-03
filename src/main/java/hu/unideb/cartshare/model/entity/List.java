package hu.unideb.cartshare.model.entity;

import hu.unideb.cartshare.model.entity.common.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

/**
 * Spring Data JPA list entity with table name: lists.
 */
@Getter
@Setter
@Entity(name = "lists")
public class List extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL)
    private java.util.Set<ListItem> items;

    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL)
    private java.util.Set<ListMembership> relationships;
}
