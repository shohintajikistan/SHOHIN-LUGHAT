# SHOHIN LUGHAT

**English ↔ Тоҷикӣ** — an offline bilingual dictionary for Android.

## Android APK
- APK packaging is intentionally paused while the content and navigation are being restored and checked.
- The planned universal APK will include the website interface, full dictionary JSON, story covers, 24 conversation topics, and offline content.
- The app is designed to work without an account, server, or API key.
- A signed Release APK will be prepared only after the content and app behavior have been checked. A permanent private release keystore is required for stable updates.

## Added learning sections
- Phrasebook: eight practical situations with English–Tajik expressions.
- Sentence builder: ten offline word-order exercises.
- Sentence translation: thirty prepared English–Tajik sentence pairs, searchable in both languages.
- Numbers and dates: English number names through 999,999, spoken time, weekdays, and months.
- Business English: workplace communication, interviews, CV terms, and business email phrases.
- The five sections are bundled for offline use and do not replace the dictionary or existing conversation/story sections.

## Dictionary target and quality
- Target: 20,000 reviewed English–Tajik headwords and useful expressions.
- Current assembled dataset: 7,315 unique English headwords and expressions.
- English → Tajik and Tajik → English search.
- Sequential numbering, local favorites, and search history.
- No automatic translation just to reach the target count.
- The dataset has not yet received a complete independent native-speaker review and must not be described as fully verified.

## Content preparation
Every push to `main` and manual workflow dispatch assembles the dictionary from the frozen source groups, publishes `dictionary/words.json` for the GitHub Pages website, validates the Android dictionary asset, and packages the story cover images for offline use. This workflow does not build an APK.

## Data format
The bundled dictionary uses UTF-8 JSON records with:
- `en`: English headword or expression
- `tg`: Tajik translation in Cyrillic
- `number`: unique sequential position
- `partOfSpeech`: optional part of speech
- `example`: optional bilingual example
- `source`: provenance for review and attribution

## Data attribution
A portion of the Tajik translations is derived from the English Wiktionary translation tables, extracted through the [Multilingualism dataset](https://github.com/MethodoxTech/Multilingualism) and filtered to exclude entries marked as machine-translated. Attribution: **Wiktionary contributors**, via **MethodoxTech/Multilingualism**. The imported source data is distributed under **CC BY-SA 4.0**; the combined dictionary data must retain compatible attribution and share-alike terms. Each imported record identifies this provenance in its `source` field.

Additional practical vocabulary sources:
- [JBLMFLCL Tajik language course](https://jblmflc.com/Tajik/PDF/): vocabulary lists in Lessons 3, 10, and 15. Tajik Cyrillic spellings were standardized during editorial import.
- [Wikivoyage Tajik phrasebook](https://en.wikivoyage.org/wiki/Tajik): everyday phrases, travel, places, time, numbers, and food vocabulary. Wikivoyage text is available under CC BY-SA 3.0; retain attribution and share-alike terms for reused material.

The current assembled dictionary contains 7,315 unique entries; the 51 supplemental candidates are duplicates of existing headwords and are skipped. The additional practical batch includes editorially composed entries. Source labels are retained per entry; translations and phrase naturalness still require independent review by a fluent Tajik speaker.

See [DATA_LICENSE.md](DATA_LICENSE.md) for the data licence and attribution details.
