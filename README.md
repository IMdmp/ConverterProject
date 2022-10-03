# ConverterProject

### Note: API provided in technical exam is not working anymore. it has been changed to apilayer.
ref: https://github.com/exchangeratesapi/exchangeratesapi/issues/117

new api layer documentation: https://apilayer.com/marketplace/exchangerates_data-api

endpoints used:
- `/symbols` (for supported currencies)
- `/convert` (for actual conversion)

### Setting up api key
in local.properties, add this: (no quotes "")
```
API_KEY = <key>
```

### Libraries used
- Dagger2 + Hilt 
- Jetpack compose
- Retrofit, okhttp, gson
- kotlin coroutines + flow
- jetpack room

### Architecture
#### MVVVM + state hoisting in compose
`Service` and/or `DAO` ->   `Repository` -> `UseCase` -> `ViewModel` -> `UI ` 

