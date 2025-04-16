drop database if exists ligr_test;
create database ligr_test;
use ligr_test;

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
    fav_game_id int null,
    date_joined date,
    region varchar(5),
    profile_description text,
    preferred_genre varchar(20),
	constraint fk_fav_game_id
		foreign key (fav_game_id) references game(game_id) 
        on delete set null
);

create table `user`(
	user_id int primary key auto_increment,
    profile_id int not null,
    username varchar(25),
    email varchar(50) unique,
    password varchar(100),
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

delimiter //
create procedure set_known_good_state()
begin

    delete from game_review;
    alter table game_review auto_increment = 1;
    delete from `user`;
    alter table `user` auto_increment = 1;
    delete from `profile`;
    alter table `profile` auto_increment = 1;
    delete from game;
    alter table game auto_increment = 1;
    
	insert into game (title, developer, genre, year_released, platform, region) values
	("Deleted Game", "N/A", "N/A", 0, "N/A", "OTHER"),
	("Default Game", "N/A", "N/A", 0, "N/A", "OTHER");
 
	insert into profile (fav_game_id, date_joined, region, profile_description, preferred_genre) values
	(1, '2000-01-01', 'OTHER', 'Placeholder account for a deleted user!', 'None');
    
	insert into `user` (profile_id, username, email, `password`, isAdmin) values
	(1, "Deleted_User", "deleted@user.com", "deleteduser", true);
    
	insert into game (title, developer, genre, year_released, platform, region) values
	('Chrono Break', 'Square Enix', 'RPG', 2023, 'PC', 'JP'),
	('Star Defender', 'NovaCore Studios', 'Shooter', 2022, 'Xbox', 'NA'),
	('Mystic Hollow', 'DreamForge', 'Adventure', 2021, 'Switch', 'EU'),
	('Cyber Rally X', 'Hyperbyte', 'Racing', 2024, 'PS5', 'NA'),
	('Shadow Circuit', 'NebulaWorks', 'Action', 2020, 'PC', 'OTHER');
    
    
	insert into profile (fav_game_id, date_joined, region, profile_description, preferred_genre) values
	(2, '2023-01-15', 'JP', 'Long-time JRPG fan, loves turn-based combat.', 'RPG'),
	(3, '2022-07-08', 'NA', 'FPS enthusiast and streamer.', 'Shooter'),
	(4, '2021-11-20', 'EU', 'Admin account for moderation purposes.', 'Strategy'),
	(5, '2024-03-01', 'NA', 'Speedrunner who loves a challenge.', 'Platformer'),
	(6, '2020-09-14', 'OTHER', 'Retro gamer with a passion for pixel art.', 'Action');

        
	insert into `user` (profile_id, username, email, `password`, isAdmin) values
	(2, 'gamer_girl91', 'gg91@example.com', 'p@ssword123', false),
	(3, 'noobmaster69', 'noob69@example.com', 'qwertyuiop', false),
	(4, 'admin_one', 'admin@example.com', 'adminpass', true),
	(5, 'speedrunner42', 'speed42@example.com', 'fastnfurious', false),
	(6, 'retro_rex', 'rexretro@example.com', 'classicgames', false);

        
	insert into game_review (game_id, user_id, review, rating) values
	(3, 2, 'Surprisingly addictive! Great controls and fast-paced action.', 4.5),
	(3, 5, 'Not bad, but the puzzles felt a bit too easy.', 3.2),
	(4, 6, 'Top-tier storytelling. This is how RPGs should be made.', 4.9);
    


end //
delimiter ;

