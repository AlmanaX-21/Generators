package me.almana.generators.utilities;

import me.almana.generators.models.Gen;
import me.almana.generators.models.User;
import org.bukkit.Location;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.codehaus.plexus.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class SimpleUtilities {


    public static User findUser(List<User> userList, String uuid) {

        for (User user: userList) {

           if (user.getUuid().equals(uuid)) return user;
        }
        return null;
    }

    public static Gen findGen(List<Gen> genList, String id) {

        for (Gen gen: genList) {

            if (gen.getId().equals(id)) return gen;
        }
        return null;
    }

    public static String serialiseLocation(Location location) throws IOException {

        String encoded = "";
        ByteArrayOutputStream io = new ByteArrayOutputStream();
        BukkitObjectOutputStream os = new BukkitObjectOutputStream(io);
        os.writeObject(location);
        os.flush();
        byte[] serializedObject = io.toByteArray();
        encoded = new String(Base64.encodeBase64(serializedObject));
        return encoded;
    }

    public static Location deserializeLoc(String serializedLocation) throws IOException, ClassNotFoundException {

        BukkitObjectInputStream is = null;
        byte[] bytes = java.util.Base64.getDecoder().decode(serializedLocation);
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        is = new BukkitObjectInputStream(in);
        return (Location) is.readObject();
    }
}
