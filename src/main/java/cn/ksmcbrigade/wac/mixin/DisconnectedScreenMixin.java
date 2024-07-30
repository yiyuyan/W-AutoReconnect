package cn.ksmcbrigade.wac.mixin;

import cn.ksmcbrigade.wac.WAutoReconnect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(DisconnectedScreen.class)
public class DisconnectedScreenMixin extends Screen{
    @Shadow @Final private GridLayout layout;

    @Shadow @Final private Screen parent;
    @Unique
    public Button wac$button;

    @Unique
    public Button wac$recButton;

    @Unique
    public int wac$timer;

    protected DisconnectedScreenMixin(Component p_96550_) {
        super(p_96550_);
    }

    @Inject(method = "init",at = @At("TAIL"))
    public void init(CallbackInfo ci){

        this.wac$button = this.layout.addChild(Button.builder(Component.translatable("gui.wac.rec"),b -> wac$rec()).build(),3,0,1,1,this.layout.newCellSettings().padding(2).padding(-6));
        this.wac$recButton = this.layout.addChild(Button.builder(Component.translatable("gui.wac.auto_rec"),b -> wac$press()).build(),4,0,1,1,this.layout.newCellSettings().padding(2));

        this.layout.arrangeElements();

        addRenderableWidget(this.wac$button);
        addRenderableWidget(this.wac$recButton);

        if(WAutoReconnect.autoRec){
            try {
                wac$timer = WAutoReconnect.getWaitTicks();
            } catch (IOException e) {
                e.printStackTrace();
                wac$timer = 100;
            }
        }
    }

    @Unique
    public void wac$press(){

        WAutoReconnect.autoRec = !WAutoReconnect.autoRec;

        if(WAutoReconnect.autoRec){
            try {
                wac$timer = WAutoReconnect.getWaitTicks();
            } catch (IOException e) {
                e.printStackTrace();
                wac$timer = 100;
            }
        }
    }

    @Unique
    public void wac$rec(){
        if(WAutoReconnect.last==null) return;
        ConnectScreen.startConnecting(this.parent, Minecraft.getInstance(), ServerAddress.parseString(WAutoReconnect.last.ip),WAutoReconnect.last,false);
    }

    @Unique
    public void tick(){
        if(wac$recButton==null) return;
        if(!WAutoReconnect.autoRec){
            wac$recButton.setMessage(Component.translatable("gui.wac.auto_rec"));
            return;
        }

        wac$recButton.setMessage(Component.translatable("gui.wac.auto_rec").append(Component.literal(" ("+(int)Math.ceil(wac$timer/20.0D)+")")));

        if(wac$timer>0){
            wac$timer--;
            return;
        }

        wac$rec();
    }
}
