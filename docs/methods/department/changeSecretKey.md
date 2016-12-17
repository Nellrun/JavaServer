#Метод: deparment.changeSecretKey<a name="deparment.changeSecretKey"/>

Данный метод позволяет изменить **любому** преподавателю секретный ключ своей кафедры.  
Данный метод доступен только для авторизованных преподавателей

##Параметры
| Название     | Назначение     |
| :------------- | :------------- |
| **token**       | Уникальный ключ доступа пользователя  <br>**Строка**
**secretKey** | Новый секретный ключ для регистрации преподаватлей на данной кафедре.  Ключ не более 50 символов <br> **Строка**       

##Ответ запроса
В качестве ответа возвращается успешный статус код (200) или ошибка, если во время выполнения метода произошла ошибка.

##Ошибки
Данный метод возвращает стандартные ошибки.  
Стандартные ошибки определены [в данном разделе](#errors)
Так же есть специальные ошибки:
- [Доступ запрещен](#AccessDenidedError)

##Пример

###Параметры запроса
| Параметры | Значение параметра     |
| :------------- | :------------- |
| **token**       | niy4vkrlif2mmpbpdlixx3q1       |
**secretKey** | VerySecretKey!

###Ответ сервера

```
statuscode: 200
```