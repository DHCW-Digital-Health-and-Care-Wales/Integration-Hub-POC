USE master;
GO

-- Create database if not exists
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'MonitoringDatabase')
    BEGIN
        CREATE DATABASE MonitoringDatabase;
    END;
GO

USE MonitoringDatabase;
GO

-- Create Schema
IF NOT EXISTS (SELECT * FROM sys.schemas WHERE name = 'queue')
    BEGIN
        EXEC('CREATE SCHEMA queue');
    END;
GO

-- Create Table
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'queue' AND TABLE_NAME = 'APIEvents')
    BEGIN
        CREATE TABLE queue.APIEvents (
                                         EventID INT IDENTITY(1,1) PRIMARY KEY,
                                         EventType VARCHAR(250) NOT NULL,
                                         EventDateTime DATETIME2(0) NOT NULL,
                                         UniqueEventId VARCHAR(500) NOT NULL,
                                         APIName VARCHAR(250) NOT NULL,
                                         APIVersionNumber VARCHAR(50),
                                         APIServerName VARCHAR(250),
                                         EventMessage VARCHAR(MAX),
                                         EventInformation VARCHAR(5000),
                                         EventProcess VARCHAR(250)
        );
    END;
GO

-- Create Stored Procedure
CREATE OR ALTER PROCEDURE [queue].[prInsertAPIEvent]
    @EventType   VARCHAR(250),
    @EventDateTime  DATETIME2(0),
    @UniqueEventId  VARCHAR(500),
    @APIName   VARCHAR(250),
    @APIVersionNumber VARCHAR(50),
    @APIServerName  VARCHAR(250),
    @EventMessage  VARCHAR(MAX),
    @EventInformation VARCHAR(5000),
    @EventProcess  VARCHAR(250)
AS
BEGIN
    INSERT INTO queue.APIEvents
    (EventType, EventDateTime, UniqueEventId, APIName, APIVersionNumber, APIServerName, EventMessage, EventInformation, EventProcess)
    VALUES
        (@EventType, @EventDateTime, @UniqueEventId, @APIName, @APIVersionNumber, @APIServerName, @EventMessage, @EventInformation, @EventProcess);
END;
GO
