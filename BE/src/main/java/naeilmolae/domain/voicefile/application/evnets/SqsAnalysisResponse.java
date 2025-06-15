package naeilmolae.domain.voicefile.application.evnets;

import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naeilmolae.domain.chatgpt.dto.ScriptValidationResponseDto;
import naeilmolae.domain.chatgpt.service.ChatGptService;
import naeilmolae.domain.voicefile.domain.entity.AnalysisResultStatus;
import naeilmolae.domain.voicefile.domain.entity.VoiceFile;
import naeilmolae.domain.voicefile.application.dto.response.AnalysisResponseDto;
import naeilmolae.domain.voicefile.application.usecase.VoiceFileUseCase;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Profile("prod")
public class SqsAnalysisResponse {

    private final VoiceFileUseCase voiceFileUseCase;
    private final ChatGptService chatGptService;

    @SqsListener("${kafka.topic.analysis.response}")
    public void analysisResponseConsumer(@Payload AnalysisResponseDto analysisResponseDto) {
        // 파일 조회
        Long voiceFileId = analysisResponseDto.voiceFileId();
        VoiceFile voiceFile = voiceFileUseCase.findById(voiceFileId);

        // 성공 저장
        if ("SUCCESS".equals(analysisResponseDto.analysisResultStatus())) {

            // stt 결과와 script를 비교하여 유효성 검사
            ScriptValidationResponseDto scriptValidationResponseDto =
                    chatGptService.getCheckScriptRelevancePrompt2(voiceFile.getContent(), analysisResponseDto.sttContent());

            log.info("음성 유사도 분석 결과 : {}", scriptValidationResponseDto);

            // 결과 저장
            if (!scriptValidationResponseDto.isProper()) { // 성공시
                Integer reason = scriptValidationResponseDto.getReason();
                AnalysisResultStatus analysisResultStatus = AnalysisResultStatus.of(reason);
                String description = analysisResultStatus.getDescription();
                voiceFileUseCase.saveResult(analysisResponseDto);
                log.info("음성 유사도 분석 실패 : {}", description);
            } else { // 실패시
                voiceFileUseCase.saveResult(analysisResponseDto);
            }
        } else { // 실패 저장
            voiceFileUseCase.saveResult(analysisResponseDto);
        }
    }
}
