#!/usr/bin/env python3
"""Validate the SHOHIN LUGHAT offline dictionary JSON file.

Usage:
  python tools/validate_dictionary.py dictionary/words.json
"""
import json
import sys
import unicodedata
from pathlib import Path

ALLOWED_CATEGORIES = {
    "Муошират", "Оила", "Ғизо", "Хона", "Маориф", "Кор",
    "Тиҷорат", "Сафар", "Саломатӣ", "Табиат", "Технология", "Ҷомеа",
}

def norm(value):
    return " ".join(unicodedata.normalize("NFC", value).casefold().split())

def main():
    path = Path(sys.argv[1] if len(sys.argv) > 1 else "dictionary/words.json")
    data = json.loads(path.read_text(encoding="utf-8"))
    if not isinstance(data, list):
        raise SystemExit("ERROR: top-level JSON value must be an array")

    seen_en = {}
    errors = []
    for index, item in enumerate(data, start=1):
        if not isinstance(item, dict):
            errors.append(f"#{index}: entry must be an object")
            continue
        en, tg, category = item.get("en"), item.get("tg"), item.get("category")
        if not isinstance(en, str) or not en.strip():
            errors.append(f"#{index}: missing English headword")
        if not isinstance(tg, str) or not tg.strip():
            errors.append(f"#{index}: missing Tajik translation")
        if category not in ALLOWED_CATEGORIES:
            errors.append(f"#{index}: invalid category {category!r}")
        if isinstance(en, str) and en.strip():
            key = norm(en)
            if key in seen_en:
                errors.append(f"#{index}: duplicate English headword {en!r} (first at #{seen_en[key]})")
            else:
                seen_en[key] = index
        if isinstance(item.get("source"), str) and not item["source"].strip():
            errors.append(f"#{index}: source must not be blank when present")

    print(f"Entries: {len(data)}")
    print(f"Unique English headwords: {len(seen_en)}")
    print(f"Target: 20,000")
    print(f"Remaining: {max(0, 20000 - len(seen_en))}")
    if errors:
        print(f"Validation errors: {len(errors)}")
        for error in errors[:100]:
            print(" - " + error)
        raise SystemExit(1)
    print("Validation: PASS")

if __name__ == "__main__":
    main()
