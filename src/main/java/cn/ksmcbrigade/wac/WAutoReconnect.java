package cn.ksmcbrigade.wac;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@Mod(WAutoReconnect.MODID)
@Mod.EventBusSubscriber
public class WAutoReconnect {

    public static final String MODID = "wac";

    public static File config = new File("config/wac-config.json");
    public static boolean autoRec = true;
    public static int ticks = 100;

    public static ServerData last;

    public WAutoReconnect() throws IOException {
        MinecraftForge.EVENT_BUS.register(this);
        new File("config").mkdirs();
        if(!config.exists()){
            JsonObject object = new JsonObject();
            object.addProperty("wait_second",5);
            FileUtils.writeStringToFile(config,object.toString());
        }
        ticks = getWaitTicks();
    }

    public static int getWaitTicks() throws IOException {
        return JsonParser.parseString(FileUtils.readFileToString(config)).getAsJsonObject().get("wait_second").getAsInt()*20;
    }
}
