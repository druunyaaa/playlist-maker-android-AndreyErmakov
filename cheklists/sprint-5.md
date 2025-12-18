# Чек-лист Sprint-5

- [x] Структура проекта разделена на слои Data, Domain, UI; пакеты creator и ui/activity созданы и используются.
- [x] MainActivity перенесена в ui/activity, путь в манифесте указан корректно
- [x] Эмулятор сервера (Storage) реализован: метод search корректно фильтрует треки по названию и исполнителю без учёта регистра.
- [x] Слой Data содержит TrackDto, TracksSearchRequest, TracksSearchResponse и RetrofitNetworkClient, все классы соответствуют заданию.
- [x] Слой Domain содержит интерфейсы NetworkClient и TracksRepository; репозиторий TracksRepositoryImpl преобразует TrackDto → Track и эмулирует задержку (delay(1000)).
- [x] При ошибке запроса (resultCode != 200) searchTracks возвращает пустой список (emptyList()).