package hu.unideb.cartshare.entity;

import hu.unideb.cartshare.entity.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ListItem extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer quantity = 1;

    @Column(nullable = false)
    private Boolean isChecked = false;

    @ManyToOne(optional = false)
    @JoinColumn(name = "list_id", nullable = false)
    private List list;
}
