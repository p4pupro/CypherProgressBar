package teamcypher.cypherprogress.configuration;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

@State(
        name = "CypherProgress",
        storages = {@Storage("CypherProgresss.xml")}
)
public class CypherProgressState implements PersistentStateComponent<CypherProgressState> {
    public int progressColor = 0x5CAB4C;
    public int backColor = 0x000000;
    public int tintColor = 0xFFFFFF;
    public boolean useCustomBackColor = false;

    public static CypherProgressState getInstance() {
        return ApplicationManager.getApplication().getService(CypherProgressState.class);
    }

    @Override
    public CypherProgressState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull CypherProgressState state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
