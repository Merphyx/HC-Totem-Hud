package de.myronx.hctotemhud.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ModMenuConfig {
    public static Screen create(Screen parent) {

        Config config = Config.getInstance();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.of("Config"))
                .setSavingRunnable(config::save);

        ConfigCategory general = builder.getOrCreateCategory(Text.of("General"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        general.addEntry(entryBuilder.startBooleanToggle(Text.of("Hud"), config.toggleMod)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.toggleMod = newValue)
                .setTooltip(Text.of("Hier kannst du die \"Mod\" aktivieren / deaktivieren."))
                .build());

        general.addEntry(entryBuilder.startIntSlider(Text.of("Farbschwellenwert"), config.threshold, 3, 15)
                .setDefaultValue(5)
                .setSaveConsumer(newValue -> config.threshold = newValue)
                .setTooltip(Text.of("Hier kannst du einstellen, bis wann der Wert in orange angezeigt werden soll."))
                .build());

        general.addEntry(entryBuilder.startIntField(Text.of("Warnschwellenwert"), config.warningThreshold)
                .setDefaultValue(1)
                .setSaveConsumer(newValue -> config.warningThreshold = newValue)
                .setTooltip(Text.of("Hier kannst du einstellen, ab wann gewarnt werden soll."))
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Text.of("Warnmeldung"), config.warningToggle)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.warningToggle = newValue)
                .setTooltip(Text.of("Hier kannst du die \"Warnmeldung\" aktivieren / deaktivieren."))
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Text.of("Warnton"), config.soundToggle)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.soundToggle = newValue)
                .setTooltip(Text.of("Hier kannst du den \"Warnton\" aktivieren / deaktivieren."))
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Text.of("Vignetteneffekt"), config.vignetteToggle)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.vignetteToggle = newValue)
                .setTooltip(Text.of("Hier kannst du den \"Vignetteneffekt\" aktivieren / deaktivieren."))
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Text.of("Pulsieren"), config.pulsationToggle)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.pulsationToggle = newValue)
                .setTooltip(Text.of("Hier kannst du das Pulsieren von dem Totem-HUD aktivieren / deaktivieren."))
                .build());

        return builder.build();

    }
}
