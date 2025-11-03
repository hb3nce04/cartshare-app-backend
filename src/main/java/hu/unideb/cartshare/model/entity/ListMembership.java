package hu.unideb.cartshare.model.entity;

import hu.unideb.cartshare.model.entity.common.BaseEntity;
import hu.unideb.cartshare.model.enums.MembershipRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

/**
 * Spring Data JPA list - user membership entity with a table name: user_list_memberships.
 */
@Getter
@Setter
@Entity(name = "user_list_memberships")
public class ListMembership extends BaseEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "list_id")
    private List list;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'MEMBER'")
    private MembershipRole role;
}
