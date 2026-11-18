# SHOHIN LUGHAT

**English ↔ Тоҷикӣ** — an offline bilingual dictionary for Android.

## Target release
- 20,000 reviewed English–Tajik headwords and useful expressions
- English → Tajik and Tajik → English search
- 12 thematic categories
- Offline search; no API, server, or remote database at runtime
- Favorites and recent search history
- Premium Emerald, Gold, Ivory, and Midnight visual identity

## Dictionary quality rules
- Count unique English headwords, not duplicated translations or inflected forms.
- Every entry must have a meaningful Tajik translation.
- Do not generate translations automatically just to reach the target count.
- Keep source and review metadata for every imported batch.
- Remove exact duplicates and review conflicting translations before release.
- The app must include all dictionary data locally in the APK.

## Data format
The bundled dictionary will use UTF-8 JSON records with these fields:
- `en`: English headword or expression
- `tg`: Tajik translation in Cyrillic
- `category`: one of the 12 app categories
- `partOfSpeech`: optional part of speech
- `example`: optional bilingual example
- `source`: provenance for review and attribution

## Build
Every push to `main` starts the GitHub Actions workflow. The debug APK is uploaded as an Actions artifact.

## Current development stage
The native Android foundation and first interface are in place. The app currently contains 50 starter entries. The 20,000-entry dictionary is a separate content milestone and is not yet complete.
