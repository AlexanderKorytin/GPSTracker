# Проект Волшебный Колобок

##### Приложение для определения текущих координат и записи треков передвижения с использованием библиотеки open street map.

На данный момент приложение в процессе разработки - пока не реализовано отправка тетущих координат через Bluetooth®.
-----

#### для связи telegram: @koritin84

-----

#### совместимость: Android 10 (min SDK 29)

-----

#### Версия языка kotlin.android' version '1.8.0''
Gradle JDK version '17.0.9'
#### Зависимости: Room, coroutines, fragment, glide, gson, retrofit2, peko, org.osmdroid:osmdroid-android (maven {url  = uri("https://jitpack.io")})

-----
### Технологии:
Git,  XML,  JSON,  Kotlin,  Room,  MVVM,  Android SDK,  Single Activity,  Fragments,  Retrofit2,  ConstraintLayout,  RecyclerView,  SharedPreferences,  Permissions, Gradle, Coroutines, Flow, LiveData, Jetpack Navigation Component, Koin, Glide 

-----
### Инструкция по установке

1. Откройте Android studio
2. Нажмите кнопку "Get from VCS"
3. В поле "URL:" вставте ссылку [github](https://github.com/AlexanderKorytin/GPSTracker.git) на этот проект и нажмите "clone"
4. при необходимости скачайте нужную версию Gradle JDK

### Инструкция по эксплуатации

1 При запуске приложения будет запрошено разрешение на точную геолокацию и при получении - диалог запроса разрешения на фоновую геолокацию (для работы сервиса в фоне), далее откроется экран с картой и текущем местоположением:

![Screenshot_20240319-215322](https://github.com/AlexanderKorytin/GPSTracker/assets/124441554/67b60031-17fc-448b-8734-ecb5595d5b9f)

Здесь: 

- при нажатии на иконку вверху спава - текущее местоположение отобразиться по центру экрана
- при нажатии иконки сторой свеху справа - запуститься сервис, записывающий параметры текущего трека (скорость, расстояние, средняя скорость и массив координат). Трек отрисовывается на экране. При повторном нажатии на эту иконку - откроется диалог сохранения текущего трека.

Сервис будет работать при закрытом приложении при наличии разрешения на фоновую геолокацию, в притивном случае остановиться.

2 Экран списка треков:

![Screenshot_20240319-215330](https://github.com/AlexanderKorytin/GPSTracker/assets/124441554/5b12c99d-26d4-46aa-80d2-081f72dcfb0e)

Пока не реализована отработка нажатий на трек.

3 Экран настроек: 



Здесь можно выбрать интервал обновления координат и цвет линиии трека.

![Screenshot_20240319-215336](https://github.com/AlexanderKorytin/GPSTracker/assets/124441554/01c0b192-6dd0-4549-97a1-428a356a7ba8)
