# How to deploy Streambot to your local machine

For devs and testing purposes

## Create a bot account on Discord

- Go to https://discordapp.com/developers/applications/me and create a new application.
- Call it whatever you want. The name is the only thing you need, the rest is optional.
- On the next page, click on "Create a Bot User"
- You're set for this part, but keep that page open sinec we'll need it later

## Create a database

The database I'm using is good ol' MySQL. You can find good tutorials on how to install a database server to your computer if you've never done that before.
Once your server is running, import the `streambot.sql` file to your database and you should be set for this part.

## Setup the config file

There is a file that is not shared on the repository since it contains all the sensible data like my Bot token, database accesses and such. You can reproduce it by filling the following :

```
# If set to true, the bot will behave normally but won't post any messages.
mode.debug = true

# Your bot token, available at https://discordapp.com/developers/applications/me
bot.token=

# Database config

# Connection URL
hibernate.connection.url=jdbc:mysql://<database address>
# Change this only if you use something else than MySQL. Use at your own risk.
hibernate.connection.driver_class=com.mysql.cj.jdbc.Driver
# Credentials
hibernate.connection.username=
hibernate.connection.password=
# Set this to true and you're in for a console spam, as you wish
hibernate.show_sql=false

# This is optional, only if you want to run the tests (most of them shouldn't pass since I didn't keep them up to date)
hibernate.connection.url.test=jdbc:mysql://<test database address>
```

Name this file config.properties and put it at the root of the project folder.

# Now you should be good

Unless I forgot something... welp.
Try running the bot once everything else is up and running (especially the database server). If you get this message : `[Info] [JDA]: Finished Loading!` Then the bot has successfully started. 
You can then invite it to a test server with the following link : https://discordapp.com/oauth2/authorize?&client_id=<YOUR CLIENT ID>&scope=bot&permissions=224256 (Check your app's page to get the Client ID)
And then you can start testing stuff or whatever !