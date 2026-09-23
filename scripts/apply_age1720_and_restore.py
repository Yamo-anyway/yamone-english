from pathlib import Path
import runpy

script_path = Path("scripts/apply_age1720.py")
script_text = script_path.read_text(encoding="utf-8")
bad_quote = "'Do you know how you're going to handle everything?'"
good_quote = "\"Do you know how you're going to handle everything?\""
if bad_quote not in script_text:
    raise RuntimeError("expected generator quoting pattern not found")
script_text = script_text.replace(bad_quote, good_quote, 1)

workflow_delete = "for rel in ['scripts/apply_age1720.py', '.github/workflows/bootstrap-age1720.yml']:"
source_only_delete = "for rel in ['scripts/apply_age1720.py']:"
if workflow_delete not in script_text:
    raise RuntimeError("expected generator cleanup pattern not found")
script_text = script_text.replace(workflow_delete, source_only_delete, 1)
script_path.write_text(script_text, encoding="utf-8")

runpy.run_path("scripts/apply_age1720.py", run_name="__main__")
Path('scripts/apply_age1720_and_restore.py').unlink(missing_ok=True)
print('Prepared source commit without changing workflow files.')
