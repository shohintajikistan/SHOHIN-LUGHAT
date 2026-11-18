# Dictionary dataset

The production dataset will be stored here as `words.json` in UTF-8 JSON format. Each record must contain `en`, `tg`, and `category`; optional fields are `partOfSpeech`, `example`, and `source`.

Run `python tools/validate_dictionary.py dictionary/words.json` before committing a new batch. The validator checks required fields, approved categories, and duplicate English headwords. The release target is 20,000 unique, reviewed entries. Do not use machine-generated placeholder translations to inflate the count.
