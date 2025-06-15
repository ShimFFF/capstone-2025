package naeilmolae.domain.voicefile.domain.entity;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum VoiceReactionType {
    THANK_YOU("고마워요"),
    HELPFUL("도움되요"),
    MOTIVATED("힘낼게요"),
    LOVE("사랑해요");

    private final String value;

    VoiceReactionType(String value) {
        this.value = value;
    }

    // value가 VoiceReactionType에 해당하는지 확인
    public static boolean isReactionValue(String message) {
        return Arrays.stream(values())
                .anyMatch(reaction -> reaction.getValue().equals(message));
    }
}
