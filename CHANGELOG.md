Changelog
=========
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/).

## [2.11.0] - 2025-07-05
### Changed
- **Requirement update:** IntelliJ Platform 252.23591.19 (2025.2 EAP) is now the minimal supported version.
- Minor internal refactoring for better compatibility with newer IntelliJ and general code improvement.

## [2.10.0] - 2025-04-15
### Changed
- **Requirement update:** IntelliJ Platform 251.23774.318 is now the minimal supported version (only EAP builds for now).
- Update PSScriptAnalyzer from v1.23.0 to [v1.24.0](https://github.com/PowerShell/PSScriptAnalyzer/releases/tag/1.24.0).
- Update PowerShellEditorServices from v4.2.0 to [v4.3.0](https://github.com/PowerShell/PowerShellEditorServices/releases/tag/v4.3.0).

### Fixed
- [#323: Configurations with PowerShell "Before task" don't work in Debug](https://github.com/ant-druha/intellij-powershell/issues/323).
- [#352](https://github.com/ant-druha/intellij-powershell/issues/352): fix an assertion thrown from settings in IntelliJ IDEA 2025.1. Thanks to @Fantoom for the contribution.

## [2.9.0] - 2025-02-07
### Changed
- **Requirement update:** IntelliJ Platform 251.20015 is now the minimal supported version (only EAP builds for now).
- Update PowerShellEditorServices from v3.20.1 to [v4.2.0](https://github.com/PowerShell/PowerShellEditorServices/releases/tag/v4.2.0).
- Used library updates and minor code cleanup.

## [2.8.0] - 2024-10-20
### Added
- Debugger: "verified" status for breakpoints that are successfully set. Thanks to @Fantoom for the contribution.

### Changed
- **Requirement update:** IntelliJ Platform 2024.2 is now the minimal supported version (i.e., a downgrade, meaning we support more versions).
- Update PSScriptAnalyzer from v1.22.0 to v1.23.0.
- Update junixsocket library from v2.10.0 to v2.10.1.

## [2.7.0] - 2024-08-21
### Changed
- **Requirement update:** IntelliJ Platform 2024.2.0.2 is now the minimal supported version.
- Update PowerShellEditorServices from v3.18.1 to v3.20.1.
- Update junixsocket library from v2.9.0 to v2.10.0.
- Update LSP4J library from v0.22.0 to v0.23.0.

### Added
- **Debugger support** based on the debugger adapter protocol from PowerShellEditorServices.

  Thanks to @Fantoom for the contribution.

## [2.6.1] - 2024-03-28
### Fixed
- [#257](https://github.com/ant-druha/intellij-powershell/pull/257): pass `-NoProfile` to PowerShell when detecting its version, to determine the version faster and avoid possible problems caused by shell customizations

  Thanks to @En3Tho for the contribution.
- [#259](https://github.com/ant-druha/intellij-powershell/pull/259): additionally, pass `-NonInteractive` argument

## [2.6.0] - 2024-03-24
### Changed
- **Requirement update!** If you set the plugin to use PowerShell (aka PowerShell Core, as opposed to Windows PowerShell), then PowerShell 7.2.11 is the minimal supported version

  The requirements related to Windows PowerShell version are not changed.
- [#22](https://github.com/ant-druha/intellij-powershell/issues/22): the default working directory for a newly created **PowerShell** run configuration is now the script's parent directory

  Configurations created by previous versions of the plugin are unchanged.
- [#176: Migrate to PowerShell Core by default](https://github.com/ant-druha/intellij-powershell/issues/176)

  If available on the target system, the modern PowerShell will be used be default, if available. Note that only new plugin installations will be affected, due to how we store the plugin settings. If the plugin is already installed, then it will use the previously discovered PowerShell instance.

  This is a Windows-only change, because only on Windows there are two PowerShell versions that may coexist.
- [#229](https://github.com/ant-druha/intellij-powershell/issues/229): the **PowerShell** run configuration will now save all the files before executing

  This fixes the cases when the started configuration wasn't using the latest version of an edited script file.
- [#51: Update PowerShellEditorServices](https://github.com/ant-druha/intellij-powershell/issues/51) to v3.18.1
- PSScriptAnalyzer has been updated to the latest v1.22.0

### Removed
- The plugin no longer bundles Plaster that was a part of an older PowerShellEditorServices distribution, even though it wasn't used by the plugin.

## [2.5.0] - 2024-03-12
### Fixed
- [#67](https://github.com/ant-druha/intellij-powershell/issues/67): An attribute block before function's `param` could lead to incorrect indentation
- [#102](https://github.com/ant-druha/intellij-powershell/issues/102): Formatter breaking the code around unary operators such as `-Not` and equal signs in command-line arguments
- [#183: Redundant multi line comment closing sequence added when user adds newline](https://github.com/ant-druha/intellij-powershell/issues/183)
- [#138: Run configuration template doesn't work](https://github.com/ant-druha/intellij-powershell/issues/138)

### Added
- [#184](https://github.com/ant-druha/intellij-powershell/issues/184): Support the null-coalescing operator

### Changed
- [LSP4J](https://github.com/eclipse-lsp4j/lsp4j) library has been updated to v0.3.1
- [junixsocket](https://github.com/kohlschutter/junixsocket) has been updated to 2.9.0

## [2.4.0] - 2024-03-09
### Changed
- The lexer and parser were migrated to be auto-generated by the Gradle Grammar-Kit task

  We do not expect any changes, but they may happen, so please report any cases of suspicious plugin behavior.

### Fixed
- [#79: Can't detect PowerShell version on Linux](https://github.com/ant-druha/intellij-powershell/issues/79)

  Thanks to @8ru73u5 for investigation of the issue.

  This problem was caused by [the PowerShell issue #11747](https://github.com/PowerShell/PowerShell/issues/11747).
- A bit more of IO work during language server connection moved to the corresponding threads

  This should save some IDE responsibility in rare cases when it could be a problem.
- Increased timeout on connecting to the language server; should fix issues on slower computers
- [#42: The run configuration now uses terminal encoding](https://github.com/ant-druha/intellij-powershell/issues/42) (UTF-8 by default)
- [#17: When the username has a space, the plugin fails to initialize](https://github.com/ant-druha/intellij-powershell/issues/17)

## [2.3.1] - 2023-12-30
### Fixed
- [#172](https://github.com/ant-druha/intellij-powershell/issues/172): Console output forcibly wraps output to 80 characters
- [#160](https://github.com/ant-druha/intellij-powershell/issues/160): the `exit` keyword is now allowed in more positions in the parser
- [#166](https://github.com/ant-druha/intellij-powershell/issues/166): "Slow operations are prohibited on EDT" report about disk IO on run configuration startup
- [#154](https://github.com/ant-druha/intellij-powershell/issues/154): Input not properly processed in the run configuration
### Changed
- The **PowerShell** run configuration no longer ignores certain kinds of internal exceptions; they will be properly reported

## [2.3.0] - 2023-10-29
### Changed
- The PowerShellEditorServices connection code has been refactored to not cause any UI freezes: everything was moved to background and rewritten to use non-blocking IO as much as possible

### Fixed
- [#155](https://github.com/ant-druha/intellij-powershell/issues/155): cannot start PowerShellEditorServices under the default PowerShell 5 execution policy, `Restricted`
- [#104](https://github.com/ant-druha/intellij-powershell/issues/104) (fixed partially): use less restricting flags when opening pipe connections to PowerShellEditorServices

### Added
- [#144](https://github.com/ant-druha/intellij-powershell/issues/144): terminate the language server processes when exiting the IDE (even if the IDE was terminated abnormally)

## [2.2.0] - 2023-10-01
### Changed
- Cleaned up a lot of deprecated API usages and warnings (by @VladRassokhin)
- [#148](https://github.com/ant-druha/intellij-powershell/issues/148): drop Gson and Guava dependencies from the plugin
- [#88](https://github.com/ant-druha/intellij-powershell/issues/88): fix UI freezes caused by the plugin and migrate all the LSP communication to background threads

### Added
- [#145](https://github.com/ant-druha/intellij-powershell/pull/145): log PowerShellEditorServices output

## [2.1.0] - 2023-09-13
### Changed
- The minimal supported IntelliJ version is now 2023.2
- [#45](https://github.com/ant-druha/intellij-powershell/pull/45): Upgrade PSScriptAnalyzer from 1.17.1 to 1.21.0, thanks to @bergmeister
- [#75](https://github.com/ant-druha/intellij-powershell/pull/75): Add indents for array elements when formatting, thanks to @VladRassokhin
- [#90](https://github.com/ant-druha/intellij-powershell/pull/90): Allow for more user interaction with the run configuration in terminal (use PTY when available), thanks to @ZhengKeli
- [#89](https://github.com/ant-druha/intellij-powershell/pull/89): Allow entering the executables from `PATH` in the plugin settings, thanks to @ZhengKeli

### Fixed
- [#124](https://github.com/ant-druha/intellij-powershell/pull/124): Support placing function name on next line in function definition, thanks to @VladRassokhin

### Added
- [#128](https://github.com/ant-druha/intellij-powershell/pull/128): The optional error reports are now sent to the Marketplace in newer IDE versions after the user approval

## [2.0.10] - 2022-02-25
### Fixed
- [#76](https://github.com/ant-druha/intellij-powershell/issues/76): Exception thrown when plugin is enabled but no PowerShell is found

## [2.0.9] - 2021-11-30
### Fixed
- Properly mark the plugin as compatible with IntelliJ 2021.3+

## [2.0.8] - 2021-11-19
### Changed
- The minimal supported IntelliJ version is now 2021.3

### Added
- [#63](https://github.com/ant-druha/intellij-powershell/issues/63): support Path Macros for the run configuration

## [2.0.7] - 2021-05-25
### Added
- Ability to set PowerShell version globally and for Run Configuration
- Run Configuration: ability to specify environment variables
- macOS ARM support

## [2.0.6] - 2021-03-13
### Changed
- The minimal supported IntelliJ version is now 2021.1

### Fixed
- [#40](https://github.com/ant-druha/intellij-powershell/issues/40): Run configuration keeps getting renamed when opening or editing, thanks to @Dimi1010

## [2.0.5] - 2020-08-14
### Added
- [#48](https://github.com/ant-druha/intellij-powershell/pull/48) Adds simplest code folding: for code blocks, arrays, thanks to @VladRassokhin

## [2.0.4] - 2019-11-22
### Fixed
- [#38](https://github.com/ant-druha/intellij-powershell/issues/38): UnsupportedClassVersionError running 2019.3 under 1.8 JDK

## [2.0.3] - 2019-11-03
### Removed
- [#33](https://github.com/ant-druha/intellij-powershell/issues/33): Get rid of implicit dependency on Java plugin

### Changed
- Platform API maintenance update

### Fixed
- PSES startup script path wrong for standalone unpacked installation
- Stack overflow in RenamePsiElementProcessor.findReferences

## [2.0.2] - 2019-05-23
### Changed
- The minimal supported IntelliJ version is now 2019.2 compatibility

### Fixed
- [#24](https://github.com/ant-druha/intellij-powershell/issues/24): Parenthesized expression parsing error

## [2.0.1] - 2019-01-17
### Fixed
- [#10](https://github.com/ant-druha/PowerShell/issues/10): Script Arguments not working as expected
- [#12](https://github.com/ant-druha/PowerShell/issues/12): Memory (project) leak in plugin, thanks to @VladRassokhin

## [2.0.0-IJ2018.2.X] - 2018-12-07
Same as **2.0.0**, but compatible with IntelliJ 2018.2+.

## [2.0.0] - 2018-12-07
### Added
- [#9](https://github.com/ant-druha/PowerShell/issues/9): PowerShell ScriptAnalyzer support in IDE Editor
- [#3](https://github.com/ant-druha/PowerShell/issues/3) Run/Debug configuration: ability to set working directory

### Changed
- [#6](https://github.com/ant-druha/PowerShell/issues/6): Update PowerShell Language server version

### Fixed
- [#5](https://github.com/ant-druha/PowerShell/issues/5): Reformat code Error
- [#2](https://github.com/ant-druha/PowerShell/issues/2): NullPointerException during normal IDE operation
- [#1](https://github.com/ant-druha/PowerShell/issues/1): Exception on PyCharm launch

## [1.1.1] - 2018-06-21
### Added
- [#22 (BitBucket)](https://bitbucket.org/adernov/powershell/issues/22): Integrated PowerShell Console
- [#18 (BitBucket)](https://bitbucket.org/adernov/powershell/issues/18): Remote files editing with 'psedit' command in PowerShell console

## [1.1.0] - 2018-05-15
### Added
- Language injections in interpolated strings

### Fixed
- [#19 (BitBucket)](https://bitbucket.org/adernov/powershell/issues/19): Source formatting does not omit "@ at the beginning of line
- [#20 (BitBucket)](https://bitbucket.org/adernov/powershell/issues/20): Handle drive letters in unc path
- [#21 (BitBucket)](https://bitbucket.org/adernov/powershell/issues/21): `NoClassDefFoundError` if IntelliLang plugin is not installed

## [1.0.2] - 2018-01-31
### Fixed
- [#11 (BitBucket)](https://bitbucket.org/adernov/powershell/issues/11): `using` keyword not being recognized

### Added
- More pre-defined [Live Template](https://www.jetbrains.com/help/idea/live-templates.html) snippets

### Changed
- Use `-File` parameter to specify the script file to run

## [1.0.1] - 2018-01-25
### Fixed
- [#8 (BitBucket)](https://bitbucket.org/adernov/powershell/issues/8): Exception shown on LSP communication failure
- [#9 (BitBucket)](https://bitbucket.org/adernov/powershell/issues/9): Language server fails to start if path to startup script contains spaces

## [1.0.0] - 2018-01-22
### Added
- Language Server Protocol support. Plugin now bundles [PowerShellEditorServices module](https://github.com/PowerShell/PowerShellEditorServices) (same as VSCode) to provide Editor assistance. Supported requests:
  - `textDocument/didOpen` (File opened in editor)
  - `textDocument/didClose` (File closed in editor)
  - `textDocument/didChange` (Editor document text changes)
  - `textDocument/completion` (Editor Auto completion)

## [0.0.3] - 2018-01-16
### Added
- [#3 (BitBucket)](https://bitbucket.org/adernov/powershell/issues/3): Support PowerShell Scopes like Global or Local for functions
- [#5 (BitBucket)](https://bitbucket.org/adernov/powershell/issues/5): Support more PowerShell file extensions

### Fixed
- [#7 (BitBucket)](https://bitbucket.org/adernov/powershell/issues/7): `StackOverflowError` on resolving variable that has no definition

## [0.0.2] - 2017-12-25
### Changed
Enhanced completion:
- resolve and completion of declared in script type members (methods, properties, constructors);
- completion for different contexts (references, types, properties).

## [0.0.1] - 2017-12-06
Initial editor and code completion support:
- syntax highlighting;
- variables and declared types reference resolve and find usages;
- reference and keyword completion;
- rename refactoring;
- structure view;
- colors and fonts;
- code style;
- live templates;
- run configuration.

[0.0.1]: https://github.com/ant-druha/intellij-powershell/releases/tag/v0.0.1
[0.0.2]: https://github.com/ant-druha/intellij-powershell/compare/v0.0.1...v0.0.2
[0.0.3]: https://github.com/ant-druha/intellij-powershell/compare/v0.0.2...v0.0.3
[1.0.0]: https://github.com/ant-druha/intellij-powershell/compare/v0.0.3...v1.0.0
[1.0.1]: https://github.com/ant-druha/intellij-powershell/compare/v1.0.0...v1.0.1
[1.0.2]: https://github.com/ant-druha/intellij-powershell/compare/v1.0.1...v1.0.2
[1.1.0]: https://github.com/ant-druha/intellij-powershell/compare/v1.0.2...v1.1.0
[1.1.1]: https://github.com/ant-druha/intellij-powershell/compare/v1.1.0...v1.1.1
[2.0.0]: https://github.com/ant-druha/intellij-powershell/compare/v1.1.1...v2.0.0
[2.0.0-IJ2018.2.X]: https://github.com/ant-druha/intellij-powershell/compare/v2.0.0...v2.0.0-IJ2018.2.X
[2.0.1]: https://github.com/ant-druha/intellij-powershell/compare/v2.0.0...v2.0.1
[2.0.2]: https://github.com/ant-druha/intellij-powershell/compare/v2.0.1...v2.0.2
[2.0.3]: https://github.com/ant-druha/intellij-powershell/compare/v2.0.2...v2.0.3
[2.0.4]: https://github.com/ant-druha/intellij-powershell/compare/v2.0.3...v2.0.4
[2.0.5]: https://github.com/ant-druha/intellij-powershell/compare/v2.0.4...v2.0.5
[2.0.6]: https://github.com/ant-druha/intellij-powershell/compare/v2.0.5...v2.0.6
[2.0.7]: https://github.com/ant-druha/intellij-powershell/compare/v2.0.6...v2.0.7
[2.0.8]: https://github.com/ant-druha/intellij-powershell/compare/v2.0.7...v2.0.8
[2.0.9]: https://github.com/ant-druha/intellij-powershell/compare/v2.0.8...v2.0.9
[2.0.10]: https://github.com/ant-druha/intellij-powershell/compare/v2.0.9...v2.0.10
[2.1.0]: https://github.com/ant-druha/intellij-powershell/compare/v2.0.10...v2.1.0
[2.2.0]: https://github.com/ant-druha/intellij-powershell/compare/v2.1.0...v2.2.0
[2.3.0]: https://github.com/ant-druha/intellij-powershell/compare/v2.2.0...v2.3.0
[2.3.1]: https://github.com/ant-druha/intellij-powershell/compare/v2.3.0...v2.3.1
[2.4.0]: https://github.com/ant-druha/intellij-powershell/compare/v2.3.1...v2.4.0
[2.5.0]: https://github.com/ant-druha/intellij-powershell/compare/v2.4.0...v2.5.0
[2.6.0]: https://github.com/ant-druha/intellij-powershell/compare/v2.5.0...v2.6.0
[2.6.1]: https://github.com/ant-druha/intellij-powershell/compare/v2.6.0...v2.6.1
[2.7.0]: https://github.com/ant-druha/intellij-powershell/compare/v2.6.1...v2.7.0
[2.8.0]: https://github.com/ant-druha/intellij-powershell/compare/v2.7.0...v2.8.0
[2.9.0]: https://github.com/ant-druha/intellij-powershell/compare/v2.8.0...v2.9.0
[2.10.0]: https://github.com/ant-druha/intellij-powershell/compare/v2.9.0...v2.10.0
[2.11.0]: https://github.com/ant-druha/intellij-powershell/compare/v2.10.0...v2.11.0
[Unreleased]: https://github.com/ant-druha/intellij-powershell/compare/v2.11.0...HEAD
