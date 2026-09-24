# Yamone English — Play Store Metadata Draft

> 상태: **초안**. 이 문서는 Play Console에 제출 완료된 값이 아닙니다. 실제 제출 직전에 Play Console의 현재 글자 수·그래픽·스크린샷 요구 조건과 최종 제품 방향을 다시 확인합니다.

## 1. 기본 등록 정보

- 패키지명: `com.yamone.english`
- 카테고리 제안: **Education**
- 기본 언어 제안: 한국어 (`ko-KR`)
- 추가 스토어 언어 제안: 영어 (`en-US`)
- 앱 이름(한국어): **Yamone English**
- App name (English): **Yamone English**

## 2. 한국어 스토어 문구

### 짧은 설명 초안

듣고 이해하고 말하며 영어식 어순까지 익히는 음성 중심 영어 회화 학습

### 상세 설명 초안

Yamone English는 한국어 사용자가 영어를 단어 암기보다 **듣기 → 이해 → 말하기 → 영어식 어순 → 실제 대화**의 흐름으로 익히도록 만든 음성 중심 영어 회화 학습 앱입니다.

학교에서 기본 단어와 문법을 배웠지만 실제 영어가 빠르게 들리지 않거나, 머릿속에서 한국어 문장을 먼저 만든 뒤 번역하느라 바로 말하기 어려운 학습자를 위해 구성했습니다.

주요 학습 기능:

- 5개 과정, 총 500개 레슨
- 영어권 일상 표현의 난이도를 5~7세, 8~10세, 11~13세, 14~16세, 17~20세 표현 수준으로 단계화
- 모든 레슨에 6턴 실제 대화 흐름 제공
- 문장 속 연결 발음, 강세, 억양과 리듬 가이드
- 영어식 사고와 어순 훈련
- Android 음성 인식을 활용한 말하기 연습
- 자연스러운 예문과 상황별 표현 연습
- 틀린 듣기·말하기·어순 항목을 자동 복습에 반영
- 과정별 종료 테스트와 학습 진행 기록

Yamone English의 목표는 한국어 문장을 영어로 번역하는 습관에서 벗어나, 영어에서 자연스럽게 이어지는 순서로 생각하고 말하는 힘을 키우는 것입니다.

학습 기록은 현재 앱 내부에 저장됩니다. 앱은 직접 운영 서버나 자체 네트워크 API로 학습 데이터를 전송하지 않습니다. 음성 인식과 TTS 기능은 Android 기기와 설치된 서비스 제공자의 구현을 사용하며, 해당 서비스의 동작 방식에 따라 음성 또는 텍스트 처리가 기기 밖에서 이루어질 수 있습니다.

## 3. English Store Copy

### Short description draft

Voice-first English practice for listening, speaking, word order, and conversation.

### Full description draft

Yamone English is a voice-first English conversation app designed for Korean speakers who know basic vocabulary and grammar but want to understand natural speech and respond more naturally in real situations.

Instead of focusing on isolated words, the app follows a practical learning flow: **listen → understand → speak → build English word order → continue the conversation**.

Key learning features:

- 5 progressive courses with 500 lessons in total
- Everyday expressions organized by reference levels inspired by how English-speaking children, teens, and young adults naturally communicate
- Exactly 6 dialogue turns in every lesson
- Guidance for linking, stress, intonation, and rhythm in connected speech
- English-thinking and word-order practice
- Speaking practice using Android speech recognition
- Natural examples and situation-based prompts
- Automatic review of missed listening, speaking, and word-order items
- End-of-course assessments and local study progress tracking

The goal is to help learners reduce word-for-word translation from Korean and build the habit of forming thoughts in a more natural English sequence.

Study progress is currently stored locally in the app. Yamone English does not directly send study data to its own server or network API. Speech recognition and text-to-speech rely on Android device/service providers, and processing may occur off-device depending on the provider and device configuration.

## 4. 대상 사용자 / 연령 설정 검토

앱 내부 과정 이름의 `5~7세`, `8~10세` 등은 **영어권에서 해당 연령대가 사용하는 표현 수준을 학습 난이도 기준으로 삼은 명칭**입니다. 이것을 Google Play의 법적/스토어 대상 연령과 자동으로 동일하게 보지 않습니다.

Play Console 제출 전 다음을 제품 정책으로 확정해야 합니다.

- 실제 주 대상이 성인/청소년 한국어 학습자인지
- 만 13세 미만 아동을 의도적으로 대상 사용자에 포함할지
- 아동 대상이 포함된다면 Google Play Families 및 관련 개인정보 정책 적용 여부

현재 초안에서는 **학습 난이도 구분과 스토어 대상 연령을 분리해서 판단**하는 것을 원칙으로 합니다.

## 5. 스크린샷 준비 체크리스트

최종 Play Console 요구 개수와 해상도는 제출 시점의 콘솔 안내를 기준으로 확인합니다. 다음 화면을 우선 캡처 후보로 준비합니다.

1. 홈 — 과정 선택과 오늘의 다음 학습
2. 레슨 — 핵심 문장과 뜻
3. 소리 가이드 — 연결 발음·강세·억양
4. 영어식 어순 훈련
5. 말하기 — 마이크를 통한 실제 발화 연습
6. 6턴 대화 예시
7. 자동 복습 화면
8. 과정 종료 테스트 / 결과 화면
9. 설정 — 듣기 속도와 학습 도움말

캡처 시 확인:

- 실제 출시 빌드와 동일한 UI 사용
- 개인정보, 개발자 계정, 테스트용 디버그 텍스트 노출 금지
- 작은 글씨가 읽히는 해상도 확보
- 한글 스토어에는 한국어 UI 중심, 영문 스토어를 운영하면 영문 설명과 이미지 구성도 별도 검토
- 상태바/내비게이션바 잘림 없는 Android 16 화면 사용

## 6. 그래픽 준비 항목

- [x] launcher/adaptive icon 최소 리소스 존재
- [ ] 최종 브랜드용 고해상도 앱 아이콘 디자인 확정
- [ ] Play Console feature graphic 제작
- [ ] 휴대폰 스크린샷 제작
- [ ] 필요 시 태블릿/대화면 스크린샷 제작
- [ ] Android 16 테마 아이콘용 monochrome 레이어 필요 여부 결정

## 7. 제출 전에 다시 확인할 항목

- [ ] 앱 이름과 설명의 최종 문구 승인
- [ ] 현재 Play Console 글자 수 제한에 맞는지 확인
- [ ] 카테고리와 대상 연령 최종 결정
- [ ] 콘텐츠 등급 설문 완료
- [ ] 개인정보처리방침 공개 URL 입력
- [ ] Data safety 응답을 `docs/PRIVACY_DATA_SAFETY.md`와 실제 출시 빌드 기준으로 최종 확인
- [ ] 스크린샷/feature graphic이 현재 Play 정책을 충족하는지 확인
- [ ] 서명된 AAB를 내부 테스트 트랙에서 먼저 설치 검증

## 8. 관련 내부 문서

- `../RELEASE_CHECKLIST.md`
- `PRIVACY_DATA_SAFETY.md`

이 문서의 문구는 마케팅/스토어 초안이며, Play Console 등록 완료 상태를 의미하지 않습니다.
