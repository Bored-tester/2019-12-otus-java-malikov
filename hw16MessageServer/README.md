Мульисервисная юзер админка
Группа 2019-12

###Составные части:

**message-server** - маршрутизатор сообщений между сервисами. Слушает захардкоженый порт 8083.
После старта поднимает два инстанса UI на портах 8086б, 8087 и два инстанса бэкендов на портах 8084 и 8085 
(см MessageSystemStartupListener).

**message-client** - либа, которую должен использовать сервис для пересылки сообщений. 
Использует server.port перменную, чтобы поднять свой сокет для обмена сообщениями с сервером.
При инициализации регистрируется у message-server, используя композитный айдишник MessageClientId, 
состоящий из имени подписывающегося сервиса, хоста и порта. 
Сервисы, регистрирующиеся под одним именем считаются равнозначными и будут получать сообщения по round Robin.

**messaging-api** - либа для общих классов сервера и клиента.  
`Из важного косячного:` хранит enum MessageType, содержащий все возможные типы сообщений системы.
Стоило бы вынести этот класс в либу сервисов, но оставил так, ибо не захотел ради двух шареных енамов запиливать либу)

**admin-backend** - поднимает свой инстанс хибернэйт базы с юзерами.

**admin-ui** - поднимает ui для юзер админки. Используйте http://localhost:<port>/login чтобы попасть в админку.
Можно использовать дефолтные креды или креды любого юзера с ролью ADMIN.  
`Из важного косячного:`  
1) Если вы логинетесь в один ui, то сессия в другом ui протухает, и нужен релогин, чтобы оно заработало.
Я бы оставил так, так как аутентификация не входит в требования к дз, а выпиливание\переделка не относится к теме дз,
но могу исправить, если это принципиально важно.  
2) Так как бэкенды каждый поднимает свою независимую базу, а фронты попадают в бэкенды раунд робином, 
то после создания новых юзеров, они будут через раз показываться в полном списке юзеров.

###Как запускать

Если через Spring boot плагин
1. Установите working directory в `...hw16MessageServer/message-server/target`
2. Я использовал jdk версии 13.0.2. Если дефолтный другой, то задайте JAVA_HOME в environment variables явно 
на мою версию или выше.
3. Main class -> ru.otus.UserAdminMessageServer.

Если черз cmd
1. Перейдите в `...hw16MessageServer/message-server/target`
2. Проверьте версию жавы (см пункт 2 выше).
3. Запускайте `java -jar ./message-server.jar`

После запуска смотрите лог message server'а. Через несколько секунд должны появится такие строчки (в любом порядке):

`new client:MessageClientId(name=FrontendService, host=http://localhost, port=8087)`  
`new client:MessageClientId(name=FrontendService, host=http://localhost, port=8086)`  
`New client FrontendService was registered on port: 8087`  
`New client FrontendService was registered on port: 8086`  
`new client:MessageClientId(name=BackendService, host=http://localhost, port=8084)`  
`New client BackendService was registered on port: 8084`  
`new client:MessageClientId(name=BackendService, host=http://localhost, port=8085)`  
`New client BackendService was registered on port: 8085`

Логи admin-ui и admin-backend упадут в `...hw16MessageServer/message-server/target`. 
Называются: admin-backend1.log, admin-backend2.log, admin-ui1.log, admin-ui2.log 

Теперь можно идти на `http://localhost:<port>/login` где порт равен либо 8086, либо 8087.