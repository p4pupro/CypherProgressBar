package teamcypher.cypherprogress.configuration;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import teamcypher.cypherprogress.ImagePreloader;

import java.awt.*;

@State(
        name = "CypherProgress",
        storages = {@Storage("CypherProgresss.xml")}
)
public class CypherProgressState implements PersistentStateComponent<CypherProgressState> {
    public int progressColor = 0x5CAB4C;
    public int backColor = 0x000000;
    public int tintColor = 0xFFFFFF;
    public int selectedImageIndex = 0;
    public boolean useCustomBackColor = false;

    public static CypherProgressState getInstance() {
        return ApplicationManager.getApplication().getService(CypherProgressState.class);
    }

    @Override
    public CypherProgressState getState() {
        return this;
    }

    public int getSelectedImageIndex() {
        return this.selectedImageIndex;
    }

    public void setSelectedImageIndex(int index) {
        this.selectedImageIndex = index;
    }

    public Image getSelectedImage() {
        return switch (selectedImageIndex) {
            case 1 -> ImagePreloader.getMorpheusImage();
            case 2 -> ImagePreloader.getNeoImage();
            case 3 -> ImagePreloader.getTrinityImage();
            default -> ImagePreloader.getCypherImage();
        };
    }

    @Override
    public void loadState(@NotNull CypherProgressState state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
