package com.algaworks.algacomments.comment.api.model;

import com.algaworks.algacomments.comment.domain.model.Comment;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
public class CommentOutput {

    private UUID id;

    private String text;

    private String author;

    private OffsetDateTime createdAt;


    public static CommentOutput convertToOutput(Comment comment){
        return CommentOutput.builder()
                .id(comment.getId())
                .text(comment.getText())
                .author(comment.getAuthor())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
