[GPT Assistant](https://github.com/adriantache/GPTAssistant) rewrite to target both Android and Desktop (Windows, macOS,
Linux) targets using Kotlin Multiplatform with ComposeUi (with Ktor for network access, Kotlin Serialization for JSON
manipulation and interop with Ktor, DataStore for storing the API key, settings and local message cache).

This is an app that enables interaction with the latest OpenAI ChatGPT, without requiring a monthly subscription. It
requires an API key, which you can obtain ontheir website at https://platform.openai.com/api-keys, after which you must
purchase API credits at https://platform.openai.com/settings/organization/billing/overview (10$ should last you a long
time if you're not using image generation).

An important feature of this app is that it enables the use of Personas, in order to guide the GPT to respond to your
queries taking on a certain persona, and following certain instructions, leading to better results.

In order to make a build of this project, simply open it in IntelliJ IDEA, double press the CTRL key and type:
`gradle packageDistributionForCurrentOS`. This will generate either an MSI or DMG file, depending on whether you are on
Windows or macOS, respectively. The resulting output will be in `\composeApp\build\compose\binaries\main\`. 
