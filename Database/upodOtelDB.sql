CREATE DATABASE upodOtelDB;


CREATE TABLE roomType (
    id INT NOT NULL AUTO_INCREMENT,
    roomName VARCHAR(50),
    PRIMARY KEY (id)
);


CREATE TABLE extraFeature (
	id INT NOT NULL AUTO_INCREMENT,
	featureName VARCHAR(100),
	PRIMARY KEY(id)
);


CREATE TABLE feature (
	id INT NOT NULL AUTO_INCREMENT,
	featureName VARCHAR(100),
	PRIMARY KEY(id)
);


INSERT INTO feature (featureName) VALUES 
("Kettle");
INSERT INTO feature (featureName) VALUES 
("Phone");
INSERT INTO feature (featureName) VALUES 
("Hair Dryer");
INSERT INTO feature (featureName) VALUES 
("Air Conditioning");
INSERT INTO feature (featureName) VALUES 
("TV");


CREATE TABLE roomFeature(
	roomTypeId INT,
	featureId INT,
	extraFeatureId INT,
    FOREIGN KEY (roomTypeId) REFERENCES roomType(id),
	FOREIGN KEY (extraFeatureId) REFERENCES extraFeature(id),
	FOREIGN KEY (featureId) REFERENCES feature(id)
);


CREATE TABLE room (
    id INT NOT NULL AUTO_INCREMENT,
    roomTypeId INT,
    roomName VARCHAR(100),
    capacity INT,
    price DOUBLE,
    featureName VARCHAR(100),
    PRIMARY KEY (id),
    FOREIGN KEY (roomTypeId) REFERENCES roomType(id)
);


CREATE TABLE customer(
	id INT NOT NULL AUTO_INCREMENT,
	fullName VARCHAR(100),
	identityNumber VARCHAR(11),
	phoneNumber VARCHAR(10),
	birthDate DATE,
	description VARCHAR(255),
	PRIMARY KEY(id)
);


CREATE TABLE service(
	id INT NOT NULL AUTO_INCREMENT,
	serviceName VARCHAR(100),
	unitPrice DOUBLE,
	PRIMARY KEY(id)
);


CREATE TABLE reservation(
	reservationId INT NOT NULL AUTO_INCREMENT,
	roomId INT,
	checkInDate DATE,
	checkOutDate DATE,
	checkedInTime DATE,
	isCustomerAssigned TINYINT(1) NOT NULL DEFAULT 0,
	PRIMARY KEY(reservationId),
	FOREIGN KEY (roomId) REFERENCES room(id)
);


CREATE TABLE reservationCustomer(
	reservationId INT,
	customerId INT,
	FOREIGN KEY (reservationId) REFERENCES reservation(reservationId),
	FOREIGN KEY (customerId) REFERENCES customer(id)
);


CREATE TABLE reservationService(
	reservationServiceId INT NOT NULL AUTO_INCREMENT,
	reservationId INT,
	serviceId INT,
	quantity INT,
	PRIMARY KEY(reservationServiceId),
	FOREIGN KEY (reservationId) REFERENCES reservation(reservationId),
	FOREIGN KEY (serviceId) REFERENCES service(id)
);
