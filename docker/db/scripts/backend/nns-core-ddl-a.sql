use nns-core;

commit;

CREATE TABLE notifications (
  id bigint unsigned NOT NULL AUTO_INCREMENT,
  /* msisdnA */
  destination_phone varchar(11) NOT NULL,
  /* msisdnB */
  source_phone varchar(11) NOT NULL,
  expiry_date datetime,
  web_id varchar(50),
  created datetime,
  updated datetime,
  created_by varchar(50),
  updated_by varchar(50),
  optlock int unsigned DEFAULT 0,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

commit;
