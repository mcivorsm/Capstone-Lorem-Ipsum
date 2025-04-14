drop database if exists ligr;
create database ligr;
use ligr;

-- create tables and relationships

create table game (
	game_id int primary key auto_increment,
    title varchar(50),
    developer varchar (50),
    genre varchar(20),
    year_released int,
    platform varchar(25),
    region varchar(5)
);

create table `profile`(
	profile_id int primary key auto_increment,
    fav_game_id int not null,
    date_joined date,
    region varchar(5),
    profile_description text,
    preferred_genre varchar(20),
	constraint fk_fav_game_id
		foreign key (fav_game_id) references game(game_id)
);

create table `user`(
	user_id int primary key auto_increment,
    profile_id int not null,
    username varchar(25),
    email varchar(50) unique,
    password varchar(50),
    isAdmin boolean,
	constraint fk_profile_id
        foreign key (profile_id) references `profile` (profile_id)
);

create table game_review (
	review_id int primary key auto_increment,
    game_id int,
    user_id int,
    review text,
    rating numeric(2,1),
	constraint fk_game_id
		foreign key (game_id) references game(game_id),
	constraint fk_user_id
		foreign key (user_id) references `user` (user_id)
);

