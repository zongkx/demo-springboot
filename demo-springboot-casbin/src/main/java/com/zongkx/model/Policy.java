package com.zongkx.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

/**
 * @author zongkxc
 */

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity(name = "policy")
public class Policy {
    @Id
    private String id;

    private String sub;

    private String obj;

    private String act;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Policy policy = (Policy) o;
        return id != null && Objects.equals(id, policy.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
