package old;

import old.LocalServer;
import old.Permissions;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class Logger {

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

            if(server.getChild("teams") != null){
                Element teams = server.getChild("teams");
                for(Element team : teams.getChildren()){
                    ls.addTeam((team.getText()));
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

            if(server.getChild("compact") != null){
                ls.setCompact(Boolean.parseBoolean(server.getChild("compact").getText()));
            }

            serverMap.add(ls);
        }
        return serverMap;
    }
}
