DROP TABLE PBX.ZIP

CREATE TABLE PBX.ZIP(
 "ЗИП ID" NUMBER(50) NOT NULL PRIMARY KEY,
 "Наименование ЗИП" VARCHAR(25) NOT NULL,
 "Серийный номер" VARCHAR(25) NOT NULL,
 "Общее Кол-во" NUMBER(10) NOT NULL,
 "Используется (кол-во)" NUMBER(50) NOT NULL,
 "Свободно в резерве (кол-во)" NUMBER(50),
 "Кем из абонентов взято в резерв" NUMBER(50),
 "Ящик" VARCHAR(25) NOT NULL,
 "Помещение" VARCHAR(25) NOT NULL,
 "Признак устройства" VARCHAR(25) NOT NULL,
 "Признак вхождения в состав устройства" VARCHAR(25) NOT NULL, 
 "Код ОКП" VARCHAR(25),
 "Где применяется" VARCHAR(25),
 "Количество в изделии" NUMBER(10)
	
) 

ALTER TABLE PBX.ZIP
ADD   FOREIGN KEY("Серийный номер")
REFERENCES PBX.DEVSTAT("Серийный номер")

ALTER TABLE PBX.ZIP
ADD FOREIGN KEY("Кем из абонентов взято в резерв")
REFERENCES PBX.SUBSCRIBERS("ID Абонента")

ALTER TABLE PBX.ZIP
ADD  FOREIGN KEY( "ЗИП ID")
REFERENCES PBX.DEVSTAT("ID Устройства")