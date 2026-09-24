# Yamone English — Play 서명 / 내부 테스트 작업 체크리스트

> 상태: **실행 준비 문서**. 실제 upload key 생성, Play Console 등록, 서명된 AAB 업로드, 내부 테스트 설치는 아직 완료로 간주하지 않습니다.

## 1. 키 역할 먼저 구분

Google Play의 신규 앱은 Play App Signing을 사용합니다.

- **App signing key**: 최종 사용자에게 배포되는 APK를 Google Play가 서명할 때 사용하는 키. Play App Signing에서 Google이 관리합니다.
- **Upload key**: 개발자가 Play Console에 올릴 AAB를 서명하는 키. 개발자가 안전하게 보관합니다.
- Upload key와 app signing key는 같은 역할이 아닙니다. upload key가 노출되거나 분실된 경우 Play 절차를 통해 upload key 재설정을 요청할 수 있지만, app signing key 취급은 별도입니다.

공식 참고:
- https://developer.android.com/studio/publish/app-signing
- https://support.google.com/googleplay/android-developer/answer/9842756

## 2. 저장소 보안 원칙

다음 파일/값은 Git에 커밋하지 않습니다.

- `*.jks`, `*.keystore`, `*.p12`, `*.pfx`, 개인키 파일
- `keystore.properties`, `signing.properties`
- store password / key password / alias가 포함된 비밀 설정

현재 프로젝트는 위 패턴을 `.gitignore`에 포함합니다.

## 3. Upload key 생성 — 수동 작업

예시 명령입니다. 실제 경로, alias, 비밀번호는 안전한 값으로 직접 결정합니다.

```bash
keytool -genkeypair -v \
  -keystore /secure/path/yamone-english-upload.jks \
  -alias yamone-english-upload \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000
```

Android 릴리스 서명용 인증서는 충분한 유효기간을 가져야 합니다. Android 공식 릴리스 준비 문서의 인증서 유효기간 요구도 제출 시점에 다시 확인합니다.

생성 후 인증서 정보를 확인합니다.

```bash
keytool -list -v \
  -keystore /secure/path/yamone-english-upload.jks \
  -alias yamone-english-upload
```

별도로 기록할 항목:

- alias
- SHA-256 인증서 fingerprint
- keystore의 오프라인/암호화 백업 위치
- 복구 담당자/복구 절차

비밀번호 자체를 일반 문서나 저장소에 적지 않습니다.

## 4. 로컬 서명 연결 방법

프로젝트는 실제 키가 없을 때 기존처럼 **unsigned release**를 빌드하고, 아래 4개 값이 모두 제공되면 upload key 서명을 활성화합니다.

### 방법 A — 로컬 `keystore.properties`

저장소 루트에 Git 비추적 파일 `keystore.properties`를 만듭니다.

```properties
storeFile=/absolute/path/to/yamone-english-upload.jks
storePassword=TODO_LOCAL_SECRET
keyAlias=yamone-english-upload
keyPassword=TODO_LOCAL_SECRET
```

이 파일은 `.gitignore` 대상입니다.

### 방법 B — 환경 변수

```bash
export YAMONE_UPLOAD_STORE_FILE=/absolute/path/to/yamone-english-upload.jks
export YAMONE_UPLOAD_STORE_PASSWORD='...'
export YAMONE_UPLOAD_KEY_ALIAS='yamone-english-upload'
export YAMONE_UPLOAD_KEY_PASSWORD='...'
```

CI에서 사용할 경우 GitHub 저장소 코드나 workflow YAML에 값을 직접 쓰지 않고 비밀 저장소/secret 기능을 사용합니다. 현재 기본 CI는 실제 서명 secret을 요구하지 않으며 unsigned release 검증을 계속 수행합니다.

## 5. 서명된 Release AAB 생성

키 연결 후:

```bash
gradle :app:bundleRelease
```

예상 위치:

```text
app/build/outputs/bundle/release/app-release.aab
```

AAB JAR 서명 확인 예시:

