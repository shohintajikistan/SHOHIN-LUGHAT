# Dictionary dataset

The production dataset is stored as `words.json` in UTF-8 JSON format. Each record must contain:
- `number`: unique sequential position in the complete dictionary, starting at 1
- `en`: English headword or expression
- `tg`: Tajik translation in Cyrillic

Optional fields are `partOfSpeech`, `example`, and `source`. Categories are not used. The number is global across the full dictionary and must remain consecutive after every update.

Run `python tools/validate_dictionary.py dictionary/words.json` before committing a new batch. The validator checks required fields, sequential numbering, and duplicate English headwords. The release target is 20,000 unique entries. Do not use machine-generated placeholder translations to inflate the count.
