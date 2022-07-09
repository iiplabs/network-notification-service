use nns-core;

commit;

CREATE TABLE notifications (
  id bigint unsigned NOT NULL AUTO_INCREMENT,
  /* msisdnA */
  dest_phone varchar(11) NOT NULL,
  /* msisdnB */
  source_phone varchar(11) NOT NULL,
  completion_date datetime,
  expiry_date datetime,
  next_attempt_date datetime,
  notification_status int,
  web_id varchar(50),
  created datetime,
  optlock int unsigned DEFAULT 0,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

commit;
