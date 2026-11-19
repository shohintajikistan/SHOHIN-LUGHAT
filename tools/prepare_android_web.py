#!/usr/bin/env python3
"""Package the current SHOHIN LUGHAT website and story artwork into Android assets."""
import re
import shutil
import urllib.request
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
INDEX = ROOT / "index.html"
DICTIONARY = ROOT / "app/src/main/assets/dictionary/words.json"
WWW = ROOT / "app/src/main/assets/www"
COVERS = [
    "https://images.unsplash.com/photo-1511818966892-d7d671e672a2?auto=format&fit=crop&w=900&q=80&fm=jpg",
    "https://images.unsplash.com/photo-1512389142860-9c449e58a543?auto=format&fit=crop&w=900&q=80&fm=jpg",
    "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=900&q=80&fm=jpg",
    "https://images.unsplash.com/photo-1511497584788-876760111969?auto=format&fit=crop&w=900&q=80&fm=jpg",
    "https://images.unsplash.com/photo-1500375592092-40eb2168fd21?auto=format&fit=crop&w=900&q=80&fm=jpg",
    "https://images.unsplash.com/photo-1470252649378-9c29740c9fa8?auto=format&fit=crop&w=900&q=80&fm=jpg",
    "https://images.unsplash.com/photo-1416879595882-3373a0480b5b?auto=format&fit=crop&w=900&q=80&fm=jpg",
    "https://images.unsplash.com/photo-1553284965-83fd3e82fa5a?auto=format&fit=crop&w=900&q=80&fm=jpg",
    "https://images.unsplash.com/photo-1507842217343-583bb7270b66?auto=format&fit=crop&w=900&q=80&fm=jpg",
]

html = INDEX.read_text(encoding="utf-8")
match = re.search(r"const storyCovers\s*=\s*\[(.*?)\];", html, re.S)
if not match:
    raise SystemExit("Could not find storyCovers array in index.html")
found_urls = re.findall(r'"(https://images\.unsplash\.com/[^"]+)"', match.group(1))
if len(found_urls) != len(COVERS):
    raise SystemExit(f"Expected {len(COVERS)} story cover URLs, found {len(found_urls)}")

cover_dir = WWW / "assets/story-covers"
cover_dir.mkdir(parents=True, exist_ok=True)
local_urls = []
for index, url in enumerate(COVERS, 1):
    filename = f"story-{index:02d}.jpg"
    request = urllib.request.Request(url, headers={"User-Agent": "SHOHIN-LUGHAT-Android-Build/1.0"})
    with urllib.request.urlopen(request, timeout=45) as response:
        data = response.read()
        content_type = response.headers.get("Content-Type", "")
    if not content_type.startswith("image/") or len(data) < 5000:
        raise SystemExit(f"Story cover download failed or was not an image: {filename} ({content_type}, {len(data)} bytes)")
    (cover_dir / filename).write_bytes(data)
    local_urls.append(f'"https://shohin.local/assets/story-covers/{filename}"')
    print(f"Bundled {filename}: {len(data):,} bytes")

replacement = "const storyCovers=[" + ",".join(local_urls) + "];"
html = html[:match.start()] + replacement + html[match.end():]
WWW.mkdir(parents=True, exist_ok=True)
(WWW / "index.html").write_text(html, encoding="utf-8")

if not DICTIONARY.exists():
    raise SystemExit("Prepared offline dictionary is missing")
dictionary_target = WWW / "dictionary/words.json"
dictionary_target.parent.mkdir(parents=True, exist_ok=True)
shutil.copy2(DICTIONARY, dictionary_target)
print(f"Packaged app HTML: {(WWW / 'index.html').stat().st_size:,} bytes")
print(f"Packaged offline dictionary: {dictionary_target.stat().st_size:,} bytes")
print(f"Bundled story covers: {len(local_urls)}")
