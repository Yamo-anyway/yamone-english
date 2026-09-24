# Yamone English 출시 후보 체크리스트

v0.8.5 Play 스토어 제출 준비 2차 이후 실제 Android 기기와 스토어 계정에서 확인해야 하는 항목입니다. 체크되지 않은 항목은 자동화 환경에서 실제 테스트·등록·제출했다고 간주하지 않습니다.

## 0. 2026 제출 기준 / 높은 우선순위
- [x] `compileSdk = 36`, `targetSdk = 36`으로 Android 16(API 36) 기준 반영
- [x] AGP 8.10.1 + Gradle 8.11.1 + JDK 17 조합으로 CI 구성
- [ ] **2026-09-30 전에** Play Console의 Android developer verification 상태를 확인한다.
- [ ] Play Console에서 `com.yamone.english` 패키지 등록 상태를 확인한다. Google이 자동 등록했더라도 상태 화면에서 최종 확인한다.
- [ ] 개발자 신원 확인이 아직 끝나지 않았다면 Play Console 안내에 따라 완료한다.

공식 참고:
- Target API: https://support.google.com/googleplay/android-developer/answer/11926878
- Developer verification: https://developer.android.com/developer-verification
- Play package registration: https://support.google.com/googleplay/android-developer/answer/16984799

## 1. 설치 / 업데이트 / 저장 기록
- [ ] v0.8.5 debug APK 신규 설치 후 5개 과정이 모두 표시된다.
- [ ] 기존 v0.7.x 또는 v0.8.0~0.8.4 설치 상태 위에 업데이트 설치해 `selected_course`가 유지된다.
- [ ] 업데이트 뒤 `completed`, `review`, `thinking_completed`, `expression_completed`가 유지된다.
- [ ] 5개 과정의 기존 assessment 결과가 유지되고 손상된 값은 UI를 깨뜨리지 않는다.
- [ ] 앱 삭제 후 재설치 시 로컬 기록이 초기화되는 현재 정책을 확인한다.

## 2. 과정 / 화면 / 뒤로가기
각 5개 과정에서 동일하게 확인한다.
- [ ] 0/100 상태에서 1번 레슨이 다음 학습으로 표시된다.
- [ ] 99/100 상태에서 실제 남은 레슨 1개가 다음 학습으로 표시된다.
- [ ] 100/100 상태에서 마지막 레슨이 `이어서 학습`으로 다시 나오지 않는다.
- [ ] 100/100 상태에서 과정 종료 테스트 진입이 명확하다.
- [ ] 홈 → 레슨 → Android 시스템 뒤로가기 → 홈 복귀가 정상이다.
- [ ] 홈 → 설정 → Android 시스템 뒤로가기/닫기 → 홈 복귀가 정상이다.
- [ ] 홈 → 과정 테스트 → Android 시스템 뒤로가기 및 완료 후 홈 복귀가 정상이다.
- [ ] 듣기/어순/대화/복습 탭에서 시스템 뒤로가기를 누르면 오늘 탭으로 먼저 복귀한다.
- [ ] 오늘 탭에서 시스템 뒤로가기를 누르면 일반 Android 종료 동작을 따른다.
- [ ] Android 16의 predictive back 애니메이션/3-button back에서도 현재 Compose `BackHandler` 계층이 의도대로 동작한다.
- [ ] 화면 회전/프로세스 재생성으로 탭 인덱스가 비정상 복원되어도 크래시하지 않는다.
- [ ] 듣기/어순/대화/복습 탭 이동 뒤 선택 과정이 바뀌거나 섞이지 않는다.

## 3. Android 16 / 화면 크기 / edge-to-edge
코드 정적 점검에서는 edge-to-edge opt-out, 고정 orientation, `resizeableActivity=false`, aspect ratio 제한을 사용하지 않는다. 실제 화면 품질은 기기/에뮬레이터에서 확인한다.
- [ ] Android 16 휴대폰에서 상태바/내비게이션바 아래 콘텐츠가 잘리지 않는다.
- [ ] 제스처 내비게이션과 3-button 내비게이션에서 하단 내비게이션이 정상이다.
- [ ] 작은 Android 화면에서 홈 카드와 하단 내비게이션이 잘리지 않는다.
- [ ] 큰 글꼴 설정에서 주요 버튼과 평가 문구가 겹치지 않는다.
- [ ] Pixel Tablet 또는 동급 sw600dp 에뮬레이터에서 세로/가로 전환 시 레이아웃이 사용 가능하다.
- [ ] 폴더블/대화면 리사이즈 또는 멀티윈도우에서 상태가 손상되지 않는다.
- [ ] 세로 화면 회전/액티비티 재생성 뒤 탭과 선택 과정이 안전한 상태로 복원된다.
- [ ] 레슨 중 프로세스 재생성 후 홈으로 돌아가더라도 저장 완료 기록이 손상되지 않는다.
- [ ] 과정 테스트 중 프로세스 재생성 시 크래시하지 않고 안전한 상태로 복귀한다.

