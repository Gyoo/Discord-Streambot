# Discord-Streambot
A bot for Discord that alerts a channel when Twitch streams go live

Created by [Gyoo](http://twitter.com/GyooRunsStuff)

Join the [Discord server](https://discord.gg/0jXAp5dkKPUoM7ZW) to invite Streambot to your server !

# How to use (Commands, process)

`!streambot <command>`

`commands` : List of available commands

`streams` : List of online streams (will send you a PM)

`add <option> <content>` : Add data to the bot (see options below)

`remove <option> <content>` : Remove data from the bot

`list <option>` : List data from the bot

`notify <me|everyone|here>` : Adds a mention at the beginning of the announce. me will add a mention to you, everyone will add the "everyone" mention, here will add the "here" mention. (Type command again to remove
existing notification)

`enable` : Activates bot after configuration

`disable` : Disactivates bot

`invite` : Gives an invite link so people can get the bot on their own server !

`compact on|off` : Enables/Disables compact mode

`cleanup <none|edit|delete>` : When a stream goes offline, choose to either leave the message (`none`), `edit` the "NOW LIVE" to "OFFLINE" or `delete` the announce

`move <channel>` : Moves the bot announces to another channel (Must use the #channel identifier !)

`permissions <use|queue|forbid> <command> <role|everyone>` : Gives permission level for a command to the users of a role (supports `add` and `remove` commands only currently)

`queue` : shows queued commands by users with the QUEUE permission set. **Warning** : Using this command cleans the queue !

`donate` : If you like my work, please consider making a donation :)

_Options_ :

`game` : Game name based on Twitch's list (must be the exact name to work !)

`channel` : Twitch channel name

`tag` : Word or group of words that must be present in the stream's title

~~`team` : Twitch team name (ID as mentioned in the team link : twitch.tv/team/<Team ID>)~~

`manager` : Discord user (must use the @ alias when using this option !)

**Recommended process**

Invite the bot from anywhere using this link : https://discordapp.com/oauth2/authorize?&client_id=170832003715956746&scope=bot&permissions=224256

**WARNING** : You must be the owner of a server to invite the bot to said server

*Your Server*

`!streambot add channel <Twitch channel>` if you want to add channels to Streambot. No link, only channel name (Example : `!streambot add channel gyoo_`)

`!streambot add game <Twitch game>` if you want Streambot to announce every stream with the given game(s). Game name must be the same as on Twitch, copy/pasting is recommended. (Example : `!streambot add game The Legend of Zelda: Twilight Princess`)

`!streambot add tag <Tag>` if you want to filter streams that only contain some tags in the title. (Example : `!streambot add tag speedrun`)

Once configured, `!streambot enable`

If you set both channels, games and tags, then streams will appear if __channel__ is playing __game__ with __tag__ in his title. Keep that in mind when configuring Streambot !

From now on, multiple contents can be added in a single command for the `game`, `channel`and  `tag` options, using the "|" symbol between each content.

Example : `!streambot add channel gyoo_ | gamesdonequick | europeanspeedsterassembly`

**Details for permissions**

- Use : permission to use the command directly
- Queue : commands will be queued to a list that is accessible to managers only. Type `!streambot queue` to access the queue.
- Forbid : Self explaining... I guess

Commands supported are currently `add` and `remove` only. I can add some more with your feedback about this feature.

Permissions are set for a given role. Please type the exact Role name, or `everyone` to give the permission level to... everyone, no matter the role.

# Development, help

Please check the issues to see both bugs to fix and features to add !

If you have any questions (as the code is mostly not documented, I know it's bad), please hit me on the Streambot Discord server, link is above

## Credentials

To test the bot on your side, ask me directly for the process. Things have changed since Discord created the BOT accounts.
