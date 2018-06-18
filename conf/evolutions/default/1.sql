# User schema
# --- !Ups
create table `products` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `description` TEXT NOT NULL,
  `price` int NOT NULL,
  `availability` SMALLINT NOT NULL,
  `quantity` BIGINT DEFAULT 0 NOT NULL,
  `category_name` VARCHAR(255) NOT NULL
);

# --- !Downs
drop table if exists `products`;

