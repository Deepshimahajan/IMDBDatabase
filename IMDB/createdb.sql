create table movies (
	mID INTEGER PRIMARY KEY,
 	title varchar(200), 
	year varchar(200), 
	rtAllCriticsRating varchar(200),
	rtAllCriticsNumReviews varchar(200),
	rtTopCriticsRating varchar(200),
	rtTopCriticsNumReviews varchar(200),
	rtAudienceRating varchar(200),
	rtAudienceNumRatings varchar(200)
    );

create table tags (
	tagID INTEGER PRIMARY KEY,
	tag_text varchar(200)
	);

create table movie_genres (
	movieID INTEGER,
	genre varchar(200),
	PRIMARY KEY(movieID,genre)
	);

create table movie_countries (
	movieID INTEGER ,
	country varchar(200),
	PRIMARY KEY(movieID,country)
	);

create table movie_locations (
	movieID INTEGER,
	location1 varchar(200),
	location2 varchar(200),
	location3 varchar(200),
	location4 varchar(200), 
--	PRIMARY KEY(movieID,location1,location2,location3,location4),
	FOREIGN KEY (movieID) REFERENCES movies(mID)
	 ON DELETE CASCADE
	 );

create table movie_tags (
	movieID INTEGER,
	tagID INTEGER, 
	tagWeight varchar(200), 
	PRIMARY KEY(movieID,tagID),
	FOREIGN KEY(movieID) REFERENCES movies(mID),
	FOREIGN KEY(tagID) REFERENCES tags(tagID) 
	ON DELETE CASCADE
	);

commit;


--create index indexmovieid on movies(mID); --already index
--create index indextag on tags(tagID); -- already indexed 
create index indexgenre on movie_genres(genre);
create index indexcountry on movie_countries(country);
create index indexlocation on movie_locations(location1);