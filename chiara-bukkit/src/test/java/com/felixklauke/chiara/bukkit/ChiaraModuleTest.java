package com.felixklauke.chiara.bukkit;

import static org.mockito.Mockito.when;

import com.google.inject.Guice;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicesManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
final class ChiaraModuleTest {
  @Mock
  private Plugin plugin;
  @Mock
  private Server server;
  @Mock
  private ServicesManager servicesManager;
  private ChiaraModule chiaraModule;

  @BeforeEach
  void setUp() {
    when(plugin.getServer()).thenReturn(server);
    when(server.getServicesManager()).thenReturn(servicesManager);
    chiaraModule = ChiaraModule.withPlugin(plugin);
  }

  @Test
  void testConfiguration() {
    var injector = Guice.createInjector(chiaraModule);
  }
}