#!/usr/bin/env python3
"""Merge curated supplemental entries into the offline dictionary without deleting data."""
import json
import unicodedata
from pathlib import Path

base = Path("dictionary/words.json")
extra = Path("dictionary/additions.json")
out = Path("app/src/main/assets/dictionary/words.json")

def norm(value):
    return " ".join(unicodedata.normalize("NFC", value).casefold().split())

words = json.loads(base.read_text(encoding="utf-8"))
batch = json.loads(extra.read_text(encoding="utf-8"))
if not isinstance(words, list) or not isinstance(batch, list):
    raise SystemExit("ERROR: dictionary files must contain JSON arrays")
seen = {norm(item["en"]) for item in words if isinstance(item, dict) and isinstance(item.get("en"), str)}
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
print(f"Base entries: {len(words) - added}")
print(f"Supplemental candidates: {len(batch)}")
print(f"New unique entries added: {added}")
print(f"Duplicates skipped: {skipped}")
print(f"Total offline entries: {len(words)}")
