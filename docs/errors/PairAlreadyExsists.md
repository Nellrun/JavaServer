#Ошибка: Пара уже существует <a name="PairAlreadyExists"/>
Данная ошибка возникает при использовании метода [schedule.add](#schedule.add), если данное время у заданной учебной группы есть учебная пара

##Параметры
| Название     | Назначение     | Значение
| :------------- | :------------- | :------------- |
| **codeError**      | Код ошибки |  6     
**errorName** | Имя ошибки | Пара уже существует
**errorText** | Текст ошибки | Ошибка. У данной группы в этой время уже есть пара

##Пример

###Ответ сервера

```
{
  "errorCode": 6,
  "errorName": "Пара уже существует",
  "errorText": "Ошибка. У данной группы в этой время уже есть пара"
}
```