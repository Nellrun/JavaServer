#Метод: user.logout <a name="user.logout"></a>
Данный метод обеспечивает корректный выход пользователя из его ученой записи.

##Параметры
| Название     | Назначение     |
| :------------- | :------------- |
| **token**      | Уникальный ключ пользователя.  <br>**Строка**

##Ответ запроса
В качестве ответа в коде статуса возвращается ответ от сервера.
Значение кодов статуса представлены [тут](#statusCode)


##Ошибки
Данный метод возвращает стандартные ошибки.  
Стандартные ошибки определены [в данном разделе](#errors)

##Пример

###Параметры запроса
| Параметры | Значение параметра     |
| :------------- | :------------- |
| **token**       | 8qv19jlmaubcho8j50r1r3th       |

###Ответ сервера

```
statusCode: 200
```