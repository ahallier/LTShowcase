# LTShowcase

<b>Description:</b>
Command-line based tool that fetches photo album data from provided endpoint and formats and displays results to user.

<b>Example output:</b>
```
 > photo-album 12
[15] ut maxime reiciendis veritatis
[16] eos accusamus illum sunt consequatur qui
> photo-album 16
[45] ex laborum laudantium et omnis earum eum
[67] voluptas cumque velit quos repudiandae ab numquam"
```

<b>Dependencies:</b>
  -Java8
  -HttpClient
  -JUnit 

<b>Install and run:</b>
  Check out project
  Navigate to LTShowcase/out/artifacts/LTShowcase_jar from the command line
  - Get all albums and photos
     ```
     java -jar LTShowcase.jar
     ```
  - Get specific album and photos
     ``` 
     java -jar LTShowcase.jar <album number>
     ```
  - Get a list of albums and photos, give parameters separated by a space 
     ```
     java -jar LTShowcase.jar <album_one> <album_two> ...
     ```
  - Run interactively, to end type 'exit' and enter
     ```
     java -jar LTShowcase.jar interactive
     ```
