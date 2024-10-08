package technology.sola.engine.rememory;

import technology.sola.engine.assets.BulkAssetLoader;
import technology.sola.engine.assets.audio.AudioClip;
import technology.sola.engine.assets.graphics.SpriteSheet;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.assets.graphics.gui.GuiJsonDocument;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.defaults.SolaWithDefaults;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;
import technology.sola.engine.graphics.gui.elements.TextStyles;
import technology.sola.engine.graphics.gui.style.ConditionalStyle;
import technology.sola.engine.graphics.gui.style.theme.DefaultThemeBuilder;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.engine.physics.system.GravitySystem;
import technology.sola.engine.physics.system.ImpulseCollisionResolutionSystem;
import technology.sola.engine.physics.system.ParticleSystem;
import technology.sola.engine.rememory.events.ForgetPagesEvent;
import technology.sola.engine.rememory.gui.GuiManager;
import technology.sola.engine.rememory.render.GrainyGraphicsModule;
import technology.sola.engine.rememory.render.LoadingScreen;
import technology.sola.engine.rememory.systems.EnemySystem;
import technology.sola.engine.rememory.systems.RoomSystem;
import technology.sola.engine.rememory.systems.PlayerSystem;
import technology.sola.engine.rememory.systems.PortalSystem;

import java.util.List;

public class ReMemorySola extends SolaWithDefaults {
  private boolean isLoading = true;
  private LoadingScreen loadingScreen = new LoadingScreen();

  public ReMemorySola() {
    super(new SolaConfiguration("re;memory", 256, 240, 30));
  }

  @Override
  protected void onInit(DefaultsConfigurator defaultsConfigurator) {
    PlayerAttributeContainer playerAttributeContainer = new PlayerAttributeContainer(eventHub);

    defaultsConfigurator
      .useGui(
        DefaultThemeBuilder.buildLightTheme()
          .addStyle(TextGuiElement.class, List.of(ConditionalStyle.always(
            TextStyles.create().setFontAssetId(Constants.Assets.Font.MONO_10).build()
          )))
      )
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

    // ecs
    solaEcs.addSystems(
      new PlayerSystem(keyboardInput, eventHub, playerAttributeContainer, assetLoaderProvider.get(AudioClip.class)),
      new EnemySystem(eventHub, playerAttributeContainer, assetLoaderProvider.get(AudioClip.class), platform.getRenderer()),
      new ParticleSystem(),
      new RoomSystem(eventHub, platform.getRenderer(), solaEcs, playerAttributeContainer),
      new PortalSystem(eventHub)
    );

    // gui
    GuiManager guiManager = new GuiManager(solaEcs, eventHub, playerAttributeContainer);

    // Start loading assets while loading displays
    loadingScreen = new LoadingScreen();
    new BulkAssetLoader(assetLoaderProvider)
      .addAsset(AudioClip.class, "memories", "assets/audio/Memories-9.wav")
      .addAsset(AudioClip.class, Constants.Assets.AudioClips.QUACK, "assets/audio/Quack.wav")
      .addAsset(AudioClip.class, Constants.Assets.AudioClips.BELL, "assets/audio/Bell-2.wav")
      .addAsset(AudioClip.class, Constants.Assets.AudioClips.FORGET, "assets/audio/Forget.wav")
      .addAsset(Font.class, Constants.Assets.Font.MONO_10, "assets/font/monospaced_NORMAL_10.json")
      .addAsset(SpriteSheet.class, Constants.Assets.Sprites.ID, "assets/sprites/rememory_spritesheet.json")
      .addAsset(SpriteSheet.class, Constants.Assets.CozySprites.ID, "assets/sprites/cozy_room.json")
      .addAsset(SpriteSheet.class, Constants.Assets.BasementSprites.ID, "assets/sprites/basement.json")
      .addAsset(SpriteSheet.class, Constants.Assets.AcidRainSprites.ID, "assets/sprites/acid_rain_sprites.json")
      .addAsset(SpriteSheet.class, Constants.Assets.LibrarySprites.ID, "assets/sprites/Library.json")
      .addAsset(GuiJsonDocument.class, Constants.Assets.Gui.CONTROLS, "assets/gui/controls.json")
      .addAsset(GuiJsonDocument.class, Constants.Assets.Gui.IN_GAME, "assets/gui/in_game.json")
      .addAsset(GuiJsonDocument.class, Constants.Assets.Gui.DIARY, "assets/gui/diary.json")
      .loadAll()
      .onComplete(assets -> {
        // configure audio clips
        ((AudioClip) assets[1]).addFinishListener(AudioClip::stop);
        ((AudioClip) assets[2]).addFinishListener(AudioClip::stop);
        ((AudioClip) assets[3]).addFinishListener(AudioClip::stop);
        AudioClip backgroundMusicClip = ((AudioClip) assets[0]);

        backgroundMusicClip.setVolume(0.9f);
        backgroundMusicClip.loop(AudioClip.CONTINUOUS_LOOPING);

        guiManager.initialize(guiDocument, (GuiJsonDocument) assets[10], (GuiJsonDocument) assets[11], (GuiJsonDocument) assets[12]);

        // Start the game
        eventHub.emit(new ForgetPagesEvent());
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
