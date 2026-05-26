# Void Client

Void Client é um projeto de launcher e client Minecraft Java com tema vermelho/preto, inspirado em clients PvP modernos e construído com código próprio.

## Estrutura do projeto

- `launcher/`: launcher Java com UI em JavaFX e suporte à seleção da versão Minecraft.
- `mod/`: mod Forge 1.8.9 com HUD, módulos e menu personalizado.

## Requisitos

- Java 11+ para o launcher
- Java 8 para compilar o mod Forge 1.8.9
- Gradle 6.x ou superior

## Como compilar

1. Abra um terminal no diretório `VoidClient`.
2. Execute `gradle-local.bat build`.
   - Se você quiser usar Gradle global, instale o Gradle e use `gradle build`.
3. O launcher irá empacotar os artefatos em `launcher/build/libs`.
4. O mod será gerado em `mod/build/libs`.

## Como usar

1. Copie o jar do mod `mod/build/libs/mod-1.0.0.jar` para a pasta `mods` do seu perfil Forge 1.8.9.
2. Execute o launcher com `gradle :launcher:run` ou abra o jar gerado.
3. No launcher, selecione a versão `1.8.9` e execute.

## Características iniciais

- Launcher próprio com interface vermelha/preta
- Sistema de seleção de versão e download de arquivos básicos
- Menu principal personalizado dentro do Minecraft
- HUD configurável e arrastável
- Sistema de módulos on/off com vários módulos básicos
- Configuração salva em JSON
- Identificação de jogadores Void Client via canal de rede personalizado

## Recursos previstos

- Autenticação Microsoft/Minecraft oficial
- Cosmetics próprios
- Ajustes adicionais de HUD e menu

## Aviso

Este projeto é construído do zero e não usa código proprietário de Lunar Client, Badlion ou qualquer outro client.
