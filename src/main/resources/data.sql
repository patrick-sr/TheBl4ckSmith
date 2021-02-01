DROP TABLE IF EXISTS AlertDomain;

CREATE TABLE AlertDomain (
  ticket VARCHAR(55) PRIMARY KEY,
  telephony VARCHAR(250) NOT NULL,
  acknowledge int,
  countCalls int,
  createDate DATE
);