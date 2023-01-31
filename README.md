# Essential Slick Exercises

Exercises and solutions to accompany [Essential Slick][essential-slick].

Copyright 2016 [Underscore Consulting LLP][underscore].
Exercise code licensed [Apache 2.0][license].

## Quick Start

Follow the instructions below to get set up.
You will need a recent version of Docker Compose.

1. Clone this repo and switch to the root directory:

   ```bash
   git clone https://github.com/underscoreio/essential-slick-code.git

   cd essential-slick
   ```

2. Run Docker Compose:

   ```bash
   docker compose up
   ```

   This will start two containers:
   
    - `app` - containing Scala, SBT, a JVM, and a MySQL client. 
      The container's working directory mapped to the current directory
      so you can shell into it and compile and run the application.
      
    - `database` - running an instance of MySQL.
      The container's `/var/lib/mysql` is mapped to `./mysql-data`
      to persist the database between runs of `docker compose up`.
      
3. In a separate terminal, shell into the `app` container:

   ```bash
   docker compose exec app bash
   ```

3. Compile and run the example "helloworld.Main" application.
   This will take a few minutes to run the first time.
   You'll need an internet connection to download dependencies:

   ```bash
   sbt> runMain helloworld.Main
   ```

4. If you see a list of albums similar to the following, all is well!

   ```
   Album(Keyboard Cat,Keyboard Cat's Greatest Hits,1)
   Album(Spice Girls,Spice,2)
   Album(Rick Astley,Whenever You Need Somebody,3)
   Album(Manowar,The Triumph of Steel,4)
   Album(Justin Bieber,Believe,5)
   ```

   If not, drop me an email and I'll try to help.

[essential-slick]: http://underscore.io/books/essential-slick
[underscore]: http://underscore.io
[license]: http://www.apache.org/licenses/LICENSE-2.0
[gitter]: https://gitter.im/underscoreio/scala
