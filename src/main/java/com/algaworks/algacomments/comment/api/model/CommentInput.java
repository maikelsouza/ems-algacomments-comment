package com.algaworks.algacomments.comment.api.model;

import com.algaworks.algacomments.comment.domain.model.Comment;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
public class CommentInput {

    private UUID id;

    @NotNull
    private String text;

    @NotNull
    private String author;

    public static Comment convertToModel(CommentInput commentInput){
            return Comment.builder()
                    .id(commentInput.getId())
                    .createdAt(OffsetDateTime.now())
                    .author(commentInput.getAuthor())
                    .text(commentInput.getText())
                    .build();
    }
}
