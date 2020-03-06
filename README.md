# Interactive Fiction Engine
This application is a command line interactive fiction adventure game in which the protagonist wakes up in a maze and aims to escape from it. The maze is divided into several rooms with boxes, objects and doors that the protagonist has to open and use to escape.

The application defines the maze and the adventure itself by processing XML files that are easily configurable to create routes, rooms, doors and even new objects or traps. Two XML files (`example_EN.xml` and `example_ES.xml`) are included with an easy to solve maze. To create a new adventure, just copy one of the files and edit it taking into account its internal structure.

> **This application is part of the exercises I did working as private tutor for students of engineering in Spain. Because of that, some of the information is in Spanish and the entire application was developed strictly following the requirements established by the student's professor.**
>
> **This code was not made by one of my students, but by me, since it was an assignment that a student ordered me to develop entirely. Also, this code is publicly distributed here with the permission of the student I developed this application for.**

## Setup and Run
1. Install Java SE Development Kit 13 (it should work with other versions)
2. Download the source code
3. The code is already compiled, so change the current working directory to `./bin` and execute the following command: `java Main`
4. Run the game by selecting a XML file in `./adventures` using the file chooser

* To build again the code, change the current working directory to `./src` and execute the following command: `javac Main.java`

## Directory Structure
```
|- /.github
    |- ...
|- /adventures
    |- example_EN.xml
    |- example_ES.xml
    |- example.dtd
|- /bin
    |- ...
|- /src
    |- ...
|- _config.yml
|- .gitignore
|- Development Requirements.pdf
|- LICENSE
|- README.md
```
