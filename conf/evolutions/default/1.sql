# User schema
# --- !Ups
create table `products` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `description` VARCHAR(255),
  `price` BIGINT
);

INSERT INTO `scala`.`products` (`id`, `name`, `description`, `price`) VALUES ('1', 'nazwa1', 'opis1', '20');
INSERT INTO `scala`.`products` (`id`, `name`, `description`, `price`) VALUES ('2', 'nazwa2', 'opis2', '30');
INSERT INTO `scala`.`products` (`id`, `name`, `description`, `price`) VALUES ('3', 'nazwa3', 'opis3', '16');
INSERT INTO `scala`.`products` (`id`, `name`, `description`, `price`) VALUES ('4', 'nazwa4', 'opis4', '66');


# --- !Downs
drop table if exists `products`;