Android 16 target 변경 참고: https://developer.android.com/about/versions/16/behavior-changes-16

## 4. 마이크 / STT
- [ ] 최초 마이크 권한 요청에서 허용 후 영어 말하기가 동작한다.
- [ ] 최초 마이크 권한 거부 시 앱이 종료되지 않고 안내가 표시된다.
- [ ] 설정에서 마이크 권한을 다시 허용한 뒤 앱 재진입 시 말하기가 정상 동작한다.
- [ ] 음성 인식 버튼을 빠르게 연속 입력해도 중복 세션/오류 콜백이 겹치지 않는다.
- [ ] 무음 상태에서 timeout 안내 후 재시도할 수 있다.
- [ ] 네트워크 기반 STT 서비스가 끊긴 기기에서 오류 안내 후 재시도할 수 있다.
- [ ] 음성 인식 서비스가 없는 기기/에뮬레이터에서 크래시하지 않는다.

## 5. TTS / 연속 듣기
- [ ] 영어 TTS 음성이 설치된 기기에서 레슨 TTS가 재생된다.
- [ ] 영어 TTS 음성이 없는 기기에서 안내 후 앱을 계속 사용할 수 있다.
- [ ] 한국어 TTS가 없는 기기에서 `영어 → 해석` 모드가 안전하게 중단되고 안내된다.
- [ ] 연속 듣기 도중 다른 탭으로 이동하면 이전 음성이 멈춘다.
- [ ] 연속 듣기 도중 레슨/섹션을 바꾸면 오래된 콜백이 다음 항목을 재생하지 않는다.
- [ ] 빈/비정상 재생 데이터에서도 크래시하지 않는다.

## 6. 앱 리소스 / 개인정보 / 권한 정책
- [ ] 홈 화면/앱 서랍에서 `Yamone English` 앱 이름이 정상 표시된다.
- [ ] 일반/원형/adaptive launcher icon이 제조사 런처에서 잘리지 않고 의도한 형태로 보인다.
- [ ] Android 16 QPR 계열 자동 테마 아이콘 표시를 확인하고 필요 시 monochrome adaptive icon 레이어를 추가한다.
- [x] CI에서 release APK의 package/version/minSdk/targetSdk/`RECORD_AUDIO`/`INTERNET` 부재를 자동 검증한다.
- [ ] 실제 서명된 최종 AAB/APK에서도 요청 런타임 권한이 의도와 같은지 다시 확인한다.
- [ ] 앱 자체가 직접 네트워크 API를 호출하지 않는 현재 버전 정책을 최종 소스와 대조한다.
- [ ] `allowBackup=false` 정책이 제품 의도와 맞는지 출시 전에 최종 승인한다.
- [ ] `docs/PRIVACY_DATA_SAFETY.md`의 현재 데이터 흐름을 실제 앱과 최종 대조한다.
- [ ] `docs/PRIVACY_POLICY_DRAFT.md`의 TODO를 채워 공개 가능한 개인정보처리방침 URL을 준비한다.
- [ ] 개인정보처리방침에 RECORD_AUDIO 사용 목적과 SpeechRecognizer/TTS가 기기·서비스 제공자에 의해 처리될 수 있음을 반영한다.
- [ ] Play Console Data safety 문항은 실제 출시 빌드·선택한 음성 서비스 동작·Google Play 정의를 기준으로 최종 답변한다. 저장소 초안을 그대로 제출하지 않는다.

## 7. Play 스토어 메타데이터
- [ ] `docs/PLAY_STORE_METADATA.md`에서 최종 한국어 앱 이름/짧은 설명/상세 설명을 확정한다.
- [ ] 영문 스토어 등록을 사용할 경우 영어 앱 이름/짧은 설명/상세 설명을 확정한다.
- [ ] 앱 카테고리를 Education으로 최종 확인한다.
- [ ] Play Console의 실제 대상 연령을 결정한다. 앱 내부 `5~7세` 등은 영어권 표현 수준 구분이므로 법적/스토어 대상 연령과 자동 동일시하지 않는다.
- [ ] 실제로 아동을 대상 사용자에 포함한다면 적용되는 Families/아동 관련 정책을 별도로 검토한다.
- [ ] 아이콘/feature graphic/휴대폰 및 필요한 폼팩터 스크린샷을 Play Console의 현재 요구 사양으로 제작한다.
- [ ] 콘텐츠 등급 설문을 실제 앱 기능 기준으로 완료한다.

