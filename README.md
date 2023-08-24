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

[badge-plugins]: https://img.shields.io/jetbrains/plugin/v/10249?label=powershell
[plugin-repository]: https://plugins.jetbrains.com/plugin/10249-powershell
