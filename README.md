# SHOHIN LUGHAT

**English ↔ Тоҷикӣ** — an offline bilingual dictionary for Android.

## Android APK
- A single universal APK is built by GitHub Actions for Android devices from Android 6.0 onward.
- The application packages the current website interface, dictionary JSON, and story cover images inside the APK.
- Dictionary search and story illustrations work offline after installation.
- No account, server, API key, or internet permission is required by the app.
- Build artifact: `SHOHIN-LUGHAT-APK` in the latest successful **Build SHOHIN LUGHAT APK** workflow run.
- The current APK is a debug-signed testing/distribution build. A private release keystore is required before publishing stable signed updates or distributing through Google Play.

## Dictionary target and quality
- Target: 20,000 reviewed English–Tajik headwords and useful expressions.
- Current assembled dataset: 7,315 unique English headwords and expressions.
- English → Tajik and Tajik → English search.
- Sequential numbering, local favorites, and search history.
- No automatic translation just to reach the target count.
- The dataset has not yet received a complete independent native-speaker review and must not be described as fully verified.

## Android build
Every push to `main` and manual workflow dispatch starts the Android APK build. The workflow assembles and validates the dictionary, packages the web interface and story illustrations into Android assets, builds the APK, and uploads it as an Actions artifact.

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

The additional practical batch includes editorially composed entries. Source labels are retained per entry; translations and phrase naturalness still require independent review by a fluent Tajik speaker.

See [DATA_LICENSE.md](DATA_LICENSE.md) for the data licence and attribution details.
