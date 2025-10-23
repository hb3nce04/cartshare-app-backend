package hu.unideb.cartshare.model.entity;

import hu.unideb.cartshare.model.entity.common.BaseEntity;
import hu.unideb.cartshare.model.enums.UserListRole;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "user_list_memberships")
public class UserListMembership extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "list_id")
    private List list;

    @Enumerated(EnumType.STRING)
    private UserListRole role;
}
