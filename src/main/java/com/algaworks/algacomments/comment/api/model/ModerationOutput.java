package com.algaworks.algacomments.comment.api.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModerationOutput {

    private boolean approved;

    private String reason;

}
