# SHOHIN LUGHAT

**English ↔ Тоҷикӣ** — an offline bilingual dictionary for Android.

## Target release
- 20,000 reviewed English–Tajik headwords and useful expressions
- English → Tajik and Tajik → English search
- 12 thematic categories
- Offline search; no API, server, or remote database at runtime
- Favorites saved on the device
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
The native Android app now loads its dictionary from a bundled UTF-8 JSON asset. The bundled dictionary currently contains **6,398 unique English headwords** across the 12 categories. **13,602 additional entries** are still required to reach the 20,000 target. The dataset combines editorial entries with sourced Wiktionary translations. Imported entries retain their source metadata; the dataset has not yet received a complete independent native-speaker review and must not be described as fully verified. GitHub Actions validates the JSON and copies it into the APK before building.


## Data attribution

A portion of the Tajik translations is derived from the English Wiktionary translation tables, extracted through the [Multilingualism dataset](https://github.com/MethodoxTech/Multilingualism) and filtered to exclude entries marked as machine-translated. Attribution: **Wiktionary contributors**, via **MethodoxTech/Multilingualism**. The imported source data is distributed under **CC BY-SA 4.0**; the combined dictionary data must retain compatible attribution and share-alike terms. Each imported record identifies this provenance in its `source` field. No machine-translated rows from that source were imported.

See [DATA_LICENSE.md](DATA_LICENSE.md) for the data licence and attribution details.
