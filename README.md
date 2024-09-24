# Azure Spring AI Movie Generator

> [!WARNING]
> Do not use this project in a production environment.
> Use this repository for demo purpose only.

Spring AI Movie Generator (codename `Rambi` a mashup of movie titles from "Rambo" and "Bambi").
Using this service, you can generate a movie plot from 2 existing movie plots and create a movie poster.

The implementation relies on Azure OpenAI Services (GPT4-o and Dalle) and The Movie Database (TMDb) APIs.

## Run

### Locally
mvn spring-boot:run 

### Remotely
The GitHub actions will build and deploy the application to Azure Container Apps (ACA) triggered by a push to the main branch.

## Config

In the Azure Open AI Studio, create 2 deployments for GPT4-o and Dalle and get the endpoint and API key.
The name of the deployments should be `gpt-4o` and `dall-e-3` respectively.

Create a `.env` file with the following entries
* AZURE_OPENAI_ENDPOINT
* AZURE_OPENAI_API_KEY
* TMDB_API_KEY 

> What would Rambo do if he encountered Bambi in the deep forest?

![AI generated movie poster for Rambi (fake movie)](rambi.png)

## Contribute

Contributions are always welcome!

Feel free to open issues & send PR.

## License

This project is licensed under the [Apache Software License version 2.0](https://www.apache.org/licenses/LICENSE-2.0).
