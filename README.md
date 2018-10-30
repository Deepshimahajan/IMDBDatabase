# IMDBDatabase

Developed a data analysis application for imdb.com’s user review data. The emphasis is on the database infrastructure of the 
application.

## Dataset

The dataset used for the project is an extension of MovieLens10M dataset, published by GroupLeans research group, 
http://www.grouplens.org. The datadset links the movies of MovieLens dataset with their corresponding web pages at Internet 
Movie Database (IMDb) and Rotten Tomatoes movie review systems.http://www.imdb.com http://www.rottentomatoes.com
The dataset includes 2113 users, 10197 movies, 855598 ratings and 13222 tags.

## Users

The primary users of this application will be users seeking movies and their ratings that match their search criteria.
1) movie search and 2) user search. In the first step, movie attributes such as genre, year, country, cast, and user’s tags (e.g. tags that viewers assigned to movies) can be selected to search for movies.

## Search 

We use Faceted search. It is a technique for accessing information organized according to a faceted classification system, allowing users to explore a collection of information by applying multiple filters.

the user will search for the movies from various categories that have the properties (attributes) the user is looking for. In the second step, movie ids from the results of the previous step in addition to user’s tags can be used to search for users/viewers. The result of this search will show users that assigned selected tags to the selected movies.
