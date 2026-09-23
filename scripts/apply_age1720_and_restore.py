from pathlib import Path
import runpy

script_path = Path("scripts/apply_age1720.py")
script_text = script_path.read_text(encoding="utf-8")
bad = "'Do you know how you're going to handle everything?'"
good = "\"Do you know how you're going to handle everything?\""
if bad not in script_text:
    raise RuntimeError("expected generator quoting pattern not found")
script_path.write_text(script_text.replace(bad, good, 1), encoding="utf-8")

runpy.run_path("scripts/apply_age1720.py", run_name="__main__")

original_workflow = '''name: Android CI

on:
  push:
    branches: [ "main" ]
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v5
        with:
          distribution: temurin
          java-version: "17"

      - name: Set up Gradle
        uses: gradle/actions/setup-gradle@v4
        with:
          gradle-version: "8.9"

      - name: Run catalog integrity tests
        run: gradle :app:testDebugUnitTest --stacktrace

      - name: Build debug APK
        run: gradle :app:assembleDebug --stacktrace

      - name: Upload debug APK
        uses: actions/upload-artifact@v4
        with:
          name: yamone-english-debug
          path: app/build/outputs/apk/debug/app-debug.apk
'''

Path('.github/workflows/android.yml').write_text(original_workflow, encoding='utf-8')
Path('scripts/apply_age1720_and_restore.py').unlink(missing_ok=True)
print('Restored Android workflow and removed one-shot wrapper.')
