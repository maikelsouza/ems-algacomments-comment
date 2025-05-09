package com.algaworks.algacomments.comment.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment{

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String text;

    private String author;

    private OffsetDateTime createdAt;
}
