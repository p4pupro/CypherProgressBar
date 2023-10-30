package teamcypher.cypherprogress;

import com.intellij.ide.ui.LafManager;
import com.intellij.ide.ui.LafManagerListener;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ProgressBarLafManagerListener implements LafManagerListener {
    @Override
    public void lookAndFeelChanged(@NotNull LafManager lafManager) {
        updateMatrixProgressBarUI();
    }

    private static void updateMatrixProgressBarUI() {
        UIManager.put("ProgressBarUI", MatrixProgressBarUI.class.getName());
        UIManager.getDefaults().put(MatrixProgressBarUI.class.getName(), MatrixProgressBarUI.class);
    }
}
