package io.events.entities;

import java.rmi.registry.Registry;
import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

@Entity
@Immutable
@Cacheable
@Table(name = "link_shortening_registry")
public class LinkShorteningRegistry extends PanacheEntity {

    @Id
    @Column(
        name = "original_link",
        columnDefinition = "VARCHAR(512)",
        unique = true,
        nullable = false,
        updatable = false
    )
    private String originalLink;

    @Column(
        name = "shortened_link",
        columnDefinition = "CHAR(7)",
        unique = true,
        nullable = false,
        updatable = false
    )
    private String shortenedLink;

    @CreationTimestamp
    @Column(
        name = "created_at",
        nullable = false,
        updatable = false)
    private Instant createdAt;

    @Override
    public int hashCode() {
        int hash = 7;
        return 31 * hash + this.originalLink.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null) return false;
        if(this.getClass() != o.getClass()) return false;
        LinkShorteningRegistry linkShorteningRegistry = (LinkShorteningRegistry) o;
        return this.originalLink.equals(linkShorteningRegistry.originalLink);
    }

    public static Uni<LinkShorteningRegistry> findByOriginalLink(String originalLink) {
        return find("originalLink", originalLink).firstResult();
    }
}
