Two movie titles and plots will be provided, along with a target genre.
Using the titles, plots and genre as inspiration, generate the following:
* Step 1: Generate a new movie title that combines elements of the provided titles and fits the target genre. The title should be catchy and humorous.
* Step 2: Generate a 4-6 sentence movie plot synopsis for the new title, incorporating themes, characters, or plot points from the provided movies. Adapt them to fit the target genre.
* Step 3: Based on the generated movie plot, describe the movie poster.

Use the details below to generate the new movie title and plot:
Take care of not generating any violence.

##### Movie 1
* Title: Barbie
* Plot: Barbie and Ken are having the time of their lives in the colorful and seemingly perfect world of Barbie Land. However, when they get a chance to go to the real world, they soon discover the joys and perils of living among humans.

##### Movie 2
* Title: Back to the Future
* Plot: Eighties teenager Marty McFly is accidentally sent back in time to 1955, inadvertently disrupting his parents' first meeting and attracting his mother's romantic interest. Marty must repair the damage to history by rekindling his parents' romance and - with the help of his eccentric inventor friend Doc Brown - return to 1985.

##### Additional Information

* Target Genre: Comedy

Use the description of the  2 previous image to generate the posterDescription.

Your response should be in JSON format.
Do not include any explanations, only provide a RFC8259 compliant JSON response following this format without deviation.
Do not include markdown code blocks in your response.
Here is the JSON Schema instance your output must adhere to:
```{
  "$schema" : "https://json-schema.org/draft/2020-12/schema",
  "type" : "object",
  "properties" : {
    "genre" : {
      "type" : "string"
    },
    "plot" : {
      "type" : "string"
    },
    "posterDescription" : {
      "type" : "string"
    },
    "posterUrl" : {
      "type" : "string"
    },
    "title" : {
      "type" : "string"
    }
  }
}