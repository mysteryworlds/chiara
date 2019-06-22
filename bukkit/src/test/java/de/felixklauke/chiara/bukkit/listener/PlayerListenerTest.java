package de.felixklauke.chiara.bukkit.listener;

import de.felixklauke.chiara.bukkit.service.PermissionService;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.InetAddress;

@ExtendWith(MockitoExtension.class)
class PlayerListenerTest {

    @Mock
    private PermissionService permissionService;

    @Mock
    private Player player;

    private PlayerListener playerListener;

    @BeforeEach
    void setUp() {
        playerListener = new PlayerListener(permissionService);
    }

    @Test
    void testOnPlayerLogin() {

        PlayerLoginEvent playerLoginEvent = new PlayerLoginEvent(player, "", InetAddress.getLoopbackAddress());

        playerListener.onPlayerLogin(playerLoginEvent);

        Mockito.verify(permissionService).registerPlayer(Mockito.eq(player));
    }
}
