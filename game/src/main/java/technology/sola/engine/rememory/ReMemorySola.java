package technology.sola.engine.rememory;

import technology.sola.engine.assets.BulkAssetLoader;
import technology.sola.engine.assets.audio.AudioClip;
import technology.sola.engine.assets.graphics.SpriteSheet;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.properties.GuiPropertyDefaults;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.physics.system.GravitySystem;
import technology.sola.engine.physics.system.ImpulseCollisionResolutionSystem;
import technology.sola.engine.physics.system.ParticleSystem;
import technology.sola.engine.rememory.events.ForgetEverythingEvent;
import technology.sola.engine.rememory.gui.GuiManager;
import technology.sola.engine.rememory.render.GrainyGraphicsModule;
import technology.sola.engine.rememory.render.LoadingScreen;
import technology.sola.engine.rememory.systems.EnemySystem;
import technology.sola.engine.rememory.systems.RoomSystem;
import technology.sola.engine.rememory.systems.PlayerSystem;
import technology.sola.engine.rememory.systems.PortalSystem;

public class ReMemorySola extends SolaWithDefaults {
  private boolean isLoading = true;
  private LoadingScreen loadingScreen = new LoadingScreen();

  public ReMemorySola() {
    super(SolaConfiguration.build("re;memory", 256, 240).withTargetUpdatesPerSecond(30));
  }

  @Override
  protected void onInit(DefaultsConfigurator defaultsConfigurator) {
    PlayerAttributeContainer playerAttributeContainer = new PlayerAttributeContainer(eventHub);

    defaultsConfigurator.useGui(new GuiPropertyDefaults("monospaced_NORMAL_10", Color.BLACK, Color.BLANK))
      .usePhysics()
      .useGraphics()
      .useLighting()
      .useBackgroundColor(Color.WHITE);

    // default physics overrides
    solaEcs.getSystem(GravitySystem.class).setActive(false);
    solaEcs.addSystem(
      new ImpulseCollisionResolutionSystem(eventHub, 15, 0.001f, 0.7f)
    );

    // rendering stuff
    platform.getViewport().setAspectMode(AspectMode.MAINTAIN);
    platform.getRenderer().createLayers(
      Constants.Layers.DECORATION,
      Constants.Layers.OBJECTS
    );
    solaGraphics.addGraphicsModules(new GrainyGraphicsModule(playerAttributeContainer));

    // gui
    new GuiManager(solaGuiDocument, eventHub, playerAttributeContainer, solaEcs);

    // ecs
    solaEcs.addSystems(
      new PlayerSystem(keyboardInput, eventHub, playerAttributeContainer, assetLoaderProvider.get(AudioClip.class)),
      new EnemySystem(eventHub, playerAttributeContainer),
      new ParticleSystem(),
      new RoomSystem(eventHub, platform.getRenderer(), solaEcs, playerAttributeContainer),
      new PortalSystem(eventHub)
    );

    // Start loading assets while loading displays
    loadingScreen = new LoadingScreen();
    new BulkAssetLoader(assetLoaderProvider)
      .addAsset(AudioClip.class, "memories", "assets/audio/Memories-9.wav")
      .addAsset(AudioClip.class, Constants.Assets.AudioClips.QUACK, "assets/audio/Quack.wav")
      .addAsset(Font.class, "monospaced_NORMAL_10", "assets/font/monospaced_NORMAL_10.json")
      .addAsset(SpriteSheet.class, Constants.Assets.Sprites.ID, "assets/sprites/rememory_spritesheet.json")
      .addAsset(SpriteSheet.class, Constants.Assets.CozySprites.ID, "assets/sprites/cozy_room.json")
      .addAsset(SpriteSheet.class, Constants.Assets.AcidRainSprites.ID, "assets/sprites/acid_rain_sprites.json")
      .loadAll()
      .onComplete(assets -> {
        AudioClip audioClip = ((AudioClip) assets[0]);

        audioClip.setVolume(0.9f);
        audioClip.loop(-1);

        AudioClip audioClipQuack = ((AudioClip) assets[1]);
        audioClipQuack.addFinishListener(AudioClip::stop);

        // Start the game
        eventHub.emit(new ForgetEverythingEvent());
        isLoading = false;
        loadingScreen = null;
      });
  }

  @Override
  protected void onRender(Renderer renderer) {
    if (isLoading) {
      loadingScreen.drawLoading(renderer);
    } else {
      super.onRender(renderer);
    }
  }
}
