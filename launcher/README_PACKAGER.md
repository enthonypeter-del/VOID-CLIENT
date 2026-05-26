Void Client Launcher — Packaging Guide

This project uses the `org.beryx.runtime` Gradle plugin to automate `jlink` + `jpackage`.

Requirements
- JDK 17+ with `jlink` and `jpackage` (AdoptOpenJDK/Temurin/Oracle/OpenJDK builds typically include these tools).
- JavaFX SDK matching your Java version (e.g. JavaFX 11.0.2). Download from https://openjfx.io and unzip.

Recommended packaging steps (Windows)
1. Set environment variables for this PowerShell session:

```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
```

2. Provide the JavaFX SDK path to the Gradle task. Replace the path with your JavaFX SDK `lib` folder.

```powershell
.\gradle-local.bat :launcher:createInstaller -PjavafxSdk="C:\path\to\javafx-sdk-11.0.2\lib"
```

What this does
- `org.beryx.runtime` will run `jlink` to create a minimal runtime image that includes only required modules (including JavaFX ones you specified).
- Then it runs `jpackage` to create a Windows `.exe` installer containing the runtime image and your launcher JAR.
- Using `jlink` dramatically reduces the final size and improves startup performance.

Troubleshooting
- If `jpackage` is not found, ensure your `JAVA_HOME` points to a JDK that contains `jpackage`.
- If memory errors occur during Gradle build on your machine, increase pagefile size or run on a machine with more RAM.

If you want, I can further automate:
- Downloading JavaFX SDK automatically during build
- Creating cross-platform installers
- Embedding default mods and a lightweight runtime config

Tell me if you want me to automate JavaFX download and fully run `jlink`+`jpackage` for you here.