```bash
jarsigner -verify -verbose -certs \
  app/build/outputs/bundle/release/app-release.aab
```

Release APK도 함께 빌드했다면 Android SDK `apksigner`로 인증서를 확인합니다.

```bash
gradle :app:assembleRelease
$ANDROID_HOME/build-tools/35.0.0/apksigner verify --print-certs \
  app/build/outputs/apk/release/app-release.apk
```

확인 항목:

- 인증서 SHA-256이 기록한 upload key fingerprint와 일치
- package: `com.yamone.english`
- versionCode / versionName이 해당 출시 버전과 일치
- targetSdk 36
- 예상하지 않은 권한이 추가되지 않음

## 6. Play Console / Android developer verification

2026년 9월 배포 환경에서는 Android developer verification과 package registration 상태를 Play Console에서 반드시 확인합니다.

- [ ] Play Console 개발자 신원 확인 상태 점검
- [ ] `com.yamone.english` package registration 상태 점검
- [ ] 신규 Play 앱 생성 과정에서 package가 자동 등록되었는지 확인
- [ ] 자동 등록되지 않은 경우 Play Console 안내에 따라 소유권 확인
- [ ] **2026-09-30 시행 상태를 실제 계정에서 최우선 확인**

공식 참고:
- https://developer.android.com/developer-verification
- https://support.google.com/googleplay/android-developer/answer/16984799

## 7. Play App Signing 연결

- [ ] Play Console에서 Yamone English 앱 생성
- [ ] Play App Signing 상태 확인
- [ ] app-signing key가 Google Play 관리 키임을 확인
- [ ] 로컬 upload key 인증서 등록/연결 절차 완료
- [ ] Play Console에 표시되는 upload certificate fingerprint를 로컬 기록과 대조
- [ ] 키/인증서 정보는 공개 저장소에 올리지 않음

## 8. 내부 테스트 첫 업로드

- [ ] `versionCode`가 이전 업로드보다 큰 서명된 `app-release.aab` 생성
- [ ] Test and release → Internal testing에서 새 release 생성
- [ ] 서명된 AAB 업로드
- [ ] Play Console의 target API, 권한, 정책 경고 확인
- [ ] Data safety / 개인정보처리방침 / 대상 연령 / 콘텐츠 등급 등 필수 항목의 미완료 경고 확인
- [ ] 테스터 목록 또는 Google Group 구성
- [ ] 내부 테스트 release 게시
- [ ] 테스터 opt-in 링크에서 설치 가능 상태 확인

## 9. 신규 설치 / 업데이트 호환 검증

내부 테스트에서 최소 다음 두 경로를 각각 확인합니다.

### 신규 설치

- [ ] 5개 과정과 총 500레슨 구조 정상
- [ ] 마이크 권한 허용/거부 모두 안전
- [ ] TTS/STT 기본 흐름 정상
- [ ] 과정 종료 테스트 진입 정상

### 이전 버전에서 업데이트

- [ ] 기존 설치 버전에서 Play 내부 테스트 버전으로 업데이트
- [ ] `selected_course` 유지
- [ ] `completed` 유지
- [ ] `review` 유지
- [ ] `thinking_completed` 유지
- [ ] `expression_completed` 유지
- [ ] 5개 assessment 기록 유지
- [ ] 레슨 내부 ID 호환 유지

## 10. 내부 테스트 후 승인 기준

Production 준비로 넘어가기 전에:

- [ ] Android 16 휴대폰 핵심 흐름 완료
- [ ] Android 16 태블릿/대화면 핵심 흐름 완료
- [ ] Play pre-launch report의 crash/ANR/접근성/레이아웃 경고 검토
- [ ] 서명 인증서/업데이트 설치 문제 없음
- [ ] 공개 개인정보처리방침 URL 확정
- [ ] Play Data safety 최종 응답 확정
- [ ] 대상 연령/Families 적용 여부 최종 결정
- [ ] 스토어 스크린샷/feature graphic/아이콘 준비

이 문서의 체크박스는 실제 기기와 Play Console에서 직접 확인한 뒤에만 완료 처리합니다.
