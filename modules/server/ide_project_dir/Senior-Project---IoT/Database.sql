create table Thermostat_Device
(
    Serial_Number VARCHAR,
    Model_ID INTEGER not null,
    Location_ID INTEGER,
    User_ID INTEGER not null,
    Account_ID INTEGER not null,
    Schedule_ID INTEGER,
    PRIMARY KEY (Serial_Number),
    FOREIGN KEY (Model_ID) REFERENCES HVAC_Model(Model_ID),
    FOREIGN KEY (Location_ID) REFERENCES Location(Location_ID),
    FOREIGN KEY (User_ID) REFERENCES User(User_ID),
    FOREIGN KEY (Account_ID) REFERENCES Account(Account_ID),
    FOREIGN KEY (Schedule_ID) REFERENCES Schedule(Schedule_ID)
);

create table HVAC_Model
(
    Model_ID INTEGER,
    Name CHAR,
    Image CHAR,
    Configuration_Data CHAR
);

create table Location
(
    Location_ID INTEGER,
    Country CHAR,
    State CHAR,
    City CHAR
);

create table Weather_Forecast
(
    Forecast_ID INTEGER,
    Date DATE,
    Data_From_API CHAR,
    Location_ID INTEGER not null,
    FOREIGN KEY (Location_ID) REFERENCES Location(Location_ID)
);

create table User
(
    User_ID INTEGER,
    Email CHAR
);

create table Account
(
    Account_ID INTEGER,
    Login_Hash CHAR,
    Type BIT,
    User_ID INTEGER,
    FOREIGN KEY (User_ID) REFERENCES User(User_ID)
);

create table Login_Token
(
    Token_ID INTEGER,
    Issue_Datetime DATETIME,
    Expiry_Datetime DATETIME,
    Account_ID INTEGER,
    FOREIGN KEY (Account_ID) REFERENCES Account(Account_ID)
);

create table Schedule
(
    Schedule_ID INTEGER,
    Schedule_Data VARCHAR
);

create table Cost
(
    Cost_ID INTEGER,
    Date DATE,
    Amount DECIMAL,
    isPrediction CHAR,
    Accuracy CHAR,
    Serial_Number INTEGER not null,
    Schedule_ID INTEGER,
    Forecast_ID INTEGER,
    FOREIGN KEY (Serial_Number) REFERENCES Thermostat_Device(Serial_Number),
    FOREIGN KEY (Schedule_ID) REFERENCES Schedule(Schedule_ID),
    FOREIGN KEY (Forecast_ID) REFERENCES Weather_Forecast(Forecast_ID)
);

