create table Account
(
    Account_ID VARCHAR(36) not null unique,
    Password VARCHAR(512) not null,
    Type VARCHAR(8),
    PRIMARY KEY (Account_ID)
);

create table Login_Token
(
    Token_ID VARCHAR(36) not null unique,
    Issue_Datetime DATETIME not null,
    Expiry_time LONG not null,
    Account_ID VARCHAR(36) not null,
    PRIMARY KEY (Token_ID),
    FOREIGN KEY (Account_ID) REFERENCES Account(Account_ID)
);

create table User
(
    User_ID VARCHAR(32) not null unique,
    Email VARCHAR(128) not null,
    Account_ID VARCHAR(36) not null unique,
    PRIMARY KEY (User_ID),
    FOREIGN KEY (Account_ID) REFERENCES Account(Account_ID)
);

create table HVAC_Model
(
    Model_ID INTEGER not null AUTO_INCREMENT unique,
    Name VARCHAR(128) unique,
    Image VARCHAR(128),
    Configuration_Data VARCHAR(1024),
    PRIMARY KEY (Model_ID)
);

create table Location
(
    Location_ID INTEGER not null AUTO_INCREMENT unique,
    Country VARCHAR(64),
    State VARCHAR(64),
    City VARCHAR(64),
    PRIMARY KEY (Location_ID)
);

create table Weather_Forecast
(
    Forecast_ID INTEGER not null AUTO_INCREMENT unique,
    Date VARCHAR(64) not null,
    Data_From_API VARCHAR(5120) not null,
    Location_ID INTEGER not null,
    PRIMARY KEY (Forecast_ID),
    FOREIGN KEY (Location_ID) REFERENCES Location(Location_ID)
);

create table Schedule
(
    Schedule_ID INTEGER not null AUTO_INCREMENT unique,
    Schedule_Data VARCHAR(5120),
    Device_Serial VARCHAR(36) not null,
    PRIMARY KEY (Schedule_ID)
);

create table Thermostat_Device
(
    Serial_Number VARCHAR(36) not null unique,
    Model_ID INTEGER,
    Location_ID INTEGER,
    User_ID VARCHAR(32),
    Account_ID VARCHAR(36) not null unique,
    Active_Schedule INTEGER,
    PRIMARY KEY (Serial_Number),
    FOREIGN KEY (Model_ID) REFERENCES HVAC_Model(Model_ID),
    FOREIGN KEY (Location_ID) REFERENCES Location(Location_ID),
    FOREIGN KEY (User_ID) REFERENCES User(User_ID),
    FOREIGN KEY (Account_ID) REFERENCES Account(Account_ID),
    FOREIGN KEY (Active_Schedule) REFERENCES Schedule(Schedule_ID)
);

create table Cost
(
    Cost_ID INTEGER not null AUTO_INCREMENT unique,
    Date VARCHAR(10),
    Amount FLOAT,
    isPrediction VARCHAR(5),
    Accuracy FLOAT,
    Serial_Number VARCHAR(36) not null,
    Schedule_ID INTEGER,
    Forecast_ID INTEGER,
    PRIMARY KEY (Cost_ID),
    FOREIGN KEY (Serial_Number) REFERENCES Thermostat_Device(Serial_Number),
    FOREIGN KEY (Schedule_ID) REFERENCES Schedule(Schedule_ID),
    FOREIGN KEY (Forecast_ID) REFERENCES Weather_Forecast(Forecast_ID)
);

ALTER TABLE Schedule ADD FOREIGN KEY (Device_Serial) REFERENCES Thermostat_Device(Serial_Number);
