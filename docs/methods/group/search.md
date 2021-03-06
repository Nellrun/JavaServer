# Метод: group.search<a name="group.search"></a>

Данный метод возвращает список групп, которые соотвествуют поисковому запросу.
Возвращает информацию о группах, которые имеют короткое имя как в параметре **name**

## Параметры
| Название     | Назначение     |
| :------------- | :------------- |
| **name**       | Поисковый запрос.  <br>**Строка**

## Ответ запроса
В качестве ответа возвращается json список из объектов с заданными полями:

| Название        | Назначение     |
| :------------- | :------------- |
| **id**               | Уникальный идентификатор группы<br>**Положительное число**
| **nameShort**       | Короткое имя группы<br>**Строка**
| **nameLong**      | Длинное имя группы<br>**Строка**
| **degree**      | Ученая степень группы (*Бакалавр*, *Специалитет*, *Магистратура*, *Аспирантура*)<br>**Строка**
| **formOfEducation** | Форма обучения группы (*Очная*, *Заочная*, *Дистанционная*)<br>**Строка**


## Ошибки
Данный метод возвращает [стандартные ошибки](#errors).<br>

## Пример

### Параметры запроса
| Параметры | Значение параметра     |
| :------------- | :------------- |
| **name**       | ивтб-3     |

### Ответ сервера

```
[
  {
    "id": 638,
    "nameShort": "ИВТб-31",
    "nameLong": "ИВТб-3301-01-00",
    "degree": "Бакалавр",
    "formOfEducation": "Очная"
  },
  {
    "id": 639,
    "nameShort": "ИВТб-32",
    "nameLong": "ИВТб-3302-02-00",
    "degree": "Бакалавр",
    "formOfEducation": "Очная"
  }
]
```
