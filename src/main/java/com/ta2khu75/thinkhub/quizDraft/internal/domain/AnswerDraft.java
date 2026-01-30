package com.ta2khu75.thinkhub.quizDraft.internal.domain;

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
