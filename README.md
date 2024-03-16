# intellij-powershell [![JetBrains Plugins][badge-plugins]][plugin-repository]
Adds PowerShell language support to [IntelliJ-based](https://www.jetbrains.com/products.html?type=ide) IDEs.

### Usage:
Install the plugin [from IDE](https://www.jetbrains.com/help/idea/managing-plugins.html#repos) or download it from [IntelliJ plugins repository](https://plugins.jetbrains.com/plugin/10249-powershell) and choose to [install from disk](https://www.jetbrains.com/help/idea/managing-plugins.html#installing-plugins-from-disk).


## Features

#### Editor:
- Syntax highlighting;
- Reference and keyword completion;
- [PowerShell ScriptAnalyzer](https://github.com/PowerShell/PSScriptAnalyzer) code checker inspections in Editor;
- [Language Injections](https://www.jetbrains.com/help/idea/using-language-injections.html) in strings;
- [Colors and Fonts customization](https://www.jetbrains.com/help/idea/configuring-colors-and-fonts.html);
- [Code style and reformat](https://www.jetbrains.com/help/idea/using-code-editor.html#reformat_rearrange_code);
- [Live templates](https://www.jetbrains.com/help/idea/using-live-templates.html) (code snippets)

#### Refactoring
- [Rename](https://www.jetbrains.com/help/idea/rename-refactorings.html)

#### Navigation
- Variables and declared types reference resolve and find usages;
- [Structure view](https://www.jetbrains.com/help/idea/viewing-structure-of-a-source-file.html)

#### Execution
- [Run Configuration](https://www.jetbrains.com/help/idea/run-debug-configuration.html);
- Integrated PowerShell Console (Tools | PowerShell Console... action);
- Remote files editing with 'psedit' command in PowerShell console (see [psedit support](https://bitbucket.org/adernov/powershell/issues/18/psedit-support#comment-46351640)):
![Remote file editing](https://bitbucket.org/adernov/powershell/issues/attachments/18/adernov/powershell/1529580003.05/18/remote_files_editing.gif)

Settings
--------
The **PowerShell** run configuration uses the selected terminal encoding (UTF-8 by default).

Open **Advanced settings** and look for **Terminal character encoding** if you want to change that.

Third-Party Software
--------------------
This plugin bundles the following third-party software:

- [PowerShell Editor Services](https://github.com/PowerShell/PowerShellEditorServices),
- [PSScriptAnalyzer](https://github.com/PowerShell/PSScriptAnalyzer).

Documentation
-------------
- [Changelog][docs.changelog]
- [Contributor Guide][docs.contributing]
- [Maintainership][docs.maintainership]

[License][docs.license]
---------
Copyright 2018 Andrey Dernov

Copyright 2024 intellij-powershell contributors

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

[badge-plugins]: https://img.shields.io/jetbrains/plugin/v/10249?label=powershell
[docs.changelog]: ./CHANGELOG.md
[docs.contributing]: ./CONTRIBUTING.md
[docs.license]: ./LICENSE
[plugin-repository]: https://plugins.jetbrains.com/plugin/10249-powershell
[docs.maintainership]: ./MAINTAINERSHIP.md
