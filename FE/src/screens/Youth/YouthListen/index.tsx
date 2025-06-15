// 필요한 API 및 컴포넌트 import
import { useEffect, useRef, useState } from 'react';
import {
  Alert,
  Image,
  Keyboard,
  Pressable,
  ScrollView,
  TextInput,
  View,
} from 'react-native';
import AudioRecorderPlayer from 'react-native-audio-recorder-player';
import Toast from 'react-native-toast-message';

import { getVoiceFilesWithAlarmId } from '@apis/YouthListenToVoice/get/VoiceFilesWithAlarmId/fetch';
import { postProvidedfileCommentByProvidedFileId } from '@apis/YouthListenToVoice/post/ProvidedfileCommentByProvidedFileId/fetch';
import { AppBar } from '@components/AppBar';
import { BG } from '@components/BG';
import { CustomText } from '@components/CustomText';
import { COLORS } from '@constants/Colors';
import { KEYBOARD_DELAY_MS } from '@constants/common';
import { EMOTION_OPTIONS_YOUTH } from '@constants/letter';
import { VOICE_DELAY_MS, VOICE_LOADING_MS } from '@constants/voice';
import { useDeleteComment } from '@hooks/providedFile/useDeleteComment';
import { type NativeStackScreenProps } from '@react-navigation/native-stack';
import { LoadingScreen } from '@screens/Loading';
import { type YouthStackParamList } from '@stackNav/Youth';
import { type EmotionType } from '@type/api/providedFile';
import { type VoiceFileResponseData } from '@type/api/voiceFile';
import { showToast } from '@utils/showToast';
import { type AxiosError } from 'axios';
import LottieView from 'lottie-react-native';

import PlayIcon from '@assets/svgs/play_youth.svg';
import SendIcon from '@assets/svgs/send.svg';
import SmileIcon from '@assets/svgs/smile.svg';
import SmileGrayIcon from '@assets/svgs/smile_gray.svg';
import StopIcon from '@assets/svgs/stop.svg';

// 네비게이션 Props 타입 정의
type YouthProps = NativeStackScreenProps<
  YouthStackParamList,
  'YouthListenScreen'
>;

