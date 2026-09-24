# Yamone English 개인정보처리방침 초안 / Privacy Policy Draft

> 상태: **공개 전 초안**. 아래 `TODO` 항목을 운영자가 확정하고 공개 URL에 게시하기 전까지 Play Console 제출 완료본이 아닙니다.

---

# 한국어

## 1. 운영자 정보

- 서비스명: Yamone English
- 운영자/개발자: **TODO — 공개할 이름 또는 사업자명**
- 개인정보 문의 이메일: **TODO — 공개 문의 이메일**
- 공개 개인정보처리방침 URL: **TODO — 게시 후 입력**
- 시행일: **TODO — 실제 공개일**

## 2. 서비스 개요

Yamone English는 영어 듣기, 말하기, 영어식 어순, 대화 연습과 과정별 평가를 제공하는 Android 영어 학습 앱입니다.

현재 출시 준비 버전은 계정 가입, 자체 서버 동기화, 광고 SDK, 자체 분석 SDK 또는 온라인 AI API를 사용하지 않으며, 학습 진행 정보는 주로 사용자의 기기 내부에 저장합니다.

## 3. 기기 내부에 저장되는 정보

앱은 Android `SharedPreferences`를 사용해 다음 정보를 기기 내부에 저장할 수 있습니다.

- 선택한 학습 과정
- 완료한 레슨
- 복습 대상 레슨
- 영어식 어순/표현 학습 완료 상태
- 듣기 속도와 일부 화면 설정
- 과정 종료 테스트 결과 및 로컬 학습 상태

현재 앱은 이러한 학습 기록을 Yamone English가 운영하는 자체 서버로 전송하지 않습니다.

앱은 `android:allowBackup="false"` 정책을 사용하므로 현재 제품 정책상 Android OS 클라우드 백업을 허용하지 않습니다.

## 4. 마이크 권한과 음성 인식

앱은 다음 기능을 위해 `RECORD_AUDIO` 권한을 요청할 수 있습니다.

- 영어 문장 따라 말하기
- 내 문장 말하기
- 영어 대화 응답
- 과정 테스트의 말하기 문제
- 한국어 음성을 활용한 영어 표현 연습

마이크는 사용자가 음성 기능을 직접 실행할 때 사용됩니다.

현재 앱은 원본 마이크 오디오 파일을 Yamone English의 자체 서버나 영구 앱 데이터베이스에 저장하지 않습니다.

다만 Android의 `SpeechRecognizer` 기능은 기기 제조사, Android 시스템 또는 사용자가 설치/선택한 음성 인식 서비스 제공자가 처리합니다. 기기와 제공자 설정에 따라 음성이 기기 내부에서 처리될 수도 있고 서비스 제공자의 서버에서 처리될 수도 있습니다. 해당 처리에는 해당 서비스 제공자의 개인정보처리방침이 적용될 수 있습니다.

## 5. TTS(Text-to-Speech)

앱은 Android `TextToSpeech` 기능으로 영어 학습 문장 등을 읽어줄 수 있습니다.

Yamone English 앱 코드가 자체 TTS 서버를 직접 호출하지는 않지만, 실제 음성 합성이 기기 내부 또는 외부 서비스에서 처리되는지는 사용자의 기기와 TTS 엔진에 따라 달라질 수 있습니다.

## 6. 현재 사용하지 않는 외부 처리 기능

현재 출시 준비 버전에서는 다음 기능을 활성화하지 않습니다.

- 회원가입/로그인
- 자체 서버 계정 저장
- 자체 서버 학습 기록 동기화
- 광고 SDK
- 자체 분석/통계 SDK
- 온라인 AI API
- 자체 음성 파일 업로드/보관

이 기능들이 향후 추가되는 경우 개인정보처리방침을 다시 검토하고 필요한 경우 변경 내용을 공개합니다.

## 7. 개인정보의 보유 및 삭제

현재 학습 정보는 주로 앱의 로컬 저장소에 유지됩니다.

사용자는 Android의 앱 데이터 삭제 기능 또는 앱 삭제를 통해 앱의 로컬 학습 기록을 제거할 수 있습니다.

앱 계정이나 자체 서버 계정이 현재 없으므로 별도의 서버 계정 삭제 절차는 제공하지 않습니다.

## 8. 제3자 서비스

Android 음성 인식 및 TTS 기능의 실제 서비스 제공자는 기기/OS/사용자 설정에 따라 달라질 수 있습니다. 해당 서비스가 데이터를 처리하는 경우 해당 제공자의 약관 및 개인정보처리방침이 적용될 수 있습니다.

Yamone English는 현재 자체 광고 네트워크, 자체 분석 SDK 또는 자체 클라우드 사용자 데이터 서버를 앱에 포함하지 않습니다.

## 9. 아동 및 대상 연령

