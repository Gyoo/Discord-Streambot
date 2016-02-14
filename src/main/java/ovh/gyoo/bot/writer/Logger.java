package ovh.gyoo.bot.writer;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import ovh.gyoo.bot.data.DiscordInstance;
import ovh.gyoo.bot.data.LocalServer;
import ovh.gyoo.bot.data.Permissions;
import ovh.gyoo.bot.data.QueueItem;

import java.io.*;
import java.util.*;

public class Logger {

    public static void saveData(Map<String, LocalServer> servers, String filename){
        Element root = new Element("servers");
        Document doc = new Document(root);
        for(Map.Entry<String, LocalServer> entry : servers.entrySet()){
            Element server = new Element("server");
            server.setAttribute(new Attribute("id", entry.getKey()));

            root.addContent(server);

            Element active = new Element("active");
            active.setText(Boolean.toString(entry.getValue().isActive()));
            server.addContent(active);

            Element serverId = new Element("serverID");
            serverId.setText(entry.getValue().getServerID());
            server.addContent(serverId);

            Element channelId = new Element("channelID");
            channelId.setText(entry.getValue().getId());
            server.addContent(channelId);

            if(entry.getValue().getGameList().size() > 0){
                Element games = new Element("games");
                for(String s : entry.getValue().getGameList()){
                    Element game = new Element("game");
                    game.setText(s);
                    games.addContent(game);
                }
                server.addContent(games);
            }

            if(entry.getValue().getUserList().size() > 0){
                Element channels = new Element("channels");
                for(String s : entry.getValue().getUserList()){
                    Element channel = new Element("channel");
                    channel.setText(s);
                    channels.addContent(channel);
                }
                server.addContent(channels);
            }

            if(entry.getValue().getTagList().size() > 0){
                Element tags = new Element("tags");
                for(String s : entry.getValue().getTagList()){
                    Element tag = new Element("tag");
                    tag.setText(s);
                    tags.addContent(tag);
                }
                server.addContent(tags);
            }

            if(entry.getValue().getManagers().size() > 0) {
                Element managers = new Element("managers");
                for (String s : entry.getValue().getManagers()) {
                    Element manager = new Element("manager");
                    manager.setText(s);
                    managers.addContent(manager);
                }
                server.addContent(managers);
            }

            Element permissions = new Element("permissions");
            for(Map.Entry<String, Permissions> permissionsEntry : entry.getValue().getPermissionsMap().entrySet()){
                Element permission = new Element("permission");
                permission.setAttribute("name", permissionsEntry.getKey());
                for(Map.Entry<String, Integer> permissionEntry : permissionsEntry.getValue().getPerms().entrySet()){
                    Element role = new Element("role");
                    role.setAttribute("name", permissionEntry.getKey());
                    role.setText(Integer.toString(permissionEntry.getValue()));
                    permission.addContent(role);
                }
                permissions.addContent(permission);
            }
            server.addContent(permissions);

            Element toggles = new Element("toggles");
            for(Map.Entry<String, Boolean> toggleEntry : entry.getValue().getToggles().entrySet()){
                Element toggle = new Element("toggle");
                toggle.setAttribute("name", toggleEntry.getKey());
                toggle.setText(toggleEntry.getValue().toString());
                toggles.addContent(toggle);
            }
            server.addContent(toggles);

            if(entry.getValue().getCommandsQueue().size() > 0) {
                Element commandsQueue = new Element("commandsQueue");
                for (QueueItem i : entry.getValue().getCommandsQueue()) {
                    Element queue = new Element("queue");
                    queue.setText(i.getCommand());
                    queue.setAttribute("author", i.getAuthor());
                    commandsQueue.addContent(queue);
                }
                server.addContent(commandsQueue);
            }
        }

        XMLOutputter output = new XMLOutputter(Format.getPrettyFormat());
        try {
            output.output(doc, new FileOutputStream(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<LocalServer> loadData(String filename){
        SAXBuilder sxb = new SAXBuilder();
        List<LocalServer> serverMap = new ArrayList<>();
        Document document = null;
        try {
            document = sxb.build(new File(filename));
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
        assert document != null;
        Element root = document.getRootElement();
        for(Element server : root.getChildren()){
            String channelID = server.getChild("channelID").getText();
            String serverID = server.getChild("serverID").getText();
            LocalServer ls = new LocalServer(channelID, serverID);
            if(null == DiscordInstance.getInstance().getDiscord().getGuildById(ls.getServerID())) continue;
            if(Boolean.parseBoolean(server.getChild("active").getText())) ls.activate();

            if(server.getChild("games") != null){
                Element games = server.getChild("games");
                for (Element game : games.getChildren()){
                    ls.addGame(game.getText());
                }
            }

            if(server.getChild("channels") != null){
                Element channels = server.getChild("channels");
                for (Element channel : channels.getChildren()){
                    ls.addUser(channel.getText());
                }
            }

            if(server.getChild("tags") != null){
                Element tags = server.getChild("tags");
                for (Element tag : tags.getChildren()){
                    ls.addTag(tag.getText());
                }
            }

            if(server.getChild("managers") != null){
                Element managers = server.getChild("managers");
                for (Element manager : managers.getChildren()){
                    ls.addManager(manager.getText());
                }
            }

            if(server.getChild("permissions") != null){
                Element permissions = server.getChild("permissions");
                for(Element permission : permissions.getChildren()){
                    Permissions p = new Permissions();
                    for(Element role : permission.getChildren()){
                        p.addPermission(role.getAttributeValue("name"), Integer.parseInt(role.getText()));
                    }
                    ls.addPermission(permission.getAttributeValue("name"), p);
                }
            }

            if(server.getChild("toggles") != null){
                Element toggles = server.getChild("toggless");
                for(Element toggle : toggles.getChildren()){
                    ls.addToggle(toggle.getAttributeValue("name"), Boolean.parseBoolean(toggle.getText()));
                }
            }

            if(server.getChild("commandsQueue") != null){
                Element commandsQueue = server.getChild("commandsQueue");
                for (Element command : commandsQueue.getChildren()){
                    ls.queueCommand(command.getAttributeValue("author"), command.getText());
                }
            }

            serverMap.add(ls);
        }
        return serverMap;
    }

    public static void writeToLog(String s){
        BufferedWriter out = null;
        try
        {
            FileWriter fstream = new FileWriter("log.txt", true); //true tells to append data.
            out = new BufferedWriter(fstream);
            out.write(s);
            out.newLine();
        }
        catch (IOException e)
        {
            System.err.println("Error: " + e.getMessage());
        }
        finally
        {
            if(out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void writeToErr(String s){
        BufferedWriter out = null;
        try
        {
            FileWriter fstream = new FileWriter("err.txt", true); //true tells to append data.
            out = new BufferedWriter(fstream);
            out.write(s);
            out.newLine();
        }
        catch (IOException e)
        {
            System.err.println("Error: " + e.getMessage());
        }
        finally
        {
            if(out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
