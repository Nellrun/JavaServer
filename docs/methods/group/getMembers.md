#Метод: group.getMembers<a name="group.getMembers"/>

Данный метод возвращает список студентов, которые обучаются в заданной учебной группы.  
Возвращает информацию о студентах обучающихся в группе с заданным **id**

##Параметры
| Название     | Назначение     |
| :------------- | :------------- |
| **id**       | Положительное число, которое характеризует уникальный идентификатор группы.  <br>**Целое число**       

##Ответ запроса
В качестве ответа возвращается json список состоящий из объектов с заданными полями:

| Название        | Назначение     |
| :------------- | :------------- |
|**id**               | Уникальный идентификатор студента
|**firstName**       | Имя пользователя
**secondName**      | Фамилия
**middleName**      | Отчество
**groupID** | Уникальный идентификатор учебной группы
**groupShortName** | Короткое имя группы
**levelOfAccess** | Права доступа в группе. Подробнее о правах доступа можно прочитать в [данном разделе](#levelOfAccess)


##Ошибки
Данный метод возвращает стандартные ошибки.  
Стандартные ошибки определены [в данном разделе](#errors)

##Пример

###Параметры запроса
| Параметры | Значение параметра     |
| :------------- | :------------- |
| **id**       | 639       |

###Ответ сервера

```
[
  {
    "id": 1,
    "firstName": "Даниил",
    "secondName": "Щесняк",
    "middleName": "Сергеевич",
    "groupID": 639,
    "groupShortName": "ИВТб-32",
    "levelOfAccess": 0
  },
  {
    "id": 2,
    "firstName": "Никита",
    "secondName": "Братухин",
    "middleName": "",
    "groupID": 639,
    "groupShortName": "ИВТб-32",
    "levelOfAccess": 0
  },
  {
    "id": 3,
    "firstName": "Алексей",
    "secondName": "Губин",
    "middleName": "",
    "groupID": 639,
    "groupShortName": "ИВТб-32",
    "levelOfAccess": 0
  }
]
```