package naeilmolae.domain.voicefile.application.usecase;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.alarm.application.dto.response.AlarmResponse;
import naeilmolae.domain.alarm.application.usecase.AlarmAdapterUseCase;
import naeilmolae.domain.member.domain.Member;
import naeilmolae.domain.member.service.MemberService;
import naeilmolae.domain.pushnotification.service.adapter.PushNotificationAdapterService;
import naeilmolae.domain.voicefile.domain.entity.ProvidedFile;
import naeilmolae.domain.voicefile.domain.entity.ThanksMessage;
import naeilmolae.domain.voicefile.domain.entity.VoiceFile;
import naeilmolae.domain.voicefile.domain.entity.VoiceReactionType;
import naeilmolae.domain.voicefile.application.dto.response.RetentionDto;
import naeilmolae.domain.voicefile.application.dto.response.VoiceFileReactionSummaryResponseDto;
import naeilmolae.domain.voicefile.domain.repository.ProvidedFileRepository;
import naeilmolae.global.common.exception.RestApiException;
import naeilmolae.global.common.exception.code.status.ProvidedFileErrorStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProvidedFileUseCase {
    private final ProvidedFileRepository providedFileRepository;
    private final MemberService memberService;
    private final VoiceFileUseCase voiceFileUseCase;
    // 외부 서비스
    private final AlarmAdapterUseCase alarmAdapterUsecase;
    private final PushNotificationAdapterService pushNotificationAdapterService;

    @Transactional
    public ProvidedFile save(Long memberId, Long voiceFileId) {
        // 이미 제공한 파일인지 확인
        providedFileRepository.findByConsumerIdAndVoiceFileId(memberId, voiceFileId)
                .ifPresent(file -> {
                    throw new RestApiException(ProvidedFileErrorStatus._ALREADY_PROVIDED);
                });

        VoiceFile voiceFile = voiceFileUseCase.findById(voiceFileId);
        Member consumer = memberService.findById(memberId);


        ProvidedFile providedFile = new ProvidedFile(voiceFile, consumer.getId());

        return providedFileRepository.save(providedFile);
    }

    // 봉사자용 편지 조회
    public Page<ProvidedFile> getProvidedFiles(Long memberId, String parentCategory, Pageable pageable) {
        if (parentCategory == null) {
            return providedFileRepository.findByMemberId(memberId, pageable);
        } else {
            // 부모 카테고리를 받아서 해당하는 자식 카테고리 + alarmId를 List로 받음
            List<Long> list = alarmAdapterUsecase.findAlarmIdsByAlarmCategory(parentCategory)
                    .stream()
                    .map(AlarmResponse::getAlarmId)
                    .toList();

            // List를 가지고 in query로 ProvidedFile을 조회
            return providedFileRepository.findByMemberIdAndAlarmId(memberId, list, pageable);
        }
    }

    // 감사 편지 보내기
    @Transactional
    public List<String> likeProvidedFile(Long consumerId, Long providedFileId, String message) {
        ProvidedFile providedFile = providedFileRepository.findByConsumerId(consumerId, providedFileId)
                .orElseThrow(() -> new RestApiException(ProvidedFileErrorStatus._NOT_FOUND_FILE));

        if (providedFile.getThanksMessages().size() >= 5) {
            throw new RestApiException(ProvidedFileErrorStatus._EXCEED_MESSAGE);
        }

        providedFile.addThanksMessage(message);
        // 봉사자에게 알림 보내기
        pushNotificationAdapterService.sendNotificationThankYouMessage(providedFile.getVoiceFile().getMemberId());

        return providedFile.getThanksMessages().stream().map(ThanksMessage::getMessage).toList();
    }

    @Transactional
    public List<String> deleteLikeProvidedFile(Long consumerId, Long providedFileId, String message) {
        ProvidedFile providedFile = providedFileRepository.findByConsumerId(consumerId, providedFileId)
                .orElseThrow(() -> new RestApiException(ProvidedFileErrorStatus._NOT_FOUND_FILE));

        providedFile.removeThanksMessage(message);

        return providedFile.getThanksMessages().stream().map(ThanksMessage::getMessage).toList();
    }

    // 음성 파일 북마크하기
    @Transactional
    public boolean bookmarkProvidedFile(Long consumerId, Long providedFileId) {
        ProvidedFile providedFile = providedFileRepository.findByConsumerId(consumerId, providedFileId)
                .orElseThrow(() -> new RestApiException(ProvidedFileErrorStatus._NOT_FOUND_FILE));
        return providedFile.setConsumerSaved();
    }

    public RetentionDto getRetentionData(Long memberId) {
        Long voiceCount = voiceFileUseCase.getVoiceFileCount(memberId);
        Long thanksCount = 0L;
        Long messageCount = 0L;

        List<ThanksMessage> thankMessagesByMemberId = providedFileRepository.findThankMessagesByMemberId(memberId);
        List<String> reactionValues = thankMessagesByMemberId.stream()
                .map(ThanksMessage::getMessage)
                .toList();

        Map<VoiceReactionType, Integer> reactionSummary = new HashMap<>();
        for (VoiceReactionType reaction : VoiceReactionType.values()) {
            long count = reactionValues.stream()
                    .filter(value -> value.equals(reaction.getValue()))
                    .count();
            reactionSummary.put(reaction, (int) count);
        }

        thanksCount += (long) reactionSummary.values().stream().mapToInt(Integer::intValue).sum();
        messageCount += (long) reactionValues.size();

        return new RetentionDto(voiceCount,
                thanksCount,
                messageCount);
    }

    // 청년의 반응 보여주기
    public VoiceFileReactionSummaryResponseDto getTotalReaction(Long memberId) {

        Long totalListenCount = getTotalListenCount(memberId);
        Map<VoiceReactionType, Integer> reactionSummary = getReactionSummary(memberId);

        return new VoiceFileReactionSummaryResponseDto(totalListenCount, reactionSummary);
    }

    // 청년의 이모티콘 반응 요약
    private Map<VoiceReactionType, Integer> getReactionSummary(Long memberId) {
        List<ThanksMessage> thankMessagesByMemberId = providedFileRepository.findThankMessagesByMemberId(memberId);
        List<String> reactionValues = thankMessagesByMemberId.stream()
                .map(ThanksMessage::getMessage)
                .toList();

        Map<VoiceReactionType, Integer> reactionSummary = new HashMap<>();
        for (VoiceReactionType reaction : VoiceReactionType.values()) {
            long count = reactionValues.stream()
                    .filter(value -> value.equals(reaction.getValue()))
                    .count();
            reactionSummary.put(reaction, (int) count);
        }

        return reactionSummary;
    }

    //청년 누적 청취 수 반환 (나중에 추가 로직 작성될 수도 있어서 메서드로 따로 뻄)
    private long getTotalListenCount(Long memberId) {
        return providedFileRepository.findTotalListenersByMemberId(memberId);
    }
}
