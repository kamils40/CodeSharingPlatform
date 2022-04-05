# CodeSharingPlatform
Project is a web platform that works similar to pastebin
How to use:
View Restriction mean ammount of views possible before deleting code from database, time restiction means how much time (in seconds) code is available before deleting
GET request to /api/code/{UUID} to get JSON of code with given UUID
GET request to /api/code/latest gives 10 most recent code snippets in JSON format that has no restriction given
GET request /code/{UUID} and /code/latest works the same as their api counterparts but gives HTML code back
POST request to /api/code/new with JSON body as parameter containing given fields: code, views and time. Code is a string, views and time integers.
Views and time equal zero means no restriction given.
HTML pages are created via freemarker templates.
