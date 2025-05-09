package com.algaworks.algacomments.comment.domain.service;

import com.algaworks.algacomments.comment.api.client.ModerationClient;
import com.algaworks.algacomments.comment.api.model.CommentInput;
import com.algaworks.algacomments.comment.api.model.CommentOutput;
import com.algaworks.algacomments.comment.api.model.ModerationInput;
import com.algaworks.algacomments.comment.api.model.ModerationOutput;
import com.algaworks.algacomments.comment.domain.model.Comment;
import com.algaworks.algacomments.comment.domain.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository repository;

    private final ModerationClient client;

    @Transactional
    public CommentOutput create(CommentInput commentInput){
        Comment comment = CommentInput.convertToModel(commentInput);
        comment = repository.save(comment);
        return CommentOutput.convertToOutput(comment);
    }

    public CommentOutput findById(String uuid) {
        return repository.findById(UUID.fromString(uuid))
                .map(CommentOutput::convertToOutput)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));
    }

    public Page<CommentOutput> findAllPaged(Pageable pageable){
        return repository.findAll(pageable)
                .map(CommentOutput::convertToOutput);
    }

    public ModerationOutput moderateWord(ModerationInput moderationInput){
        return client.moderate(moderationInput);
    }



}
