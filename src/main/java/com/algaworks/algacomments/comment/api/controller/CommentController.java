package com.algaworks.algacomments.comment.api.controller;

import com.algaworks.algacomments.comment.api.model.CommentInput;
import com.algaworks.algacomments.comment.api.model.CommentOutput;
import com.algaworks.algacomments.comment.api.model.ModerationInput;
import com.algaworks.algacomments.comment.api.model.ModerationOutput;
import com.algaworks.algacomments.comment.domain.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?>  create(@Valid @RequestBody CommentInput commentInput){
        UUID uuid = UUID.randomUUID();

        ModerationOutput moderationOutput = service.moderateWord(ModerationInput.builder()
                        .id(uuid)
                        .text(commentInput.getText())
                        .build());
        if(!moderationOutput.isApproved()){
            return ResponseEntity.unprocessableEntity().body(moderationOutput);
        }
        commentInput.setId(uuid);
        return ResponseEntity.ok(service.create(commentInput));
    }

    @GetMapping("{id}")
    public ResponseEntity<CommentOutput> findById(@PathVariable String id ) {
        CommentOutput commentOutput = service.findById(id);
        return ResponseEntity.ok(commentOutput);
    }

    @GetMapping
    public Page<CommentOutput> search(Pageable pageable) {
        return service.findAllPaged(pageable);
    }

}