앱 안의 `5~7세`, `8~10세` 등의 과정 이름은 영어 표현의 난이도와 학습 단계를 구분하기 위한 콘텐츠 기준입니다. Google Play에서 설정할 실제 대상 연령과 Families 정책 적용 여부는 출시 전에 별도로 확정해야 합니다.

**TODO — 실제 Play 대상 연령과 아동 대상 여부를 확정한 뒤 이 문단을 정책 요구사항에 맞게 수정합니다.**

## 10. 정책 변경

앱 기능, 데이터 처리 방식 또는 법적 요구사항이 바뀌면 이 개인정보처리방침을 수정할 수 있습니다. 중요한 변경이 있는 경우 앱 또는 공개 배포 채널을 통해 안내할 수 있습니다.

## 11. 문의

개인정보 및 앱 데이터 처리와 관련한 문의:

- 이메일: **TODO — 공개 문의 이메일**

---

# English

## 1. Operator Information

- Service: Yamone English
- Operator / Developer: **TODO — public individual or business name**
- Privacy contact email: **TODO — public contact email**
- Public privacy-policy URL: **TODO — add after publication**
- Effective date: **TODO — actual publication date**

## 2. Service Overview

Yamone English is an Android English-learning app that provides listening, speaking, English word-order practice, conversation practice, and course assessments.

The current release-candidate build does not use account registration, Yamone-operated cloud sync, advertising SDKs, Yamone-operated analytics SDKs, or an online AI API. Learning progress is primarily stored locally on the user's device.

## 3. Information Stored on the Device

The app may use Android `SharedPreferences` to store information such as:

- selected learning course;
- completed lessons;
- review items;
- completion state for word-order and expression practice;
- listening speed and selected display settings; and
- course-assessment results and local learning state.

The current app does not transmit this learning-progress data to a server operated by Yamone English.

The app currently uses `android:allowBackup="false"`, so Android OS cloud backup is disabled as a product policy for this version.

## 4. Microphone Permission and Speech Recognition

The app may request `RECORD_AUDIO` permission for features such as:

- repeating English sentences;
- speaking your own sentence;
- spoken conversation responses;
- speaking questions in course assessments; and
- practicing English expressions from Korean speech.

The microphone is used when the user actively starts a speech feature.

The current app does not permanently store raw microphone audio files on a Yamone-operated server or in a permanent Yamone speech database.

However, Android `SpeechRecognizer` is implemented by the device manufacturer, Android system, or a speech-recognition service installed or selected by the user. Depending on the device and provider, speech may be processed on-device or by the provider's servers. The provider's own privacy policy may therefore apply to that processing.

## 5. Text-to-Speech

The app uses Android `TextToSpeech` to read learning content aloud.

The Yamone English app does not directly call a Yamone-operated TTS server. Whether synthesis is performed on-device or by an external service depends on the user's device and selected TTS engine.

## 6. Features Not Currently Enabled

The current release-candidate version does not enable:

- user registration or login;
- Yamone-operated cloud accounts;
- Yamone-operated learning-progress sync;
- advertising SDKs;
- Yamone-operated analytics SDKs;
- online AI APIs; or
- Yamone-operated raw-audio upload/storage.

If any of these features are added later, this policy will be reviewed and updated as required.

## 7. Retention and Deletion

Learning information is currently kept mainly in local app storage.

Users can remove local learning records by clearing the app's data in Android settings or uninstalling the app.

Because the current app does not provide a Yamone server account, there is currently no separate server-account deletion process.

## 8. Third-Party Services

The actual provider of Android speech recognition and TTS may vary by device, operating system, and user settings. If those providers process data, their own terms and privacy policies may apply.

The current Yamone English build does not include its own advertising network, Yamone-operated analytics SDK, or Yamone-operated cloud user-data service.

## 9. Children and Target Audience

Course labels such as `ages 5–7` or `ages 8–10` describe the language-expression level and learning progression inside the app. They do not by themselves determine the app's Google Play target-audience setting.

**TODO — finalize the actual Google Play target audience and whether Families/children-related requirements apply, then revise this section accordingly.**

## 10. Changes to This Policy

This policy may be updated when app functionality, data practices, or legal requirements change. Material changes may be announced through the app or the public distribution channel.

## 11. Contact

For privacy or app-data questions:

- Email: **TODO — public contact email**

---

## Publication checklist / 공개 전 체크

- [ ] 운영자/개발자 공개명 확정
- [ ] 문의 이메일 확정
- [ ] 실제 공개 URL 준비
- [ ] 시행일 입력
- [ ] 실제 Play 대상 연령/Families 적용 여부 확정
- [ ] 최종 출시 AAB의 권한/SDK/네트워크 동작과 내용 대조
- [ ] `docs/PRIVACY_DATA_SAFETY.md`와 모순 없는지 확인
- [ ] Play Console Data safety 최종 답변과 모순 없는지 확인

이 초안은 법률 자문을 대체하지 않으며, 실제 배포 국가·대상 사용자·사업자 형태에 따라 필요한 추가 고지를 출시 전에 검토해야 합니다.
