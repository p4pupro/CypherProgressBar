package teamcypher.cypherprogress.configuration;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Provides controller functionality for application settings.
 */
public class CypherProgressConfigurable implements Configurable {

    private CypherProgressSettingsComponent settingsComponent;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Cypher Progress Bar";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        settingsComponent = new CypherProgressSettingsComponent();
        reset();
        return settingsComponent.getPanel();
    }


    @Override
    public boolean isModified() {
        CypherProgressState settings = CypherProgressState.getInstance();
        boolean modified = settingsComponent.getProgressColor() != settings.progressColor
                | settingsComponent.getBackColor() != settings.backColor
                | settingsComponent.getTintColor() != settings.tintColor
                | settingsComponent.getCustomBackEnabled() != settings.useCustomBackColor
                | settingsComponent.getSelectedImageIndex() != settings.getSelectedImageIndex();

        if (modified) {
            CypherProgressState s = new CypherProgressState();
            s.progressColor = settingsComponent.getProgressColor();
            s.tintColor = settingsComponent.getTintColor();
            s.backColor = settingsComponent.getBackColor();
            s.useCustomBackColor = settingsComponent.getCustomBackEnabled();
            settingsComponent.updateMatrixProgressBars(s);
        }
        return modified;
    }

    @Override
    public void apply() {
        CypherProgressState settings = CypherProgressState.getInstance();
        settings.progressColor = settingsComponent.getProgressColor();
        settings.backColor = settingsComponent.getBackColor();
        settings.tintColor = settingsComponent.getTintColor();
        settings.useCustomBackColor = settingsComponent.getCustomBackEnabled();
        settings.selectedImageIndex = settingsComponent.getSelectedImageIndex();
        settingsComponent.updateMatrixProgressBars();
    }


    @Override
    public void reset() {
        CypherProgressState settings = CypherProgressState.getInstance();
        settingsComponent.setProgressColor(settings.progressColor);
        settingsComponent.setBackColor(settings.backColor);
        settingsComponent.setTintColor(settings.tintColor);
        settingsComponent.setCheckboxCustomBack(settings.useCustomBackColor);
        settingsComponent.setSelectedImageIndex(settings.getSelectedImageIndex());
    }


    @Override
    public void disposeUIResources() {
        settingsComponent = null;
    }

}