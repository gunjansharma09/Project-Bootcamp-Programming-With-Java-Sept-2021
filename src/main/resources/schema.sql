create table if not exists oauth_access_token (
  token_id VARCHAR(255),
  token LONG VARBINARY,
  authentication_id VARCHAR(255) PRIMARY KEY,
  user_name VARCHAR(255),
  client_id VARCHAR(255),
  authentication LONG VARBINARY,
  refresh_token VARCHAR(255)
);
create table if not exists oauth_refresh_token (
  token_id VARCHAR(255),
  token LONG VARBINARY,
  authentication LONG VARBINARY
);
create table if not exists oauth_client_details (      /*Stores client details*/
  client_id VARCHAR(255) PRIMARY KEY,
  resource_ids VARCHAR(255),             /*Q1: is this comma separated list of resources?*/
  client_secret VARCHAR(255),
  scope VARCHAR(255),
  authorized_grant_types VARCHAR(255),
  web_server_redirect_uri VARCHAR(255),
  authorities VARCHAR(255),              /*Q2: what it this for?*/
  access_token_validity INTEGER,         /*Q3: Is this the validity period in seconds?*/
  refresh_token_validity INTEGER,
  additional_information VARCHAR(4096),  /*Q4: Can I omit this field if I don't need any additional information?*/
  autoapprove VARCHAR(255)               /*Q5: What does this mean?*/
);