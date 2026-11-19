#!/usr/bin/env python3
"""Assemble frozen 2,000-entry groups into the offline dictionary without rewriting them."""
import json
import re
import unicodedata
from pathlib import Path

groups_dir = Path("dictionary/groups")
legacy = Path("dictionary/words.json")
extra = Path("dictionary/additions.json")
out = Path("app/src/main/assets/dictionary/words.json")

def norm(value):
    return " ".join(unicodedata.normalize("NFC", value).casefold().split())

# Group files are immutable source batches. New work must go into the next group file.
group_files = sorted(groups_dir.glob("group_*.json"))
if not group_files:
    raise SystemExit("ERROR: no dictionary groups found")

words = []
seen = set()
for path in group_files:
    match = re.fullmatch(r"group_(\d{2})\.json", path.name)
    if not match:
        raise SystemExit(f"ERROR: invalid group filename: {path.name}")
    group_number = int(match.group(1))
    batch = json.loads(path.read_text(encoding="utf-8"))
    if not isinstance(batch, list) or len(batch) > 2000:
        raise SystemExit(f"ERROR: {path} must be an array of at most 2,000 entries")
    expected_start = len(words) + 1
    for offset, item in enumerate(batch):
        if not isinstance(item, dict):
            raise SystemExit(f"ERROR: invalid record in {path}")
        en, tg = item.get("en"), item.get("tg")
        expected_number = expected_start + offset
        if not isinstance(en, str) or not en.strip() or not isinstance(tg, str) or not tg.strip():
            raise SystemExit(f"ERROR: missing English or Tajik text in {path}, entry {offset + 1}")
        if item.get("number") != expected_number:
            raise SystemExit(f"ERROR: {path} entry {offset + 1} must have number {expected_number}")
        key = norm(en)
        if key in seen:
            raise SystemExit(f"ERROR: duplicate English headword in frozen groups: {en!r}")
        seen.add(key)
        words.append(item)

# Preserve any legacy entries if that file is populated in a future migration.
if legacy.exists() and legacy.stat().st_size:
    legacy_words = json.loads(legacy.read_text(encoding="utf-8"))
    if not isinstance(legacy_words, list):
        raise SystemExit("ERROR: dictionary/words.json must contain a JSON array")
    for item in legacy_words:
        if not isinstance(item, dict) or not isinstance(item.get("en"), str) or not item["en"].strip():
            raise SystemExit("ERROR: legacy entry has no English headword")
        if not isinstance(item.get("tg"), str) or not item["tg"].strip():
            raise SystemExit(f"ERROR: missing Tajik translation for {item['en']!r}")
        key = norm(item["en"])
        if key in seen:
            continue
        record = dict(item)
        record["number"] = len(words) + 1
        words.append(record)
        seen.add(key)

batch = json.loads(extra.read_text(encoding="utf-8")) if extra.exists() else []
if not isinstance(batch, list):
    raise SystemExit("ERROR: dictionary/additions.json must contain a JSON array")
added = 0
skipped = 0
for item in batch:
    if not isinstance(item, dict) or not isinstance(item.get("en"), str) or not item["en"].strip():
        raise SystemExit("ERROR: supplemental entry has no English headword")
    if not isinstance(item.get("tg"), str) or not item["tg"].strip():
        raise SystemExit(f"ERROR: missing Tajik translation for {item['en']!r}")
    key = norm(item["en"])
    if key in seen:
        skipped += 1
        continue
    record = dict(item)
    record["number"] = len(words) + 1
    words.append(record)
    seen.add(key)
    added += 1

out.parent.mkdir(parents=True, exist_ok=True)
out.write_text(json.dumps(words, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
print(f"Frozen group files: {len(group_files)}")
print(f"Frozen group entries: {sum(len(json.loads(p.read_text(encoding='utf-8'))) for p in group_files)}")
print(f"Legacy entries added: {len(words) - added - sum(len(json.loads(p.read_text(encoding='utf-8'))) for p in group_files)}")
print(f"Supplemental candidates: {len(batch)}")
print(f"New unique supplemental entries added: {added}")
print(f"Duplicates skipped: {skipped}")
print(f"Total offline entries: {len(words)}")
