# BookShelf created by Philip Lapinski
## Assignment 7 CIS 3515

### What it's about:
Book shelf is an app that demonstrates the usage of fragments to form a more complex and desirable UI. Fragments provide a plethera
of functionality. In this case, our bookshelf app will be responsive to a user's orientation and screen size. This app will display a 
list of books with the cooresponding author. When selected, the book's details should populate the screen.  
Fragments should be used to display the information. In additon, when the user is in landscape mode or on a larger screen size,
**both the book list and selected book detail should occupy the screen**.
To spice things up a bit more, each book will have an audio file with a narration of the book. Our fragment should contain features for
the user to be able to start, stop, pause, and an interactable seekBar. The audio should be wrapped in a service to provide the user with 
an uninterrupted experience.

### Requirements:
1) Book Class
2) Book List Class
3) Layouts
    - Fragments
        - BookDetails
        - BookList
        - Control
    - Activities
        - BookSearch
        - MainActivity
4) Fragments and their core functionality
5) Service for audio


### Examples:

![image](https://user-images.githubusercontent.com/41872747/114905047-838c9700-9de6-11eb-970b-d8cce054a489.png)

### 2 In 1:

![image](https://user-images.githubusercontent.com/41872747/114905149-9ef7a200-9de6-11eb-8591-5e31737f9516.png)

### Rubric:

| Requirements | Points |
|--------------|--------|
| Integrated AudioBookService | 10% |
| Properly integrate controls (Play, Stop, Pause) in ControlFragment with Now Playing label | 20% |
| SeekBar progress is associated with book duration | 10% |
| SeekBar updates as book plays to show current progress | 10 % |
| SeekBar controls book progress when user moves progress bar | 20% |
| Book continues playing when activity is restarted | 20% |
| Controls continue to function and Now Playing shows correct book when activity is restarted | 10 % |
