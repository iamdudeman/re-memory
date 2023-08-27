package technology.sola.engine.rememory.render;

import technology.sola.ecs.World;
import technology.sola.engine.defaults.graphics.modules.SolaGraphicsModule;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.rememory.attributes.PlayerAttributeContainer;
import technology.sola.math.linear.Matrix3D;

public class GrainyGraphicsModule extends SolaGraphicsModule {
  private final PlayerAttributeContainer playerAttributeContainer;

  public GrainyGraphicsModule(PlayerAttributeContainer playerAttributeContainer) {
    this.playerAttributeContainer = playerAttributeContainer;
  }

  @Override
  public void renderMethod(Renderer renderer, World world, Matrix3D cameraScaleTransform, Matrix3D cameraTranslationTransform) {
    renderer.setBlendMode(BlendMode.DISSOLVE);

    int alpha = switch (playerAttributeContainer.getAcceptedPages().keySet().size()) {
        case 0 -> 5;
        case 1, 2 -> 1;
        default -> 0;
    };

      if (alpha > 0) {
      renderer.fillRect(0, 0, renderer.getWidth(), renderer.getHeight(), new Color(alpha, 220, 220, 220));
    }
  }

  @Override
  public int getOrder() {
    return 20;
  }
}
