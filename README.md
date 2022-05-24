![](https://img.shields.io/badge/-Hammer-orange?style=for-the-badge&logo=appveyor)

A modular library to assist in mod development for the [Fabric](https://fabricmc.net) toolchain.

All modules use the same version, which can be found in `gradle.properties`.

- **How do I import them?**

  They are individually published, and their repository should be added using Gradle.
    ```groovy
    repositories {
        maven {
            name = "vini2003.dev"
            url = "https://maven.vini2003.dev/"
        }
    }
    ```

<br>

![](https://img.shields.io/badge/-Hammer%20Adventure-blue?style=for-the-badge&logo=appveyor)

A module containing compatibility tools used with Kyori's [Adventure](https://github.com/KyoriPowered/adventure) API.

- **What can I use it for?**

  This module adds compatibility with the Adventure API, making message, boss bar and title/subtitle management simpler.


- **Where can I find its documentation?**

  This module's documentation can be found in its [Wiki](https://github.com/vini2003/Hammer/wiki/Hammer-Adventure).


- **How do I import it?**

  This module should be imported using Gradle.

    ```groovy
    modImplementation("dev.vini2003:hammer-adventure:${hammerVersion}")
    ```

<br>

![](https://img.shields.io/badge/-Hammer%20Border-blue?style=for-the-badge&logo=appveyor)

A module containing a world border implementation with a ceiling and a floor.

- **What can I use it for?**

  This module is useful for when players must be confined to a small space without unrestricted vertical movement.


- **Where can I find its documentation?**

  This module's documentation can be found in its [Wiki](https://github.com/vini2003/Hammer/wiki/Hammer-Border).


- **How do I import it?**

  This module should be imported using Gradle.

    ```groovy
    modImplementation("dev.vini2003:hammer-border:${hammerVersion}")
    ```

<br>

![](https://img.shields.io/badge/-Hammer%20Chat-blue?style=for-the-badge&logo=appveyor)

A module containing utilities to toggle chat, global chat and command feedback.

- **What can I use it for?**

  This module is useful for when players must have their chat usage restricted, or command feedback must not be shown on screen.


- **Where can I find its documentation?**

  This module's documentation can be found in its [Wiki](https://github.com/vini2003/Hammer/wiki/Hammer-Chat).


- **How do I import it?**

  This module should be imported using Gradle.

    ```groovy
    modImplementation("dev.vini2003:hammer-chat:${hammerVersion}")
    ```

<br>

![](https://img.shields.io/badge/-Hammer%20Gravity-blue?style=for-the-badge&logo=appveyor)

A module containing a gravity implementation.

- **What can I use it for?**

  This module is useful for when a world's gravity must be changed.


- **Where can I find its documentation?**

  This module's documentation can be found in its [Wiki](https://github.com/vini2003/Hammer/wiki/Hammer-Gravity).


- **How do I import it?**

  This module should be imported using Gradle.

    ```groovy
    modImplementation("dev.vini2003:hammer-gravity:${hammerVersion}")
    ```

<br>

![](https://img.shields.io/badge/-Hammer%20Interaction-blue?style=for-the-badge&logo=appveyor)

A module containing interaction restriction mechanisms.

- **What can I use it for?**

  This module is useful for when players must have their interaction abilities restricted.


- **Where can I find its documentation?**

  This module's documentation can be found in its [Wiki](https://github.com/vini2003/Hammer/wiki/Hammer-Interaction).


- **How do I import it?**

  This module should be imported using Gradle.

    ```groovy
    modImplementation("dev.vini2003:hammer-interaction:${hammerVersion}")
    ```

<br>

![](https://img.shields.io/badge/-Hammer%20GUI-blue?style=for-the-badge&logo=appveyor)

A module containing a complete toolset for creating in-game interfaces.

- **What can I use it for?**

  This module is useful for when Minecraft's screen system is insufficient or too complicated for your use cases.


- **Where can I find its documentation?**

  This module's documentation can be found in its [Wiki](https://github.com/vini2003/Hammer/wiki/Hammer-GUI).


- **How do I import it?**

  This module should be imported using Gradle.

    ```groovy
    modImplementation("dev.vini2003:hammer-gui:${hammerVersion}")
    ```

<br>

![](https://img.shields.io/badge/-Hammer%20GUI%20Energy-blue?style=for-the-badge&logo=appveyor)

A module containing compatibility tools between the **GUI** module and TeamReborn's [Energy](https://github.com/TechReborn/Energy) API.

- **What can I use it for?**

  This module is useful for when an in-game interface must handle energy.


- **Where can I find its documentation?**

  This module's documentation can be found in its [Wiki](https://github.com/vini2003/Hammer/wiki/Hammer-GUI-Energy).


- **How do I import it?**

  This module should be imported using Gradle.

    ```groovy
    modImplementation("dev.vini2003:hammer-gui-energy:${hammerVersion}")
    ```

<br>

![](https://img.shields.io/badge/-Hammer%20Permission-blue?style=for-the-badge&logo=appveyor)

A module containing a complete toolset for creating in-game roles and manipulating player permissions.

- **What can I use it for?**

  This module is useful for when roles or permissions must be used.


- **Where can I find its documentation?**

  This module's documentation can be found in its [Wiki](https://github.com/vini2003/Hammer/wiki/Hammer-Permission).


- **How do I import it?**

  This module should be imported using Gradle.

    ```groovy
    modImplementation("dev.vini2003:hammer-permission:${hammerVersion}")
    ```

<br>

![](https://img.shields.io/badge/-Hammer%20Util-blue?style=for-the-badge&logo=appveyor)

A module containing miscellaneous utilities.

- **What can I use it for?**

  This module is useful for manipulating a player's walking/flying speed, or freezing/unfreezing them.


- **Where can I find its documentation?**

  This module's documentation can be found in its [Wiki](https://github.com/vini2003/Hammer/wiki/Hammer-Util).


- **How do I import it?**

  This module should be imported using Gradle.

    ```groovy
    modImplementation("dev.vini2003:hammer-util:${hammerVersion}")
    ```

