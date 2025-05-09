package com.algaworks.algacomments.comment.api.client;

import com.algaworks.algacomments.comment.api.model.ModerationInput;
import com.algaworks.algacomments.comment.api.model.ModerationOutput;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/api/moderations")
public interface ModerationClient {


    @PostExchange
    ModerationOutput moderate(@RequestBody ModerationInput moderationInput);
}
