# SHOHIN LUGHAT

**English ↔ Тоҷикӣ** — an offline bilingual dictionary for Android.

## Target release
- 20,000 reviewed English–Tajik headwords and useful expressions
- English → Tajik and Tajik → English search
- Sequential numbering for every dictionary entry
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
- `number`: unique sequential position, starting at 1
- `partOfSpeech`: optional part of speech
- `example`: optional bilingual example
- `source`: provenance for review and attribution

## Build
Every push to `main` starts the GitHub Actions workflow. The debug APK is uploaded as an Actions artifact.

## Current development stage
The native Android app now loads its dictionary from a bundled UTF-8 JSON asset. Category filters have been removed; each word displays its permanent sequence number. The dictionary is organized into ten sequential groups of up to 2,000 entries each. Groups 01 and 02 are frozen at 2,000 entries each (1–4,000). Group 03 is frozen at 1,869 entries (4,001–5,869). Group 04 contains 12 unique sourced additions (5,870–5,881). Group 05 is in progress with 1,434 entries (5,882–7,315). All recovered legacy entries were checked for duplicate English headwords, missing translations, and sequential numbering. The current assembled target is 7,315 unique English headwords and expressions. Remaining work toward the 20,000 target: 12,685 entries. The dataset combines editorial entries and sourced practical vocabulary. Each imported record retains its `source` field. The dataset has not yet received a complete independent native-speaker review and must not be described as fully verified. GitHub Actions assembles all group files and supplemental entries into the APK, skipping exact duplicate English headwords.


## Data attribution

A portion of the Tajik translations is derived from the English Wiktionary translation tables, extracted through the [Multilingualism dataset](https://github.com/MethodoxTech/Multilingualism) and filtered to exclude entries marked as machine-translated. Attribution: **Wiktionary contributors**, via **MethodoxTech/Multilingualism**. The imported source data is distributed under **CC BY-SA 4.0**; the combined dictionary data must retain compatible attribution and share-alike terms. Each imported record identifies this provenance in its `source` field. No machine-translated rows from that source were imported.



Additional practical vocabulary sources:
- [JBLMFLCL Tajik language course](https://jblmflc.com/Tajik/PDF/): vocabulary lists in Lessons 3, 10, and 15. Tajik Cyrillic spellings were standardized during editorial import.
- [Wikivoyage Tajik phrasebook](https://en.wikivoyage.org/wiki/Tajik): everyday phrases, travel, places, time, numbers, and food vocabulary. Wikivoyage text is available under CC BY-SA 3.0; retain attribution and share-alike terms for reused material.

The additional practical batch includes editorially composed entries. Source labels are retained per entry; translations and phrase naturalness still require independent review by a fluent Tajik speaker.

See [DATA_LICENSE.md](DATA_LICENSE.md) for the data licence and attribution details.
