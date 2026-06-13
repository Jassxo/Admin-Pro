package admintools.config;

public class ModConfig {
    public boolean commandDisplayEnabled = true;
    public boolean commandDisplayOwnCommands = true;
    public boolean commandDisplayStaffOnly = false;
    public int commandDisplayTimeSeconds = 5;

    public String chatFilterMode = "OFF"; // OFF, NORMAL_CHAT, SERVER_ONLY, PLUGIN_MESSAGES_ONLY, STAFF_CHAT_ONLY, COMMAND_OUTPUT_ONLY, CUSTOM
    public String chatFilterCustomRegex = "";

    public ModConfig() {}
}
