Changelog
=========
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/).

## [Unreleased] (2.1.0)
### Changed
- The minimal supported IntelliJ version is now 2023.2
- [#45](https://github.com/ant-druha/intellij-powershell/pull/45): Upgrade PSScriptAnalyzer from 1.17.1 to 1.19.0, thanks to @bergmeister
- [#75](https://github.com/ant-druha/intellij-powershell/pull/75): Add indents for array elements when formatting, thanks to @VladRassokhin
- [#90](https://github.com/ant-druha/intellij-powershell/pull/90): Allow for more user interaction with the run configuration in terminal (use PTY when available), thanks to @ZhengKeli
- [#89](https://github.com/ant-druha/intellij-powershell/pull/89): Allow entering the executables from `PATH` in the plugin settings, thanks to @ZhengKeli

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
- More pre-defined [Live Template](<a href="https://www.jetbrains.com/help/idea/live-templates.html) snippets

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
Initial editor intellisense support:
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
[Unreleased]: https://github.com/ant-druha/intellij-powershell/compare/v2.0.10...HEAD
