package teamcypher.cypherprogress;

import com.intellij.ide.AppLifecycleListener;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public class ImagePreloader implements AppLifecycleListener {
    private static Image cypherImage;

    public static Image getImage() {
        return cypherImage;
    }

    @Override
    public void appFrameCreated(@NotNull List<String> commandLineArgs) {
        cypherImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/cypher.gif"));
    }
}
