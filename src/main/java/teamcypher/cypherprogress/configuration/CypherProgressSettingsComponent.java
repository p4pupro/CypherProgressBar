package teamcypher.cypherprogress.configuration;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.ColorPanel;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;
import teamcypher.cypherprogress.ImagePreloader;
import teamcypher.cypherprogress.MatrixProgressBarUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

/**
 * Supports creating and managing a {@link JPanel} for the Settings Dialog.
 */
public class CypherProgressSettingsComponent {

    private final JPanel myMainPanel;
    private final JProgressBar previewDeterminate = new JProgressBar();
    private final JProgressBar previewIndeteminate = new JProgressBar();
    private final ColorPanel progressColorPicker = new ColorPanel();
    private final ColorPanel tintColorPicker = new ColorPanel();
    private final ColorPanel backColorPicker = new ColorPanel();
    private final ComboBox<String> imageSelector = new ComboBox<>();


    private final JBCheckBox checkboxCustomBack = new JBCheckBox();

    public CypherProgressSettingsComponent() {

        // Setup progress bar icon
        imageSelector.addItem("Cypher");
        imageSelector.addItem("Morpheus");
        imageSelector.addItem("Neo");
        imageSelector.addItem("Trinity");

        setSelectedImageIndex(CypherProgressState.getInstance().getSelectedImageIndex());


        imageSelector.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                CypherProgressState.getInstance().setSelectedImageIndex(imageSelector.getSelectedIndex());
                updateMatrixProgressBarsWithImage();
            }
        });

        // Setup preview progress bars
        previewDeterminate.setValue(50);
        previewIndeteminate.setIndeterminate(true);


        // create panel
        myMainPanel = FormBuilder.createFormBuilder()
                .addComponent(previewDeterminate)
                .addComponent(previewIndeteminate)
                .addSeparator(JBUI.scale(20))
                .addLabeledComponent(new JBLabel("Select character"), imageSelector, 1)
                .addLabeledComponent(new JBLabel("Color"), progressColorPicker, 1)
                .addLabeledComponent(new JBLabel("Tint"), tintColorPicker, 1)
                .addSeparator()
                .addLabeledComponent(new JBLabel("Use custom back color"), checkboxCustomBack, 1)
                .addLabeledComponent(new JBLabel("Background color"), backColorPicker, 1)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    public JPanel getPanel() {
        return myMainPanel;
    }

    public int getProgressColor() {
        return progressColorPicker.getSelectedColor().getRGB();
    }

    public void setProgressColor(int color) {
        progressColorPicker.setSelectedColor(new Color(color));
    }

    public int getTintColor() {
        return tintColorPicker.getSelectedColor().getRGB();
    }

    public void setTintColor(int color) {
        tintColorPicker.setSelectedColor(new Color(color));
    }

    public int getBackColor() {
        return backColorPicker.getSelectedColor().getRGB();
    }

    public void setBackColor(int color) {
        backColorPicker.setSelectedColor(new Color(color));
    }

    public boolean getCustomBackEnabled() {
        return checkboxCustomBack.isSelected();
    }

    public int getSelectedImageIndex() {
        return imageSelector.getSelectedIndex();
    }

    public void setSelectedImageIndex(int index) {
        imageSelector.setSelectedIndex(index);
    }

    public void setCheckboxCustomBack(boolean val) {
        checkboxCustomBack.setSelected(val);
    }

    public void updateMatrixProgressBars() {
        previewDeterminate.setUI(new MatrixProgressBarUI());
        previewIndeteminate.setUI(new MatrixProgressBarUI());
    }

    public void updateMatrixProgressBars(CypherProgressState settings) {
        previewDeterminate.setUI(new MatrixProgressBarUI(settings));
        previewIndeteminate.setUI(new MatrixProgressBarUI(settings));
    }

    public void updateMatrixProgressBarsWithImage() {
        CypherProgressState settings = CypherProgressState.getInstance();
        updateMatrixProgressBars(settings);
    }
}