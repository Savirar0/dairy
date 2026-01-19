; Memento Installer Script
[Setup]
AppName=Memento
AppVersion=1.0
AppPublisher=YourName
AppPublisherURL=https://github.com/yourusername
DefaultDirName={autopf}\Memento
DefaultGroupName=Memento
AllowNoIcons=yes
OutputDir=installer_output
OutputBaseFilename=Memento_Setup
SetupIconFile=assets\icon.ico
Compression=lzma2
SolidCompression=yes
UninstallDisplayIcon={app}\icon.ico
PrivilegesRequired=admin
WizardStyle=modern

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"

[Files]
Source: "portable\Memento\Memento.bat"; DestDir: "{app}"; Flags: ignoreversion
Source: "portable\Memento\Memento.jar"; DestDir: "{app}"; Flags: ignoreversion
Source: "portable\Memento\icon.ico"; DestDir: "{app}"; Flags: ignoreversion
Source: "portable\Memento\javafx\*"; DestDir: "{app}\javafx"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "portable\Memento\lib\*"; DestDir: "{app}\lib"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
Name: "{group}\Memento"; Filename: "{app}\Memento.bat"; IconFilename: "{app}\icon.ico"
Name: "{group}\Uninstall Memento"; Filename: "{uninstallexe}"
Name: "{autodesktop}\Memento"; Filename: "{app}\Memento.bat"; IconFilename: "{app}\icon.ico"; Tasks: desktopicon

[Run]
Filename: "{app}\Memento.bat"; Description: "{cm:LaunchProgram,Memento}"; Flags: nowait postinstall skipifsilent shellexec