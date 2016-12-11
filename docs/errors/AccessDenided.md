#Ошибка: Отсутсвует параметр <a name="BadParameterFormat"/>
Данная ошибка случается при передаче неправильного ключа доступа пользователя,
либо при попытке обратиться к методу, который недоступен для данного пользователя

##Параметры
| Название     | Назначение     | Значение
| :------------- | :------------- | :------------- |
| **codeError**      | Код ошибки |  5
**errorName** | Имя ошибки | Ошибка доступа
**errorText** | Текст ошибки | Доступ запрещен

##Пример

###Ответ сервера

```
{
  "errorCode": 5,
  "errorName": "Ошибка доступа",
  "errorText": "Доступ запрещен"
}
```