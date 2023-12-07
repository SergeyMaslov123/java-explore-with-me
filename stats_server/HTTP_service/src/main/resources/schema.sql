create TABLE IF NOT EXISTS Hits (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  app VARCHAR(255) NOT NULL,
  uri VARCHAR(255) NOT NULL,
  ip VARCHAR(255) NOT NULL,
  time_create timestamp NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (id)
);