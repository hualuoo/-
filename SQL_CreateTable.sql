/*��¼����û���*/
CREATE TABLE user_table(
Username CHAR(10) NOT NULL,
Password CHAR(32) NOT NULL,
)


/*ҵ����Ϣ    ONoҵ�����    ONameҵ������    OSexҵ���Ա�    OTelҵ���绰    OIDҵ�����֤����    ONoteҵ����ע*/
CREATE TABLE Owner_Info(
ONo CHAR(6) NOT NULL CONSTRAINT O_Prim PRIMARY KEY,
OName CHAR(8) NOT NULL,
OSex NCHAR(2) NOT NULL,
OTel CHAR(13) NOT NULL,
OID CHAR(18) NOT NULL,
ONote CHAR(100))

/*������Ϣ    HNo���ݱ��    HBuild����¥���    HPark���ݵ�Ԫ��    HFloor����¥���    HRoom�������ƺ�    HArea�������    HState����״̬    HType���ݻ���    HNote���ݱ�ע    ONoҵ�����*/
CREATE TABLE House_Info(
HNo CHAR(12) NOT NULL CONSTRAINT H_Prim PRIMARY KEY,
HBuild INT NOT NULL,
HPark INT NOT NULL,
HFloor INT NOT NULL,
HRoom INT NOT NULL,
HArea INT NOT NULL,
HState CHAR(6) NOT NULL,
HType CHAR(8) NOT NULL,
HNote CHAR(100),
ONo CHAR(6) CONSTRAINT House_Owner_Fore FOREIGN KEY REFERENCES Owner_Info(ONo))

/*��λ��Ϣ    PNo��λ���    PRegion��λ����    CarNo���ƺ�    PState��λ״̬    ONoҵ�����*/
CREATE TABLE Parking_Info(
PNo CHAR(6) NOT NULL CONSTRAINT P_Prim PRIMARY KEY,
PRegion CHAR(1) NOT NULL,
CarNo CHAR(8),
PState CHAR(6) NOT NULL,
ONo CHAR(6) CONSTRAINT Parking_Owner_Fore FOREIGN KEY REFERENCES Owner_Info(ONo)
)

/*ɾ�����ݵ�ͬʱɾ��ҵ����Ϣ*/
USE Sql_Curriculum_Design
GO
CREATE TRIGGER del_H ON House_Info
AFTER DELETE
AS
	DELETE FROM Owner_Info
	WHERE Owner_Info.ONo
	IN (SELECT ONo FROM DELETED)
GO

/*�޸�ҵ��ID��ͬʱ�޸ķ��ݵ�ҵ��ID*/
USE Sql_Curriculum_Design
GO
CREATE TRIGGER Update_O ON Owner_Info
FOR UPDATE
AS
	IF UPDATE(ONo)
	UPDATE House_Info SET ONo=inserted.ONo
	FROM House_Info,deleted,inserted
	WHERE House_Info.ONo=deleted.ONo
GO