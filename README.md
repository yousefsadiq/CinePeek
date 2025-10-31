CinePeek 

Features

Sleek, Modern UI: A dark-themed, responsive interface built with JavaFX and CSS, based on Figma mockups.

Movie Search: Instantly search the entire TMDB database for any movie.

Top 6 Results: View the top 6 most relevant movies with their official posters in a clean, scrollable layout.

Fun Fact Deck: Click a movie to view an interactive "deck" of fun fact cards.

Dynamic Data: Get real-time data for:

Worldwide Revenue

Production Budget

Movie Runtime

Release Date 

How It Works

Search: Start the app and type a movie title (e.g., "Inception") into the search bar.

Select: The app displays the top 6 results. Click the poster of the movie you're curious about.

Peek: A deck of cards appears! Click the top card to "draw" it and send it flying off-screen, revealing the next fact underneath.

Tech Stack

Language: Java 

Framework: JavaFX 

Build: Apache Maven

API: The Movie Database (TMDB) API

Libraries:

Google Gson - For parsing JSON responses from the API.

java-dotenv - For secure management of the API key.

Design: Built with an MVC (Model-View-Controller) pattern using FXML and a central SceneManager for navigation.

Getting Started

To get a local copy up and running, follow these simple steps.

Prerequisites

Java (JDK 17 or newer)

Apache Maven

A free API Key from The Movie Database (TMDB)

Installation

Clone the repo:

git clone [https://github.com/your-username/CinePeek.git](https://github.com/yousefsadiq/CinePeek.git)
cd CinePeek

Create your API Key file:

In the root directory of the project (where pom.xml is), create a file named .env

Inside this file, add your API Read Access Token (v3 Auth). It will look something like this:

API_KEY=eyJhbGciOiJIUzI1NiJ9.eyJhd...

Note: Make sure this is the "API Read Access Token" (Bearer Token) and not the shorter "API Key".

Run the main method in the Main class

Acknowledgments

This application uses the TMDB API but is not endorsed or certified by TMDB.

All movie data and images are courtesy of TMDB.
