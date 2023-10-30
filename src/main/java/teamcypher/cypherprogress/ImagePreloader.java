package teamcypher.cypherprogress;

import com.intellij.ide.AppLifecycleListener;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public class ImagePreloader implements AppLifecycleListener {
    private static Image cypherImage;
    private static Image morpheusImage;
    private static Image neoImage;
    private static Image trinityImage;

    public static Image getCypherImage() {
        return cypherImage;
    }

    public static Image getMorpheusImage() {
        return morpheusImage;
    }

    public static Image getNeoImage() {
        return neoImage;
    }

    public static Image getTrinityImage() {
        return trinityImage;
    }

    @Override
    public void appFrameCreated(@NotNull List<String> commandLineArgs) {
        cypherImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/cypher.gif"));
        morpheusImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/morpheus.gif"));
        neoImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/neo.gif"));
        trinityImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/trinity.gif"));

    }
}
