package hu.unideb.cartshare.model.entity;

import hu.unideb.cartshare.model.entity.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "list_items")
public class ListItem extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "DOUBLE PRECISION DEFAULT 1.0")
    private Double quantity = 1.0;

    @Column(nullable = false)
    private String unit;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isChecked = false;

    @ManyToOne(optional = false)
    @JoinColumn(name = "list_id", nullable = false)
    private List list;
}
