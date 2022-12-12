# CLSW assignment - Sasha Pourcelot, Julien Whitfield

## Project description

The user install an application on their smart watch. It displays a 6-letter
code on the screen. The user then enters the said code on a web page. The watch
tracks the user's movements thanks to the accelerometer and sends the data to
a server. The website shows up the time since the last movement of the user and
sends a notification if the user has not moved for more than 1 minute.

## Project structure

The project is divided into 3 parts:
- The watch application (in the `watchclient` directory),
- The backend (in the `backend` directory),
- The webclient (in the `webclient` directory).

## Running the project

The watchclient can be installed and run through the Android Studio IDE.

The backend can be run by installing the `rust` stable toolchain and hitting
`cargo run` in the `backend` directory.

The webclient can be run by opening `index.html` of the `webclient` directory in a browser.


Note that the backend has been already deployed at
`http://domino.zdimension.fr:3030/`.