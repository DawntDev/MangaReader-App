<h1>MangaReader</h1>
<p>
 <a href="https://discord.gg/mexicodev">
     <img alt="Dawnt | Discord" width="24px" align="right" raw=true HSPACE="5" src="https://discord.com/assets/847541504914fd33810e70a0ea73177e.ico"></a>
 <a href="https://open.spotify.com/playlist/6eDl0FX1pNcaFXgYIBOobX?si=aewrQ2nJTuSgkMSip3d8-Q&utm_source=copy-link">
     <img alt="Dawnt | Spotify" width="24px" align="right" raw=true HSPACE="5" src="https://open.spotifycdn.com/cdn/images/favicon.5cb2bd30.ico"></a>
 <a href="https://www.codewars.com/users/Dawnt">
    <img alt="Dawnt | CodeWars" width="24px" align="right" raw=true HSPACE="5" src="https://www.codewars.com/packs/assets/logo.f607a0fb.svg"></a>
 <a href="mailto:jmanuelhv9@gmail.com">
    <img alt="Dawnt | Email" width="24px" align="right" raw=true HSPACE="5" src="https://ssl.gstatic.com/ui/v1/icons/mail/rfr/gmail.ico"></a>
</p>

<h3>About</h3>

MangaReader is an Android app that lets you read manga and webcomics. You can access a catalog of thousands of titles, download them to read them offline and create your own library with unique content for you.
This app is made with Kotlin, with the support of [Jetpack Compose](https://developer.android.com/jetpack?hl=es-419) and some other libraries, to have a more minimalist and attractive interface for the user. In addition, in the backend, it is made with [FastAPI](https://fastapi.tiangolo.com/) and [BeautifulSoup](https://www.crummy.com/software/BeautifulSoup/) to be able to scrape the content of the pages from which the information is obtained.


<!-- TABLE OF CONTENTS -->

## Table of Contents

-   [Overview](#overview)
-   [Features](#features)
-   [Contributions](#contributions)

<!-- OVERVIEW -->

## Overview

<div align="center">
    <img src="https://raw.githubusercontent.com/DawntDev/MangaReader/master/readme-assets/splash-screen.png" height="420px" raw=false>
    <img src="https://raw.githubusercontent.com/DawntDev/MangaReader/master/readme-assets/main-screen.png" height="420px" raw=false>
    <img src="https://raw.githubusercontent.com/DawntDev/MangaReader/master/readme-assets/search-screen.png" height="420px" raw=false>
    <img src="https://raw.githubusercontent.com/DawntDev/MangaReader/master/readme-assets/chapter-screen.png" height="420px" raw=false>
</div>

The application is created with [Jetpack Compose](https://developer.android.com/jetpack) and in addition with a backend which is capable of scraping content from web pages with the help of [BeautifulSoup](https://www.crummy.com/software/BeautifulSoup/) and [Selenium](https://www.selenium.dev/) in case the content loading of the scraped page is dynamic. Finally, for the structuring of the API, [FastAPI](https://fastapi.tiangolo.com/) is used to have a server that can perform these scrapes in real-time.

-   Functioning
    -   The functionality is simple, through different requests, different data is obtained from a database. When you want to obtain the images or the number of chapters of a title, scraping is used to have the number of available chapters in real-time.
-   Use of [FastAPI](https://fastapi.tiangolo.com/)
    -   FastAPI is a library that allows you to create web applications in an easy and simple way. This library was chosen because of the great speed it allows thanks to the asynchrony within its routes. The entire API was structured using it.
-   Use of [BeautifulSoup](https://www.crummy.com/software/BeautifulSoup/)
    -   Through BeautifulSoup, we scrape the content of the pages to obtain only the necessary elements, which will later be sent to the frontend where they will be rendered.
-   Use of [Selenium](https://www.selenium.dev/)
    -   Thanks to Selenium, it was possible to obtain the content of pages whose components loaded dynamically. With a normal request, the JavaScript code on the page does not execute, so the content created by that code is not visible. With Selenium, we can make that code execute and generate the content.
-   What I learned
    -   You mainly decided to develop this application to learn more about the [Kotlin](https://kotlinlang.org/) language and its [Jetpack Compose](https://developer.android.com/jetpack) library. During development, you learned and reinforced the foundations of this language, as well as being able to adapt to the Android Studio code editor.

## Features

The main function of the application is to read webcomics, although it also has some other features that will make the user have a better experience within the application.

Functions:
-   ### Read webcomics
    -   As mentioned earlier, the main functionality is the reading of webcomics. However, web novels were not left out, so the application is capable of rendering both, allowing the reader to have a more extensive and varied catalog.

-   ### List your favorite titles
    -   In addition to reading, the application allows the user to keep a list of their favorite series, so that they stand out among the large number of titles available.
-   ### Reading tracking
    -   Another way we sought to support the reader in having better control of their titles is by keeping track of their reading. This allows the reader to view the last chapters seen of each title.
-   ### Statistics
    -   Additionally, the application keeps some statistics about the content that the reader reads within the application.

## Contributions

**If you wish to contribute to the development of the application:**

-   First clone the repository
    ```bash
    git clone https://github.com/DawntDev/MangaReader.git
    ```
-   Then create a branch with your user name
    ```bash
    git checkout -b <your-user-name>
    ```
    
    Pull requests are welcome, I would appreciate your support to contribute to a better development of this application. For major changes, please open an issue to discuss what you would like to change.


<div align="center">

[![Jetpack Compose](https://img.shields.io/badge/Made%20with%20Jetpack%20Compose-61DAFB.svg?style=for-the-badge&color=4285F4&logo=jetpackcompose&logoColor=fff)](https://developer.android.com/jetpack)

</div>
