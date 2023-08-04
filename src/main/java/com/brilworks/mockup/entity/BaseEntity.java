package com.brilworks.mockup.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class BaseEntity {

    @Column(name = "created")
    @CreationTimestamp
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    protected OffsetDateTime created;
    @Column(name = "updated")
    @UpdateTimestamp
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    protected OffsetDateTime updated;
    @Column(name = "is_active")
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    protected boolean active;
}
