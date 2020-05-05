-- ## run h2 via bash
-- sh /home/ildm/Dropbox/h2/bin/h2.sh 
-- on the production the path willbe: /etc/asterisk/db/h2/bin/h2.sh
-- ##

-- ##Connection to db from code 
-- login:
-- password:
-- URL:
-- login with sa and perform: 
CREATE USER ASTERISK PASSWORD 'redalert'
ALTER USER ASTERISK ADMIN TRUE
-- then login by USER ASTERISK  and perform sql stats below:
-- ## ##

-- ## Sql stats
CREATE USER ASTERISK PASSWORD 'redalert'
CREATE SCHEMA PBX AUTHORIZATION ASTERISK
-- ##create tables:

DROP TABLE PBX.SUBSCRIBERS

CREATE TABLE PBX.SUBSCRIBERS(
 "ID Абонента" NUMBER(50) NOT NULL PRIMARY KEY,
 "Абонент" VARCHAR(25) NOT NULL,
 "Тел. номер" NUMBER(4) NOT NULL, 
 "Звание"  VARCHAR(25) NOT NULL,
 "Должность"  VARCHAR(25) NOT NULL, 
 "Пост"  VARCHAR(25) NOT NULL,
 "Помещение" VARCHAR(25) NOT NULL,
 "Дата последнего разговора" VARCHAR(25), /*int, long*/
 "Серийный номер используемого устройства" VARCHAR(25) NOT NULL,
 "Использован ли резерв" BOOLEAN, /**Триггер правдивости*/
 "Количество устройств взятых из резерва" NUMBER(1), /*Триггер подсчета*/
 "В сети" BOOLEAN
 /*
 ,
 FOREIGN KEY("ID Абонента") REFERENCES PBX.DEVSTAT("ID Устройства"),
 FOREIGN KEY("Серийный номер используемого устройства") REFERENCES PBX.DEVSTAT("Серийный номер"),
 FOREIGN KEY("Количество устройств взятых из резерва") REFERENCES PBX.ZIP("ID Устройства")		
*/
)

DROP TABLE PBX.DEVSTAT

CREATE TABLE PBX.DEVSTAT(
 "ID Устройства" NUMBER(50) NOT NULL PRIMARY KEY,
 "Наименование устройства" VARCHAR(25) NOT NULL,
  "Серийный номер" VARCHAR(25) NOT NULL,
 "IP-адрес" VARCHAR(25) NOT NULL,
 "Кол-во отказов" NUMBER(10),
 "В сети" BOOLEAN,
 "Состояние" VARCHAR(25) NOT NULL,
 "Время работы" TIME,
 "Кем используется" NUMBER(50) NOT NULL
 /*
 ,
 FOREIGN KEY("Наименование устройства") REFERENCES ZIP("Наименование ЗИП"),
 FOREIGN KEY("Серийный номер") REFERENCES ZIP("Серийный номер"),
 FOREIGN KEY("Кем используется") REFERENCES PBX.SUBSCRIBERS("ID Абонента")
*/
)

CREATE TABLE PBX.ZIP(
 "ЗИП ID" NUMBER(50) NOT NULL PRIMARY KEY,
 "Наименование ЗИП" VARCHAR(25) NOT NULL,
 "Серийный номер" VARCHAR(25) NOT NULL,
 "Общее Кол-во" NUMBER(10) NOT NULL,
--  Первоначально поля "Свободно в резерве" и "Общее Кол-во" равны.Как только устройство берется в использованием кем-либо, 
--  то срабатывает триггер пересчета взятого в резерв устройства: если 		устройство взято кем-то в использование, то отнимается единица от поля "Свободно в резерве (кол-во)" и прибавляется к полю "Используется (кол-во)" и прибавляется к полю 
 "Используется" (кол-во) NUMBER(50) NOT NULL,
 "Свободно в резерве (кол-во)" NUMBER(50),
 "Кем из абонентов взято в резерв" NUMBER(50),
 "Ящик" VARCHAR(25) NOT NULL,
 "Помещение" VARCHAR(25) NOT NULL,
 "Признак устройства" VARCHAR(25) NOT NULL,
 "Признак вхождения в состав устройства" VARCHAR(25) NOT NULL 
 
--  Поля на всякий случай. Они соответствуют конструкторской спецификации:
--  "Код ОКП" VARCHAR(25),
--  "Где применяется" VARCHAR(25),
--  "Количество в изделии" NUMBER(10)

--  ,FOREIGN KEY("Серийный номер") REFERENCES PBX.DEVSTAT("Серийный номер"),
--  FOREIGN KEY("Кем из абонентов взято в резерв") REFERENCES PBX.SUBSCRIBERS("ID Абонента"),
--  FOREIGN KEY( "ЗИП ID") REFERENCES PBX.DEVSTAT("ID Устройства")
 		
)  


-- test fd

