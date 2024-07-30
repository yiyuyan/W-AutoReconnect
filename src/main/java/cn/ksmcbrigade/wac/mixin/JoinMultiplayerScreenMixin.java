package cn.ksmcbrigade.wac.mixin;

import cn.ksmcbrigade.wac.WAutoReconnect;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JoinMultiplayerScreen.class)
public class JoinMultiplayerScreenMixin {
    @Inject(method = "join",at = @At("HEAD"))
    public void join(ServerData p_99703_, CallbackInfo ci){
        WAutoReconnect.last = p_99703_;
    }
}
