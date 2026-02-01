package com.ta2khu75.thinkhub.modules.quiz.draft.internal.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerDraft {
	Long mediaId;
	String content;
	boolean correct;
}
