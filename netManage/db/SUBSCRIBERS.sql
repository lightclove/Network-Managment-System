CREATE TABLE PBX.SUBSCRIBERS(
 "ID Абонента" NUMBER(50) NOT NULL PRIMARY KEY,
 "Абонент" VARCHAR(25) NOT NULL,
 "Тел. номер" NUMBER(4) NOT NULL, 
 "Звание"  VARCHAR(25) NOT NULL,
 "Должность"  VARCHAR(25) NOT NULL,
 "Пост"  VARCHAR(25) NOT NULL,
 "Помещение" VARCHAR(25) NOT NULL,
 "Дата последнего разговора" VARCHAR(25),
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

ALTER TABLE PBX.SUBSCRIBERS
ADD FOREIGN KEY ("ID Абонента")
REFERENCES PBX.DEVSTAT("ID Устройства")

ALTER TABLE PBX.SUBSCRIBERS
ADD FOREIGN KEY ("Серийный номер используемого устройства")
REFERENCES PBX.DEVSTAT("Серийный номер")

ALTER TABLE PBX.SUBSCRIBERS
ADD FOREIGN KEY ("Количество устройств взятых из резерва")
REFERENCES PBX.ZIP("ЗИП ID")