export const YouthListenScreen = ({
  route,
  navigation,
}: Readonly<YouthProps>) => {
  const { alarmId } = route.params;
  // 상태 관리
  const [message, setMessage] = useState(''); // 메시지 입력값
  const [isClickedEmotion, setIsClickedEmotion] = useState(false); // 감정 표현 클릭 여부
  const [isKeyboardVisible, setIsKeyboardVisible] = useState(false); // 키보드 표시 여부
  const animation = useRef<LottieView>(null); // 애니메이션 ref
  const [isPlaying, setIsPlaying] = useState(false); // 오디오 재생 여부
  const [voiceFile, setVoiceFile] = useState<VoiceFileResponseData>(
    {} as VoiceFileResponseData,
  ); // 음성 파일 데이터
  const [isLoading, setIsLoading] = useState(true); // 로딩 상태
  const audioPlayer = useRef(new AudioRecorderPlayer()); // 오디오 플레이어 ref
  const [sentEmotions, setSentEmotions] = useState<{
    [key in EmotionType]?: boolean;
  }>({}); // 전송된 감정 표현
  const { mutate: deleteComment } = useDeleteComment();

  // 초기 로딩 처리
  useEffect(() => {
    setIsLoading(true);

    const timer = setTimeout(() => {
      setIsLoading(false);
    }, VOICE_LOADING_MS);

    return () => clearTimeout(timer);
  }, []);

  // 애니메이션 재생/정지 처리
  useEffect(() => {
    if (isPlaying) {
      animation.current?.play();
    } else {
      animation.current?.pause();
    }

    return () => {
      animation.current?.pause();
    };
  }, [isPlaying]);

  // 키보드 이벤트 리스너 설정
  useEffect(() => {
    const showSubscription = Keyboard.addListener('keyboardDidShow', () =>
      setIsKeyboardVisible(true),
    );
    const hideSubscription = Keyboard.addListener('keyboardDidHide', () => {
      setTimeout(() => {
        setIsKeyboardVisible(false);
      }, KEYBOARD_DELAY_MS);
    });

    return () => {
      showSubscription.remove();
      hideSubscription.remove();
    };
  }, []);

  useEffect(() => {
    if (!alarmId) {
      return;
    }

    (async () => {
      try {
        const res = await getVoiceFilesWithAlarmId({ alarmId });

        console.log(res);
        setVoiceFile(res.result);
      } catch (error) {
        console.log(error);
        showToast({
          text: '음성을 모두 들었어요. 다음에 찾아와주세요!',
          type: 'text',
          position: 'top',
        });
        navigation.goBack();
      }
    })();
  }, [alarmId]);

  useEffect(() => {
    if (!voiceFile.fileUrl) return;

    const startPlaying = async () => {
      try {
        const playResult = await audioPlayer.current.startPlayer(
          voiceFile.fileUrl,
        );

        console.log('재생 시작:', playResult);
        setIsPlaying(true);

        audioPlayer.current.addPlayBackListener(e => {
          console.log('재생 위치:', e.currentPosition, '총 길이:', e.duration);

          if (e.currentPosition >= e.duration) {
            console.log('재생 끝');
            setIsPlaying(false);
          }
        });
      } catch (error) {
        console.log(error);
        Alert.alert('오류', '오디오 재생 중 오류가 발생했어요');
      }
    };

    setTimeout(startPlaying, VOICE_DELAY_MS);

    return () => {
      (async () => {
        await audioPlayer.current.stopPlayer();
        audioPlayer.current.removePlayBackListener();
        setIsPlaying(false);
      })();
    };
  }, [voiceFile.fileUrl]);

  const handleMessageSend = async (emotionType?: EmotionType) => {
    if (!emotionType && !message) {
      return;
    }

    if (emotionType) {
      const emotion = EMOTION_OPTIONS_YOUTH.find(
        option => option.type === emotionType,
      );

      if (!emotion) {
        return;
      }

      if (sentEmotions[emotionType]) {
        try {
          deleteComment({
            providedFileId: voiceFile.providedFileId,
            message: emotionType,
          });
          setSentEmotions(prev => {
            const updated = { ...prev };

            delete updated[emotionType];

            return updated;
          });
          showToast({
            text: `‘${emotion.label}’ 전송 취소`,
            type: 'check',
            position: 'left',
          });
        } catch (error) {
          console.log(error);
          Alert.alert('오류', '감정 표현을 취소하는 중 오류가 발생했어요');
        }

        return;
      }
    }

    try {
      await postProvidedfileCommentByProvidedFileId({
        providedFileId: voiceFile.providedFileId,
        message: emotionType ?? message,
      });

      if (emotionType) {
        const emotion = EMOTION_OPTIONS_YOUTH.find(
          option => option.type === emotionType,
        );

        if (!emotion) {
          return;
        }

        setSentEmotions(prev => ({ ...prev, [emotionType]: true }));

        showToast({
          text: `‘${emotion.label}’ 전송 완료`,
          type: 'check',
          position: 'left',
        });

        return;
      }

      showToast({
        text: '메시지 전송 완료',
        type: 'check',
        position: 'left',
      });

      if (Keyboard.isVisible()) {
        Keyboard.dismiss();

        setTimeout(() => {
          setIsKeyboardVisible(false);
        }, KEYBOARD_DELAY_MS);
      }

      setMessage('');
    } catch (error) {
      console.log(error);

      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      if (((error as AxiosError).response?.data as any).code === 'PF003') {
        showToast({
          text: '전송 가능 횟수를 초과했어요',
          type: 'notice',
          position: 'left',
        });
      } else {
        Alert.alert('오류', '메시지를 보내는 중 오류가 발생했어요');
      }
    }
  };

  // 재생/정지 버튼 클릭 처리
  const handlePlayButtonClick = async () => {
    console.log({ isPlaying, fileUrl: voiceFile.fileUrl });

    try {
      if (isPlaying) {
        await audioPlayer.current.pausePlayer();
        setIsPlaying(false);
      } else {
        await audioPlayer.current.startPlayer(voiceFile.fileUrl);
        setIsPlaying(true);
      }
    } catch (error) {
      console.log(error);
      Alert.alert('오류', '오디오 재생 중 오류가 발생했어요');
    }
  };

  // 로딩 중일 때 로딩 화면 표시
  if (isLoading) {
    return <LoadingScreen />;
  }

  // 메인 UI 렌더링
  return (
    <BG type="main">
      <View
        className={`absolute left-0 bottom-[40] w-full h-full ${
          isKeyboardVisible ? 'hidden' : ''
        }`}
        style={{ transform: [{ scale: 1.1 }] }}>
        <LottieView
          ref={animation}
          style={{
            flex: 1,
          }}
          // eslint-disable-next-line @typescript-eslint/no-require-imports
          source={require('@assets/lottie/voice.json')}
          autoPlay
          loop
        />
      </View>

      <View className="flex-1">
        <AppBar
          exitCallbackFn={() => navigation.goBack()}
          className="absolute top-[0] w-full"
        />

        <View className="h-[100] " />

        <View className="flex-1 items-center">
          {/* 프로필 */}
          <View className="flex-row self-start px-[30]">
            <View className="relative w-[31] h-[31] justify-center items-center">
              <Image
                source={
                  voiceFile?.member?.profileImage
                    ? { uri: voiceFile?.member?.profileImage }
                    : // eslint-disable-next-line @typescript-eslint/no-require-imports
                      require('@assets/pngs/logo/app/app_logo_yellow.png')
                }
                className="w-[25] h-[25]"
                style={{ borderRadius: 25 }}
              />
              <View
                className="absolute left-0 bottom-0 w-[31] h-[31] border border-yellowPrimary"
                style={{ borderRadius: 31 }}
              />
            </View>
            <View className="w-[10]" />
            <CustomText
              type="title4"
              text={voiceFile?.member?.name}
              className="text-yellowPrimary"
            />
          </View>

          <View className="h-[33] " />

          {/* 스크립트 */}
          <View className="px-[30] h-[244]">
            <ScrollView>
              <CustomText
                type="body3"
                text={voiceFile?.content ?? ''}
                className="text-white"
              />
            </ScrollView>
          </View>

          <View className="h-[33] " />

          {/* 재생/정지 버튼 */}
          <Pressable onPress={handlePlayButtonClick} className="w-[69] h-[69]">
            {isPlaying ? <StopIcon /> : <PlayIcon />}
          </Pressable>

          {/* 하단 입력 영역 */}
          <View
            className="absolute bottom-0 w-full"
            style={{ borderTopLeftRadius: 10, borderTopRightRadius: 10 }}>
            {/* 감정 표현 옵션 */}
            {isClickedEmotion && (
              <ScrollView
                horizontal
                showsHorizontalScrollIndicator={false}
                contentContainerStyle={{
                  flexDirection: 'row',
                  alignItems: 'center',
                }}
                className="pl-[25] w-full mb-[27]"
                keyboardShouldPersistTaps="always">
                {EMOTION_OPTIONS_YOUTH.map((emotion, index) => (
                  <Pressable
                    key={emotion.label}
                    className={`bg-blue400 py-[9] pl-[14] pr-[19] ${
                      index === EMOTION_OPTIONS_YOUTH.length - 1
                        ? 'mr-[50]'
                        : 'mr-[10]'
                    } flex-row items-center justify-center active:bg-blue500 ${
                      sentEmotions[emotion.type] ? 'bg-blue500' : ''
                    }`}
                    style={{ borderRadius: 50 }}
                    onPress={() => handleMessageSend(emotion.type)}>
                    {emotion.icon}
                    <CustomText
                      type="body3"
                      text={emotion.label}
                      className="text-white ml-[10]"
                    />
                  </Pressable>
                ))}
              </ScrollView>
            )}
            {/* 메시지 입력 영역 */}
            <View className="h-[86] px-[25] bg-blue500 flex-row items-center">
              <View className="flex-[7.5] relative">
                <TextInput
                  value={message}
                  onChangeText={setMessage}
                  placeholder="감사의 말을 전해보세요"
                  placeholderTextColor={COLORS.gray300}
                  className={`h-[40] text-gray100 py-[8] pl-[27] pr-[45] font-r bg-blue400 border ${
                    isKeyboardVisible ? 'border-gray200' : 'border-blue400'
                  }`}
                  style={{ fontSize: 15, borderRadius: 100 }}
                  onSubmitEditing={() => handleMessageSend()}
                  maxLength={160}
                />
                {!!message && (
                  <Pressable
                    className={`absolute right-[6] top-[5]`}
                    onPress={() => handleMessageSend()}>
                    <SendIcon />
                  </Pressable>
                )}
              </View>
              <View className="w-[15]" />
              <Pressable
                className="flex-[1]"
                onPress={() => setIsClickedEmotion(prev => !prev)}>
                {isClickedEmotion ? <SmileGrayIcon /> : <SmileIcon />}
              </Pressable>
            </View>
          </View>
        </View>
      </View>
    </BG>
  );
};
