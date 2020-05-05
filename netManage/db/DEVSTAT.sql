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
)

ALTER TABLE PBX.DEVSTAT
ADD  FOREIGN KEY("Наименование устройства")
REFERENCES ZIP("Наименование ЗИП")

ALTER TABLE PBX.DEVSTAT
ADD  FOREIGN KEY("Серийный номер")
REFERENCES ZIP("Серийный номер")

ALTER TABLE PBX.DEVSTAT
ADD  FOREIGN KEY("Кем используется")
REFERENCES PBX.SUBSCRIBERS("ID Абонента")