## 8. Play App Signing / 릴리스 서명
현재 기본 CI의 release APK/AAB는 **unsigned 빌드 검증용**이다. 실제 작업 순서는 [`docs/PLAY_SIGNING_INTERNAL_TEST.md`](docs/PLAY_SIGNING_INTERNAL_TEST.md)에 상세 정리한다.

### 저장소 보안
- [x] `*.jks`, `*.keystore`, `keystore.properties`, 개인키 계열 파일을 `.gitignore`에서 제외한다.
- [x] 키가 없는 환경에서도 unsigned release 빌드가 계속 성공하도록 유지한다.
- [x] 키가 있을 때만 로컬 비추적 properties 또는 `YAMONE_UPLOAD_*` 환경 변수로 조건부 release signing을 활성화한다.
- [ ] 실제 비밀번호/keystore가 Git 히스토리나 workflow YAML에 들어가지 않았는지 최초 키 연결 전에 재확인한다.

### 실제 upload key / Play App Signing
- [ ] Play Console에서 새 앱을 만들고 Play App Signing 상태를 확인한다.
- [ ] 업로드 키(upload key)용 keystore를 안전한 오프라인/비밀 저장소에 생성한다.
- [ ] upload key 인증서 유효기간을 Android 공식 릴리스 요구에 맞게 확인한다.
- [ ] 업로드 키의 alias, 인증서 SHA-256, 백업 위치와 복구 절차를 기록한다. 비밀번호나 keystore 파일은 Git에 커밋하지 않는다.
- [ ] `bundleRelease` AAB를 upload key로 서명한다.
- [ ] 서명된 AAB/APK의 인증서를 `jarsigner`/`apksigner`/`keytool` 등으로 검증한다.
- [ ] Play Console 내부 테스트 릴리스에 서명된 AAB를 업로드한다.
- [ ] Play App Signing이 관리하는 app-signing key와 로컬 upload key 역할을 구분해 기록한다.
- [ ] 최초 open testing/production 롤아웃 전에 Play App Signing 키 정책을 최종 승인한다.

공식 참고:
- https://developer.android.com/studio/publish/app-signing
- https://support.google.com/googleplay/android-developer/answer/9842756

## 9. 내부 테스트 / 출시 후보 QA
- [ ] Play Console 내부 테스트 트랙에서 실제 설치한다.
- [ ] 내부 테스트에서 v0.8.5 신규 설치 경로를 확인한다.
- [ ] 이전 내부 테스트 버전에서 v0.8.5로 업데이트해 로컬 기록 호환성을 확인한다.
- [ ] 최소 1대의 Android 16 휴대폰 또는 에뮬레이터에서 전체 핵심 흐름을 확인한다.
- [ ] 최소 1대의 대화면/태블릿 Android 16 환경에서 핵심 화면을 확인한다.
- [ ] Play pre-launch report의 크래시/ANR/접근성/레이아웃 경고를 검토한다.
- [ ] 내부 테스트 뒤 치명적 오류가 없을 때만 production 준비로 이동한다.

## 10. 자동화로 확인하는 항목
CI에서 다음을 자동 확인하도록 구성합니다.
- Android 16(API 36) SDK에서 컴파일
- AGP 8.10.1 / Gradle 8.11.1 / JDK 17 빌드 조합
- 500레슨 카탈로그 무결성
- 과정별 1~100 번호와 내부 ID 안정성
- 6턴 대화/어순/발음 데이터 누락
- 과정 분리 및 홈 99/100·100/100 흐름
- 기존 SharedPreferences/assessment 저장소 이름·키
- 손상된 assessment 점수와 review 오류 횟수 보정 순수 로직
- 잘못 복원된 탭 인덱스와 보조 탭 뒤로가기 목표 순수 로직
- Android `lintDebug`
- debug APK 컴파일 및 패키징
- unsigned release APK/AAB 컴파일
- release APK `applicationId=com.yamone.english`
- release APK `versionCode=33`, `versionName=0.8.5`
- release APK `minSdk=26`, `targetSdk=36`
- release APK `RECORD_AUDIO` 권한 존재
- release APK의 의도하지 않은 `INTERNET` 권한 부재
- release AAB 생성/비어 있지 않음

실제 기기 동작, 실제 upload key 생성/서명, Android developer verification, Play App Signing, Data safety 최종 응답, 개인정보처리방침 공개 URL, Play Console 내부 테스트/제출은 이 자동화 범위에 포함하지 않습니다.
