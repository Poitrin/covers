[![Build Status](https://travis-ci.com/Poitrin/covers.svg?branch=master)](https://travis-ci.com/Poitrin/covers)

# Covers app
This app has been developed for musicians who …
* know which instruments (synths, samples, …) have been used (or can be used) in a song (piece, …) and want to share this information with other musicians
  * E.g. I know that the [synth sound of the intro of Coldplay’s song Hypnotised](https://www.youtube.com/watch?v=WXmTEyq5nXc) can be obtained from a [Yamaha keyboard, with the preset “Stardust”](https://www.youtube.com/watch?v=QwKxPuqxJvI).
* play in a cover band and need to find sounds that are similar to the original sound
  * E.g. I have a Yamaha Motif XF8 synth and my band wants to cover Bon Jovi’s _It’s my life_. Which sounds do I need to select (what do I need to configure) in order to be close to the original?

## User Interface
### List all creative works (songs, pieces, …)
![List all creative works](https://raw.github.com/Poitrin/covers/master/doc/images/creative_work_list.png)

### Create a new creative work
![Create a new creative work](https://raw.github.com/Poitrin/covers/master/doc/images/creative_work_create.png)

### Get sound suggestions of a specific song
![Get sound suggestions of a specific song](https://raw.github.com/Poitrin/covers/master/doc/images/creative_work_show.png)

## Installation
If you have Docker (and docker-compose) installed, execute:
```
docker-compose up dev
```

## Testing
If you have Docker (and Docker compose) installed, execute:
```
docker-compose up test
```

## Deployment
(still needs to be written)

## Design decisions
(still needs to be written)

## To do / ideas
### General
- [x] Execute all tests via Travis CI (configure test DB, …), make sure entire build fails when test execution fails
- [ ] Consistently use 2 spaces (instead of 4)
- [ ] Convert source code TODOs into Github issues
- [x] Deploy the app so that it is public for all musicians in the world
- [ ] Input boxes are maybe too small sometimes and should be replaced by a textarea
- [ ] Admin login to approve incoming data
- [ ] Edit or delete own information
- [ ] Search functionality
- [ ] Be able to add sources (e.g. forums, videos, …)
- [ ] Generate documentation

### Tests that need to be written
- [ ] When no creative works have been created, the "New creative work" button should not be displayed twice
- [ ] The current user should only see approved information (by an admin) or his / her own published information waiting for approval
- [ ] Creative works, parts and suggestions should be correctly ordered by creation date (desc or asc)
- [ ] Validation errors should be correctly displayed / translated