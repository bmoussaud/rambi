# Project Rambi

> [!WARNING]
> Do not use this project in a production environment.
> Use this repository for demo purpose only.

Rambi is a mashup of movie titles from "Rambo" and "Bambi".
Using Rambi, you can generate movie plots from user prompts,
merging storylines from a movie database.

> What would Rambo do if he encountered Bambi in the deep forest?

![AI generated movie poster for Rambi (fake movie)](rambi.png)

The implementation relies on GenAI tools that you can run on your workstation.

## Contribute

Contributions are always welcome!

Feel free to open issues & send PR.

## License

Copyright &copy; 2024 [Broadcom, Inc. or its affiliates](https://broadcom.com).

This project is licensed under the [Apache Software License version 2.0](https://www.apache.org/licenses/LICENSE-2.0).

```
curl -X POST http://localhost:8080/generate/image -H 'Content-Type: application/json' -d '{"generatedMovie":{"title":"Rambi","plot":"The story of a young deer growing up in the forest. The pet defends the forest with an yellow umbrella and wears the red headband"}}'
```


mvn spring-boot:run -Dspring-boot.run.profiles=fake

# Config
Create a `.env` file with the following entries
* AZURE_OPENAI_ENDPOINT
* AZURE_OPENAI_API_KEY
* TMDB_API_KEY 

## Dataset

https://www.kaggle.com/datasets/akshaypawar7/millions-of-movies
URL https://image.tmdb.org/t/p/w500/wV9e2y4myJ4KMFsyFfWYcUOawyK.jpg
https://developer.themoviedb.org/docs/image-basics