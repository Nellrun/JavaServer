# Ошибка: Пара уже существует <a name="PairAlreadyExists"></a>
Данная ошибка возникает при использовании метода [schedule.add](#schedule.add), если в данное время у преподавателя есть учебная пара

## Параметры
| Название     | Назначение     | Значение
| :------------- | :------------- | :------------- |
| **codeError**      | Код ошибки |  6
| **errorName** | Имя ошибки | Пара уже существует
| **errorText** | Текст ошибки | Ошибка. У данного преподавателя в это время уже есть пара

## Пример

### Ответ сервера

```
{
  "errorCode": 6,
  "errorName": "Пара уже существует",
  "errorText": "Ошибка. У данного преподавателя в это время уже есть пара"
}
```